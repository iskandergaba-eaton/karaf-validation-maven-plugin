package com.eaton.maven.plugin.karaf.validation.dependency.karaf;

import java.util.Map;

import org.apache.maven.execution.MavenSession;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.PlexusContainer;
import org.eclipse.aether.graph.DependencyNode;

import com.eaton.maven.plugin.karaf.validation.dependency.Dependency;
import com.eaton.maven.plugin.karaf.validation.dependency.DependencyResolver;
import com.eaton.maven.plugin.karaf.validation.dependency.Identifier;
import com.eaton.maven.plugin.karaf.validation.dependency.karaf.forked.DependencyTreeHelperFactory;

public class KarafDependencyResolver implements DependencyResolver {

	private MavenSession mavenSession;

	private PlexusContainer container;

	private Log log;

	private int artifactCacheSize = 1024;

	public KarafDependencyResolver(MavenSession mavenSession, PlexusContainer container, Log log,
			int artifactCacheSize) {
		super();
		this.mavenSession = mavenSession;
		this.container = container;
		this.log = log;
		this.artifactCacheSize = artifactCacheSize;
	}

	private DependencyNode fetchDependencies(MavenProject project) throws MojoExecutionException {
		return DependencyTreeHelperFactory
				.createDependencyHelper(this.container, project, mavenSession, artifactCacheSize, log)
				.getDependencyTree(project);
	}

	@Override
	public Map<Identifier, Dependency> getDependencies(MavenProject project) {
		// TODO
		return null;
	}

}
