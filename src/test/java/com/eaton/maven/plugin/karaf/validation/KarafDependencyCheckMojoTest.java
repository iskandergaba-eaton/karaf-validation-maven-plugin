package com.eaton.maven.plugin.karaf.validation;

import java.io.PrintStream;
import java.util.List;
import org.apache.maven.execution.MavenSession;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.project.MavenProject;
import org.apache.maven.shared.dependency.graph.DependencyCollectorBuilder;
import org.apache.maven.shared.dependency.graph.DependencyGraphBuilder;
import org.junit.jupiter.api.Test;

/**
 * Test the KarafDependencyCheckMojo class out of the Maven context.
 */
public class KarafDependencyCheckMojoTest {

    @Test
    public void happyPath() throws MojoExecutionException, MojoFailureException {
        //PrintStream printStreamMock = Mockito.mock(PrintStream.class);
        //System.setOut(printStreamMock);

        //KarafDependencyCheckMojo mojo = new KarafDependencyCheckMojo();
        //mojo.execute();
    }

}