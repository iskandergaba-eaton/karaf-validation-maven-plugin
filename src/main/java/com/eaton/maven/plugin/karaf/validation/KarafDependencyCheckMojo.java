package com.eaton.maven.plugin.karaf.validation;

import org.apache.maven.execution.MavenSession;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Component;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.DefaultProjectBuildingRequest;
import org.apache.maven.project.MavenProject;
import org.apache.maven.project.ProjectBuildingRequest;
import org.apache.maven.shared.dependency.graph.DependencyCollectorBuilder;
import org.apache.maven.shared.dependency.graph.DependencyGraphBuilder;
import org.apache.maven.shared.dependency.graph.DependencyGraphBuilderException;
import org.apache.maven.shared.dependency.graph.DependencyNode;
import org.apache.maven.shared.dependency.graph.filter.AncestorOrSelfDependencyNodeFilter;
import org.apache.maven.shared.dependency.graph.filter.AndDependencyNodeFilter;
import org.apache.maven.shared.dependency.graph.filter.DependencyNodeFilter;
import org.apache.maven.shared.dependency.graph.traversal.BuildingDependencyNodeVisitor;
import org.apache.maven.shared.dependency.graph.traversal.CollectingDependencyNodeVisitor;
import org.apache.maven.shared.dependency.graph.traversal.DependencyNodeVisitor;
import org.apache.maven.shared.dependency.graph.traversal.FilteringDependencyNodeVisitor;
import org.apache.maven.shared.dependency.graph.traversal.SerializingDependencyNodeVisitor;
import org.apache.maven.shared.dependency.graph.traversal.SerializingDependencyNodeVisitor.GraphTokens;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Analyze the whole project and output incoherent dependencies with some fix tips
 */
@Mojo(name = "check-dependencies", aggregator = true)
public class KarafDependencyCheckMojo extends AbstractMojo {
    // fields -----------------------------------------------------------------

    /**
     * The Maven project.
     */
    @Parameter(defaultValue = "${project}", readonly = true, required = true)
    private MavenProject project;

    @Parameter(defaultValue = "${session}", readonly = true, required = true)
    private MavenSession session;

    /**
     * Contains the full list of projects in the reactor.
     */
    @Parameter(defaultValue = "${reactorProjects}", readonly = true, required = true)
    private List<MavenProject> reactorProjects;

    /**
     * The dependency collector builder to use.
     */
    @Component(hint = "default")
    private DependencyCollectorBuilder dependencyCollectorBuilder;

    /**
     * The dependency graph builder to use.
     */
    @Component(hint = "default")
    private DependencyGraphBuilder dependencyGraphBuilder;

    /**
     * The token set name to use when outputting the dependency tree. Possible
     * values are <code>whitespace</code>, <code>standard</code> or
     * <code>extended</code>, which use whitespace, standard (ie ASCII) or extended
     * character sets respectively.
     *
     * @since 2.0-alpha-6
     */
    @Parameter(property = "tokens", defaultValue = "standard")
    private String tokens;

    // Mojo methods -----------------------------------------------------------

    /*
     * @see org.apache.maven.plugin.Mojo#execute()
     */
    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        try {
            // Building all dependencies trees
            Map<MavenProject, DependencyNode> resolvedDependencies = new HashMap<>();
            MavenProject mavenProject = new MavenProject();

            for (MavenProject project : reactorProjects) {
                System.out.println("Checking project "+project.getName());
                
                var dependencyManagement = mavenProject.getDependencyManagement();
                if(dependencyManagement != null) {
                	System.out.println("Has dependency management " + dependencyManagement.getDependencies().isEmpty());
                }

                ProjectBuildingRequest buildingRequest = new DefaultProjectBuildingRequest(
                        session.getProjectBuildingRequest());
                buildingRequest.setProject(project);
                var dependencyTree = dependencyGraphBuilder.buildDependencyGraph(buildingRequest, null);
                var artifact = dependencyTree.getArtifact();
                System.out.println(artifact);
                resolvedDependencies.put(project, dependencyTree);
                System.out.println(serializeDependencyTree(resolvedDependencies.get(project)));
            }
        } catch (DependencyGraphBuilderException exception) {
            throw new MojoExecutionException("Cannot build project dependency graph", exception);
        }
    }

    // private methods --------------------------------------------------------

    /**
     * Serializes the specified dependency tree to a string.
     *
     * @param theRootNode the dependency tree root node to serialize
     * @return the serialized dependency tree
     */
    private String serializeDependencyTree(DependencyNode theRootNode) {
        StringWriter writer = new StringWriter();

        DependencyNodeVisitor visitor = new SerializingDependencyNodeVisitor(writer, toGraphTokens(tokens));

        // TODO: remove the need for this when the serializer can calculate last nodes
        // from visitor calls only
        visitor = new BuildingDependencyNodeVisitor(visitor);

        DependencyNodeFilter filter = new AndDependencyNodeFilter(new ArrayList<>());

        if (filter != null) {
            CollectingDependencyNodeVisitor collectingVisitor = new CollectingDependencyNodeVisitor();
            DependencyNodeVisitor firstPassVisitor = new FilteringDependencyNodeVisitor(collectingVisitor, filter);
            theRootNode.accept(firstPassVisitor);

            DependencyNodeFilter secondPassFilter = new AncestorOrSelfDependencyNodeFilter(
                    collectingVisitor.getNodes());
            visitor = new FilteringDependencyNodeVisitor(visitor, secondPassFilter);
        }

        theRootNode.accept(visitor);

        return writer.toString();
    }

    /**
     * Gets the graph tokens instance for the specified name.
     *
     * @param theTokens the graph tokens name
     * @return the <code>GraphTokens</code> instance
     */
    private GraphTokens toGraphTokens(String theTokens) {
        GraphTokens graphTokens;

        if ("whitespace".equals(theTokens)) {
            getLog().debug("+ Using whitespace tree tokens");

            graphTokens = SerializingDependencyNodeVisitor.WHITESPACE_TOKENS;
        } else if ("extended".equals(theTokens)) {
            getLog().debug("+ Using extended tree tokens");

            graphTokens = SerializingDependencyNodeVisitor.EXTENDED_TOKENS;
        } else {
            graphTokens = SerializingDependencyNodeVisitor.STANDARD_TOKENS;
        }

        return graphTokens;
    }

}