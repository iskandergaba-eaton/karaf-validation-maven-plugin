package com.eaton.maven.plugin.karaf.validation.dependency;

import java.util.Objects;
import java.util.Set;

public class Module {
	
	
	final Identifier identifier;
	
	final Set<Module> dependencies;

	public Module(Identifier identifier, Set<Module> dependencies) {
		super();
		this.identifier = identifier;
		this.dependencies = dependencies;
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
		Module other = (Module) obj;
		return Objects.equals(identifier, other.identifier);
	}


	public Identifier getIdentifier() {
		return identifier;
	}

	public Set<Module> getDependencies() {
		return dependencies;
	}

}
