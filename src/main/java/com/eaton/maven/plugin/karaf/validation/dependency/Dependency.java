package com.eaton.maven.plugin.karaf.validation.dependency;

import java.util.Objects;

public class Dependency {
	
	String groupId;
	
	String artifactId;
	
	String version;

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
		Dependency other = (Dependency) obj;
		return Objects.equals(artifactId, other.artifactId) && Objects.equals(groupId, other.groupId)
				&& Objects.equals(version, other.version);
	}
	
}
