package com.eaton.maven.plugin.karaf.validation.dependency;

import java.util.Objects;

public class Identifier {
	
	final String groupId;
	
	final String artifactId;
	
	final String version;

	public Identifier(String groupId, String artifactId, String version) {
		super();
		this.groupId = groupId;
		this.artifactId = artifactId;
		this.version = version;
	}
	
	public String getGroupId() {
		return groupId;
	}

	public String getArtifactId() {
		return artifactId;
	}

	public String getVersion() {
		return version;
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(artifactId, groupId, version);
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
		return Objects.equals(artifactId, other.artifactId) && Objects.equals(groupId, other.groupId)
				&& Objects.equals(version, other.version);
	}
	
}
