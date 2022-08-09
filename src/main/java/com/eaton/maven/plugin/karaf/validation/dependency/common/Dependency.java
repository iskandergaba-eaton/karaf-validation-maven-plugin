package com.eaton.maven.plugin.karaf.validation.dependency.common;

import java.util.HashSet;
import java.util.Set;

public class Dependency {
	
	private final Identifier identifier;
	
	private final Set<Version> versions;

	public Dependency(Identifier dependencyIdentifier) {
		this.identifier = dependencyIdentifier;
		this.versions = new HashSet<>();
	}

	public Identifier getIdentifier() {
		return identifier;
	}

	public Set<Version> getVersions() {
		return versions;
	}

	public boolean hasSingleVersion() {
		return versions.size() == 1;
	}

	public void add(Identifier projectIdentifier, SourceClasspath sourceClasspath, String version, boolean managed) {
		this.versions.add(new Version(projectIdentifier, sourceClasspath, version, managed));
	}

	public void add(Version version) {
		this.versions.add(version);
	}

	public Dependency merge(Dependency dependency) {
		if (isValidMerge(dependency)) {
			Dependency result = new Dependency(this.identifier);
			Version v0 = this.versions.iterator().next();
			Version v1 = dependency.getVersions().iterator().next();
			if (v0.equals(v1)) {
				Version version = new Version(v0.getProject(), v0.getSourceClasspaths(), v0.getVersion(), v0.isManaged());
				version.addSourceClasspath(v1.getSourceClasspaths().iterator().next());
				result.add(version);
			} else {
				result.add(v0);
				result.add(v1);
			}
			return result;
		}
		return null;
	}

	private boolean isValidMerge(Dependency dependency) {
		return dependency != null
				&& this.identifier.equals(dependency.getIdentifier())
				&& hasSingleVersion()
				&& dependency.hasSingleVersion()
				&& this.versions.iterator().next().hasSingleSourceClasspath()
				&& dependency.getVersions().iterator().next().hasSingleSourceClasspath()
				&& this.versions.iterator().next().getSourceClasspaths() != dependency.getVersions().iterator().next().getSourceClasspaths();
	}

	@Override
	public String toString() {
		return identifier.toString() + versions;
	}

	//TODO: merge method

}
