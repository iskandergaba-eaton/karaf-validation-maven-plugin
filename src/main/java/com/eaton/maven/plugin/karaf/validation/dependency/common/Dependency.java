package com.eaton.maven.plugin.karaf.validation.dependency.common;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.StringJoiner;

public class Dependency {

	private final Identifier identifier;

	private final Map<String, Version> versions;

	public Dependency(Identifier identifier) {
		this.identifier = identifier;
		this.versions = new HashMap<>();
	}

	public Identifier getIdentifier() {
		return identifier;
	}

	public Collection<Version> getVersions() {
		return versions.values();
	}

	public boolean hasSingleVersion() {
		return versions.size() == 1;
	}
	
	public void add(Version version) {
		var identifier = version.getId();
		var value = versions.get(identifier);
		if (value == null) {
			value = new Version(version.id);
		}
		value.merge(version);
		versions.put(identifier, value);
	}

	public void merge(Dependency other) {
		for (Version version : other.versions.values()) {
			add(version);
		}
	}

	@Override
	public int hashCode() {
		return Objects.hash(identifier);
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
		return Objects.equals(identifier, other.identifier);
	}

	public String toString(String prefix) {
		StringJoiner versionsJoiner = new StringJoiner("");
		versions.values().forEach(v -> versionsJoiner.add(v.toString(prefix + "\t")));
		return prefix + identifier.toString("") + ":" + versionsJoiner.toString();
	}

}
