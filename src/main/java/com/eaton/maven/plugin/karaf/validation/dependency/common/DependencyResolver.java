package com.eaton.maven.plugin.karaf.validation.dependency.common;

import java.util.Map;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.project.MavenProject;
import org.apache.maven.shared.dependency.graph.DependencyGraphBuilderException;


public interface DependencyResolver {
	
	public Map<Identifier, Dependency> getDependencies(MavenProject project) throws MojoExecutionException, DependencyGraphBuilderException;
	
}
