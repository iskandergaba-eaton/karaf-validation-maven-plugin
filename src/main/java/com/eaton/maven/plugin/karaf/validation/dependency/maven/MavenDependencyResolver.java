package com.eaton.maven.plugin.karaf.validation.dependency.maven;

import java.util.Map;

import org.apache.maven.execution.MavenSession;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.project.DefaultProjectBuildingRequest;
import org.apache.maven.project.MavenProject;
import org.apache.maven.project.ProjectBuildingRequest;
import org.apache.maven.shared.dependency.graph.DependencyGraphBuilder;
import org.apache.maven.shared.dependency.graph.DependencyGraphBuilderException;
import org.apache.maven.shared.dependency.graph.DependencyNode;

import com.eaton.maven.plugin.karaf.validation.dependency.DependencyResolver;
import com.eaton.maven.plugin.karaf.validation.dependency.Identifier;
import com.eaton.maven.plugin.karaf.validation.dependency.Dependency;

public class MavenDependencyResolver implements DependencyResolver {

	private MavenSession mavenSession;

	private DependencyGraphBuilder dependencyGraphBuilder;
	
	private Log log;

	public MavenDependencyResolver(MavenSession mavenSession, DependencyGraphBuilder dependencyGraphBuilder, Log log) {
			this.mavenSession = mavenSession;
			this.dependencyGraphBuilder = dependencyGraphBuilder;
			this.log = log;
		}

	private DependencyNode fetchDependencies(MavenProject project) throws MojoExecutionException, DependencyGraphBuilderException {
    	ProjectBuildingRequest buildingRequest = new DefaultProjectBuildingRequest(mavenSession.getProjectBuildingRequest());
    	buildingRequest.setProject(project);
    	return dependencyGraphBuilder.buildDependencyGraph(buildingRequest, null);
	}

	@Override
	public Map<Identifier, Dependency> getDependencies(MavenProject project) {
		// TODO
		return null;
	}
	
}
