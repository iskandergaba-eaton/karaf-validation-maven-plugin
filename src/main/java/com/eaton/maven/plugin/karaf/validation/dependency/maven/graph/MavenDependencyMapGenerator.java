package com.eaton.maven.plugin.karaf.validation.dependency.maven.graph;


import com.eaton.maven.plugin.karaf.validation.dependency.common.Dependency;
import com.eaton.maven.plugin.karaf.validation.dependency.common.Identifier;
import com.eaton.maven.plugin.karaf.validation.dependency.common.Source;
import com.eaton.maven.plugin.karaf.validation.dependency.common.Version;

import org.apache.maven.shared.dependency.graph.DependencyNode;
import org.apache.maven.shared.dependency.graph.traversal.DependencyNodeVisitor;

import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.Map;

public class MavenDependencyMapGenerator implements DependencyNodeVisitor {

    private final Source source;
    private final Map<Identifier, Dependency> dependencyMap = new HashMap<>();
    private final Map<DependencyNode, Object> visitedNodes = new IdentityHashMap<>(512);

    public MavenDependencyMapGenerator(Identifier projectIdentifier) {
        this.source = new Source(Source.Classpath.MAVEN);
        this.source.add(projectIdentifier);
    }

    private Dependency getDependency(DependencyNode node) {
        //TODO: Check for managed versions
        Identifier identifier = new Identifier(node.getArtifact().getGroupId(), node.getArtifact().getArtifactId());
        Dependency dependency = new Dependency(identifier);
        dependency.add(new Version(node.getArtifact().getVersion(), source));
        return dependency;
    }

    private boolean setVisited(DependencyNode node) {
        return this.visitedNodes.put(node, Boolean.TRUE) == null;
    }

    @Override
    public boolean visit(DependencyNode node) {
        if (!this.setVisited(node)) {
            return false;
        } else if (node.getArtifact() != null) {
            Dependency dependency = getDependency(node);
            dependencyMap.put(dependency.getIdentifier(), dependency);
        }
        return true;
    }

    @Override
    public boolean endVisit(DependencyNode node) {
        return true;
    }

    public Map<Identifier, Dependency> getDependencyMap() {
        return dependencyMap;
    }
}
