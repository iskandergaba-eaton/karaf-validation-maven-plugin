package com.eaton.maven.plugin.karaf.validation.dependency;

import java.util.Map;

import com.eaton.maven.plugin.karaf.validation.dependency.conflict.Identifier;
import com.eaton.maven.plugin.karaf.validation.dependency.conflict.ProjectDependency;

public interface DependencyResolver {
	
	public Map<Identifier, ProjectDependency> getDependencies();
	
}
