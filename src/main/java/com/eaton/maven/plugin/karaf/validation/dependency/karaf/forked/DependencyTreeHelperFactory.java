/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package com.eaton.maven.plugin.karaf.validation.dependency.karaf.forked;

import org.apache.maven.execution.MavenSession;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.PlexusContainer;
import org.codehaus.plexus.component.repository.exception.ComponentLookupException;
import org.eclipse.aether.RepositorySystem;
import org.eclipse.aether.RepositorySystemSession;
import org.eclipse.aether.repository.RemoteRepository;

import java.util.List;

public class DependencyTreeHelperFactory {

    DependencyTreeHelper dependencyHelper;

    /**
     * Create a new {@link DependencyTreeHelper} based on what has been found in
     * {@link org.codehaus.plexus.PlexusContainer}.
     *
     * @param container    The Maven Plexus container to use.
     * @param mavenProject The Maven project to use.
     * @param mavenSession The Maven session.
     * @param cacheSize    Size of the artifact/file LRU cache
     * @param log          The log to use for the messages.
     *
     * @return The {@link DependencyTreeHelper} depending on the Maven version used.
     *
     * @throws MojoExecutionException If the plugin execution fails.
     */
    public static DependencyTreeHelper createDependencyHelper(
            PlexusContainer container, MavenProject mavenProject, MavenSession mavenSession, int cacheSize, Log log
    ) throws MojoExecutionException {
        try {
            final RepositorySystem system = container.lookup(RepositorySystem.class);
            final RepositorySystemSession session = mavenSession.getRepositorySession();
            final List<RemoteRepository> repositories = mavenProject.getRemoteProjectRepositories();
            return new DependencyTreeHelper(repositories, session, system, cacheSize);
        } catch (ComponentLookupException e) {
            throw new MojoExecutionException(e.getMessage(), e);
        }
    }

}
