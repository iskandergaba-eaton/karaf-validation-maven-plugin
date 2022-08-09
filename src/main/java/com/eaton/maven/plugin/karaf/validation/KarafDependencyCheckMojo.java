package com.eaton.maven.plugin.karaf.validation;

import org.apache.maven.execution.MavenSession;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Component;
import org.apache.maven.plugins.annotations.LifecyclePhase;
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
import org.codehaus.plexus.PlexusContainer;

import com.eaton.maven.plugin.karaf.validation.dependency.karaf.KarafDependencyResolver;
import com.eaton.maven.plugin.karaf.validation.dependency.maven.MavenDependencyResolver;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Analyze the whole project and output incoherent dependencies with some fix tips
 */
@Mojo(name = "check-dependencies", aggregator = true, defaultPhase = LifecyclePhase.VERIFY)
public class KarafDependencyCheckMojo extends AbstractMojo {
    // fields -----------------------------------------------------------------
	
    @Parameter(defaultValue = "1024")
    private int artifactCacheSize;

    @Parameter(defaultValue = "${session}", readonly = true, required = true)
    private MavenSession session;
    
    @Component
    private PlexusContainer container;

    /**
     * Contains the full list of projects in the reactor.
     */
    @Parameter(defaultValue = "${reactorProjects}", readonly = true, required = true)
    private List<MavenProject> reactorProjects;

    /**
     * The dependency graph builder to use.
     */
    @Component(hint = "default")
    private DependencyGraphBuilder dependencyGraphBuilder;


    // Mojo methods -----------------------------------------------------------

    /*
     * @see org.apache.maven.plugin.Mojo#execute()
     */
    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
    	
    	var log = getLog();
    	
    	KarafDependencyResolver karafDependencyResolver = new KarafDependencyResolver(session, container, log, artifactCacheSize);
    	MavenDependencyResolver mavenDependencyResolver = new MavenDependencyResolver(session, dependencyGraphBuilder, log);
    	
            // Building all dependencies trees
            Map<MavenProject, DependencyNode> resolvedDependencies = new HashMap<>();

            for (MavenProject project : reactorProjects) {
            	mavenDependencyResolver.getDependencies(project);
            	karafDependencyResolver.getDependencies(project);
            	
            }
    }

}