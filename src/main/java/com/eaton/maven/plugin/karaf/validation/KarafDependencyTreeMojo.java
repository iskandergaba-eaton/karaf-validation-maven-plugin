package com.eaton.maven.plugin.karaf.validation;

import com.eaton.maven.plugin.karaf.validation.dependency.DependencyTreeHelper;
import com.eaton.maven.plugin.karaf.validation.dependency.DependencyTreeHelperFactory;
import org.apache.karaf.tooling.utils.MojoSupport;
import org.apache.maven.plugin.MojoExecutionException;

import org.apache.maven.plugins.annotations.Component;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;
import org.apache.maven.project.ProjectBuilder;
import org.apache.maven.repository.RepositorySystem;
import org.apache.maven.shared.filtering.MavenFileFilter;
import org.apache.maven.shared.filtering.MavenResourcesFiltering;
import org.codehaus.plexus.PlexusContainer;
import org.eclipse.aether.graph.DependencyNode;

import java.util.List;


@Mojo(name = "generate-dependencies-tree", defaultPhase = LifecyclePhase.VERIFY, aggregator = true, threadSafe = true)
class KarafDependencyTreeMojo extends MojoSupport {

    /**
     * Maximum size of the artifact LRU cache. This cache is used to prevent repeated artifact-to-file resolution.
     */
    @Parameter(defaultValue = "1024")
    private int artifactCacheSize;

    /**
     * Flag indicating whether transitive dependencies should be included (<code>true</code>) or not (<code>false</code>).
     * <p/>
     * N.B. Note the default value of this is true, but is suboptimal in cases where specific <code>&lt;feature/&gt;</code> dependencies are
     * provided by the <code>inputFile</code> parameter.
     */
    @Parameter(defaultValue = "true")
    private boolean includeTransitiveDependency;

    /**
     * Contains the full list of projects in the reactor.
     */
    @Parameter(defaultValue = "${reactorProjects}", readonly = true, required = true)
    private List<MavenProject> reactorProjects;

    @Component
    private PlexusContainer container;

    @Component
    private RepositorySystem repoSystem;

    @Component
    protected MavenResourcesFiltering mavenResourcesFiltering;

    @Component
    protected MavenFileFilter mavenFileFilter;

    @Component
    private ProjectBuilder mavenProjectBuilder;

    protected DependencyTreeHelper dependencyHelper;

    @Override
    public void execute() throws MojoExecutionException {
        for (MavenProject project : reactorProjects) {
            this.dependencyHelper = DependencyTreeHelperFactory.createDependencyHelper(this.container, project,
                    this.mavenSession, this.artifactCacheSize, getLog());
            DependencyNode tree = this.dependencyHelper.getDependencyTree(project);

            /*
            System.out.println(tree.getArtifact());
            for (DependencyNode node : tree.getChildren()) {
                System.out.println(node.getArtifact());
            }
            System.out.println();
            */

            print(tree);
            System.out.println();

            //this.dependencyHelper.getDependencies(project, includeTransitiveDependency);
            //System.out.println(this.dependencyHelper.getTreeListing());
        }
    }

    private void print(DependencyNode tree) {
        print(tree, "");
    }

    private void print(DependencyNode tree, String spacing) {
        if (tree != null) {
            System.out.println(spacing + tree.getArtifact());
            spacing += "\t";
            for (DependencyNode node : tree.getChildren()) {
                print(node, spacing);
            }
        }
    }

}
