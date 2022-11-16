package com.eaton.maven.plugin.karaf.validation.dependency.common;

import java.util.Objects;

public class Identifier {
	
	final String groupId;
	
	final String artifactId;

	public Identifier(String groupId, String artifactId) {
		super();
		this.groupId = groupId;
		this.artifactId = artifactId;
	}

	public String getGroupId() {
		return groupId;
	}

	public String getArtifactId() {
		return artifactId;
	}

	@Override
	public int hashCode() {
		return Objects.hash(artifactId, groupId);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Identifier other = (Identifier) obj;
		return Objects.equals(artifactId, other.artifactId) && Objects.equals(groupId, other.groupId);
	}

	public String toString(String prefix) {
		return prefix + groupId + ":" + artifactId;
	}
}
