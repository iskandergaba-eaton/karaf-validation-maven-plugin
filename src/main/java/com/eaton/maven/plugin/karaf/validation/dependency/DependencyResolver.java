package com.eaton.maven.plugin.karaf.validation.dependency;

import java.util.Map;

import org.apache.maven.project.MavenProject;


public interface DependencyResolver {
	
	public Map<Identifier, Dependency> getDependencies(MavenProject project);
	
}
