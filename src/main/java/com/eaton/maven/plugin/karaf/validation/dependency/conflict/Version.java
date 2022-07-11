package com.eaton.maven.plugin.karaf.validation.dependency.conflict;

import java.util.Objects;

public class Version {
	
	Identifier sourceProject;
	
	String version;
	
	boolean managed;

	public Version(Identifier sourceProject, String version, boolean managed) {
		super();
		this.sourceProject = sourceProject;
		this.version = version;
		this.managed = managed;
	}

	@Override
	public int hashCode() {
		return Objects.hash(managed, version);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Version other = (Version) obj;
		return managed == other.managed && Objects.equals(version, other.version);
	}

	public Identifier getProject() {
		return sourceProject;
	}

	public String getVersion() {
		return version;
	}

	public boolean isManaged() {
		return managed;
	}

}