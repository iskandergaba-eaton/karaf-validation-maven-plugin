package com.eaton.maven.plugin.karaf.validation;

import com.eaton.maven.plugin.karaf.validation.dependency.common.Dependency;
import com.eaton.maven.plugin.karaf.validation.dependency.common.Identifier;
import com.eaton.maven.plugin.karaf.validation.dependency.karaf.KarafDependencyResolver;
import com.eaton.maven.plugin.karaf.validation.dependency.maven.MavenDependencyResolver;
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
import org.codehaus.plexus.PlexusContainer;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Analyze the whole project and output incoherent dependencies with some fix
 * tips
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

	public void mergeDependencyMaps(Map<Identifier, Dependency> globalMap, Map<Identifier, Dependency> mavenMap,
			Map<Identifier, Dependency> karafMap) {

		Set<Identifier> keys = new HashSet<Identifier>();
		keys.addAll(globalMap.keySet());
		keys.addAll(mavenMap.keySet());
		keys.addAll(karafMap.keySet());

		for (Identifier identifier : keys) {
			Dependency maven = mavenMap.get(identifier);
			Dependency karaf = karafMap.get(identifier);

			if (maven != null || karaf != null) {
				globalMap.compute(identifier, (key, value) -> {
					Dependency global = value == null ? new Dependency(identifier) : value;

					if (maven != null) {
						global.merge(maven);
					}

					if (karaf != null) {
						global.merge(karaf);
					}

					return global;
				});
			}
		}
	}

	// Mojo methods -----------------------------------------------------------

	/*
	 * @see org.apache.maven.plugin.Mojo#execute()
	 */
	@Override
	public void execute() throws MojoExecutionException, MojoFailureException {

		var log = getLog();

		KarafDependencyResolver karafDependencyResolver = new KarafDependencyResolver(session, container, log,
				artifactCacheSize);
		MavenDependencyResolver mavenDependencyResolver = new MavenDependencyResolver(session, dependencyGraphBuilder,
				log);

		// Building all dependencies trees
		Map<Identifier, Dependency> globalMap = new HashMap<>();

		for (MavenProject project : reactorProjects) {
			try {
				Map<Identifier, Dependency> mavenMap = mavenDependencyResolver.getDependencies(project);
				Map<Identifier, Dependency> karafMap = karafDependencyResolver.getDependencies(project);
				mergeDependencyMaps(globalMap, mavenMap, karafMap);
			} catch (DependencyGraphBuilderException e) {
				throw new RuntimeException(e);
			}
		}

		log.info("START: List of dependencies with multiple versions.");
		for (Dependency dependency : globalMap.values()) {
			if (!dependency.hasSingleVersion()) {
				log.info(dependency.toString("\n\t"));
			}
		}
		log.info("END: List of dependencies with multiple versions.");
	}

}