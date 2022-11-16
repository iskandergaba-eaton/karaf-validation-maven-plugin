package com.eaton.maven.plugin.karaf.validation.dependency.karaf.graph;


import com.eaton.maven.plugin.karaf.validation.dependency.common.Dependency;
import com.eaton.maven.plugin.karaf.validation.dependency.common.Identifier;
import com.eaton.maven.plugin.karaf.validation.dependency.common.Source;
import com.eaton.maven.plugin.karaf.validation.dependency.common.Version;

import org.eclipse.aether.graph.DependencyNode;
import org.eclipse.aether.graph.DependencyVisitor;

import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.Map;

public class KarafDependencyMapGenerator implements DependencyVisitor {

    private final Source source;
    private final Map<Identifier, Dependency> dependencyMap = new HashMap<>();
    private final Map<DependencyNode, Object> visitedNodes = new IdentityHashMap<>(512);

    public KarafDependencyMapGenerator(Identifier projectIdentifier) {
    	source = new Source(Source.Classpath.KARAF);
    	source.add(projectIdentifier);
    }

    private Dependency getDependency(DependencyNode node) {
        Identifier identifier = new Identifier(node.getArtifact().getGroupId(), node.getArtifact().getArtifactId());
        Dependency dependency = new Dependency(identifier);
        dependency.add(new Version(node.getArtifact().getVersion(), source));
        return dependency;
    }

    private boolean setVisited(DependencyNode node) {
        return this.visitedNodes.put(node, Boolean.TRUE) == null;
    }

    @Override
    public boolean visitEnter(DependencyNode node) {
        if (!this.setVisited(node)) {
            return false;
        } else if (node.getDependency() != null) {
            Dependency dependency = getDependency(node);
            dependencyMap.put(dependency.getIdentifier(), dependency);
        }
        return true;
    }

    @Override
    public boolean visitLeave(DependencyNode node) {
        return true;
    }

    public Map<Identifier, Dependency> getDependencyMap() {
        return dependencyMap;
    }
}
