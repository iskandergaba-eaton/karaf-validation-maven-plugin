package com.eaton.maven.plugin.karaf.validation.dependency.common;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.StringJoiner;

public class Source {

	public static enum Classpath {
		MAVEN, MAVEN_MANAGED, KARAF;
	}

	private final Classpath classpath;

	private final Set<Identifier> projects;

	public Source(Classpath classpath) {
		this.classpath = classpath;
		this.projects = new HashSet<>();
	}

	public void add(Identifier project) {
		projects.add(project);
	}

	public void merge(Source other) {
		projects.addAll(other.projects);
	}

	public Set<Identifier> getProject() {
		return projects;
	}
	
	public Classpath getClasspath() {
		return classpath;
	}

	@Override
	public int hashCode() {
		return Objects.hash(classpath);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Source other = (Source) obj;
		return classpath == other.classpath;
	}

	public String toString(String prefix) {
		StringJoiner projectJoiner = new StringJoiner("");
		projects.forEach(p -> projectJoiner.add(p.toString(prefix + "\t")));
		return prefix + classpath + ":" + projectJoiner.toString();
	}

}
