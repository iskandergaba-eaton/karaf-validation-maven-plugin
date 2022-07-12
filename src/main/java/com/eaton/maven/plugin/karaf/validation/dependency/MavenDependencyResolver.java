package com.eaton.maven.plugin.karaf.validation.dependency;

import org.apache.maven.execution.MavenSession;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.project.DefaultProjectBuildingRequest;
import org.apache.maven.project.MavenProject;
import org.apache.maven.project.ProjectBuildingRequest;
import org.apache.maven.shared.dependency.graph.DependencyGraphBuilder;
import org.apache.maven.shared.dependency.graph.DependencyGraphBuilderException;
import org.apache.maven.shared.dependency.graph.DependencyNode;

public class MavenDependencyResolver {

	private MavenSession mavenSession;

	private DependencyGraphBuilder dependencyGraphBuilder;
	
	private Log log;

	public MavenDependencyResolver(MavenSession mavenSession, DependencyGraphBuilder dependencyGraphBuilder, Log log) {
			this.mavenSession = mavenSession;
			this.dependencyGraphBuilder = dependencyGraphBuilder;
			this.log = log;
		}

	public DependencyNode getDependencies(MavenProject project) throws MojoExecutionException, DependencyGraphBuilderException {
    	ProjectBuildingRequest buildingRequest = new DefaultProjectBuildingRequest(mavenSession.getProjectBuildingRequest());
    	buildingRequest.setProject(project);
    	return dependencyGraphBuilder.buildDependencyGraph(buildingRequest, null);
	}

}
