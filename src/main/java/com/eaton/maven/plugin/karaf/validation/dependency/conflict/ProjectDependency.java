package com.eaton.maven.plugin.karaf.validation.dependency.conflict;

import java.util.Set;
import java.util.TreeSet;

public class ProjectDependency {
	
	Identifier identifier;
	
	Set<Version> versions;

	public ProjectDependency(Identifier dependencyIdentifier) {
		this.identifier = dependencyIdentifier;
		this.versions = new TreeSet<Version>();
	}
	
	public void add(Identifier projectIdentifier, String version, boolean managed) {
		this.versions.add(new Version(projectIdentifier, version, managed));
	}
	
	public boolean hasMultipleVersions() {
		return versions.size() != 1;
	}

}
