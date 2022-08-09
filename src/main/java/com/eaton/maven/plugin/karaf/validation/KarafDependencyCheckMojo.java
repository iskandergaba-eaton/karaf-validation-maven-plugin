package com.eaton.maven.plugin.karaf.validation;

import com.eaton.maven.plugin.karaf.validation.dependency.common.Dependency;
import com.eaton.maven.plugin.karaf.validation.dependency.common.Identifier;
import org.apache.maven.execution.MavenSession;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Component;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;
import org.apache.maven.shared.dependency.graph.DependencyGraphBuilder;
import org.apache.maven.shared.dependency.graph.DependencyGraphBuilderException;
import org.apache.maven.shared.dependency.graph.DependencyNode;
import org.codehaus.plexus.PlexusContainer;

import com.eaton.maven.plugin.karaf.validation.dependency.karaf.KarafDependencyResolver;
import com.eaton.maven.plugin.karaf.validation.dependency.maven.MavenDependencyResolver;

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

    public Map<Identifier, Dependency> mergeDependencyMaps(Map<Identifier, Dependency> mavenMap, Map<Identifier, Dependency> karafMap) {
        Map<Identifier, Dependency> combinedMap = new HashMap<>();
        for (Identifier id : mavenMap.keySet()) {
            if (karafMap.containsKey(id)) {
                combinedMap.put(id, mavenMap.get(id).merge(karafMap.get(id)));
            } else {
                combinedMap.put(id, mavenMap.get(id));
            }
        }

        for (Identifier id : karafMap.keySet()) {
            if (!combinedMap.containsKey(id)) {
                combinedMap.put(id, karafMap.get(id));
            }
        }
        return combinedMap;
    }


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
                try {
                    Map<Identifier, Dependency> mavenMap = mavenDependencyResolver.getDependencies(project);
                    Map<Identifier, Dependency> karafMap = karafDependencyResolver.getDependencies(project);
                    Map<Identifier, Dependency> combinedMap = mergeDependencyMaps(mavenMap, karafMap);

                    log.info("START: List of dependencies with multiple versions.");
                    for (Dependency d : combinedMap.values()) {
                        if (!d.hasSingleVersion()) {
                            log.info(d.toString());
                        }
                    }
                    log.info("END: List of dependencies with multiple versions.");

                } catch (DependencyGraphBuilderException e) {
                    throw new RuntimeException(e);
                }
            }
    }

}