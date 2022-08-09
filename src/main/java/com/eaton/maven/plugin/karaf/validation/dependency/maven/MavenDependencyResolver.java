package com.eaton.maven.plugin.karaf.validation.dependency.maven;

import java.util.Map;

import com.eaton.maven.plugin.karaf.validation.dependency.maven.graph.MavenDependencyMapGenerator;
import org.apache.maven.execution.MavenSession;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.project.DefaultProjectBuildingRequest;
import org.apache.maven.project.MavenProject;
import org.apache.maven.project.ProjectBuildingRequest;
import org.apache.maven.shared.dependency.graph.DependencyGraphBuilder;
import org.apache.maven.shared.dependency.graph.DependencyGraphBuilderException;
import org.apache.maven.shared.dependency.graph.DependencyNode;

import com.eaton.maven.plugin.karaf.validation.dependency.common.DependencyResolver;
import com.eaton.maven.plugin.karaf.validation.dependency.common.Identifier;
import com.eaton.maven.plugin.karaf.validation.dependency.common.Dependency;

public class MavenDependencyResolver implements DependencyResolver {

	private final MavenSession mavenSession;

	private final DependencyGraphBuilder dependencyGraphBuilder;
	
	private final Log log;

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
	public Map<Identifier, Dependency> getDependencies(MavenProject project) throws MojoExecutionException, DependencyGraphBuilderException {
		DependencyNode root = fetchDependencies(project);
		Identifier identifier = new Identifier(project.getGroupId(), project.getArtifactId());
		MavenDependencyMapGenerator generator = new MavenDependencyMapGenerator(identifier);
		root.accept(generator);
		return generator.getDependencyMap();
	}
}
