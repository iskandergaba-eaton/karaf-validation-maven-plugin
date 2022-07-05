package com.eaton.maven.plugin.util;

import org.apache.maven.project.MavenProject;

/**
 * Utility class providing helper related to Maven.
 */
public final class MavenUtil {

    private MavenUtil() {
        // NOOP static class
    }

    /**
     * Simple helper used to generate a unique string per MavenProject.
     * @param project {@link MavenProject}
     * @return The concatenation of groupId, artifactId and version separated by ':'.
     */
    public static String getModuleIdentifier(MavenProject project) {
        return String.format("%s:%s:%s", project.getGroupId(), project.getArtifactId(), project.getVersion());
    }
}
