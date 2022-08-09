package com.eaton.maven.plugin.karaf.validation.dependency.common;

import java.util.HashSet;
import java.util.Set;

public class Dependency {
	
	Identifier identifier;
	
	Set<Version> versions;

	public Dependency(Identifier dependencyIdentifier) {
		this.identifier = dependencyIdentifier;
		this.versions = new HashSet<>();
	}
	
	public void add(Identifier projectIdentifier, SourceClasspath sourceClasspath, String version, boolean managed) {
		this.versions.add(new Version(projectIdentifier, sourceClasspath, version, managed));
	}

	public Identifier getIdentifier() {
		return identifier;
	}
	
	public boolean hasMultipleVersions() {
		return versions.size() != 1;
	}

	@Override
	public String toString() {
		return identifier.toString() + versions.toString() + "\n";
	}

	//TODO: merge method

}
