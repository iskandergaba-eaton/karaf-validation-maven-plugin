package com.eaton.maven.plugin.karaf.validation.dependency.common;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.StringJoiner;

import com.eaton.maven.plugin.karaf.validation.dependency.common.Source.Classpath;

public class Version {

	String id;

	Map<Classpath, Source> sources;

	public Version(String id, Source source) {
		this.sources = new EnumMap<Source.Classpath, Source>(Classpath.class);
		this.sources.put(source.getClasspath(), source);
		this.id = id;
	}

	public Version(String id) {
		this.sources = new HashMap<>();
		this.id = id;
	}

	public String getId() {
		return id;
	}
	
	public boolean hasSingleSourceClasspath() {
		return sources.size() == 1;
	}

	public void add(Source source) {
		var identifier = source.getClasspath();
		var value = sources.get(identifier);
		if (value == null) {
			value = new Source(identifier);
		}
		value.merge(source);
		sources.put(identifier, value);
	}

	public void merge(Version other) {
		for (Source source : other.sources.values()) {
			add(source);
		}
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
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
		return Objects.equals(id, other.id);
	}

	public String toString(String prefix) {
		StringJoiner sourcesJoiner = new StringJoiner("");
		sources.values().forEach(s -> sourcesJoiner.add(s.toString(prefix + "\t")));
		return prefix + id + ":" + sourcesJoiner.toString();
	}

}