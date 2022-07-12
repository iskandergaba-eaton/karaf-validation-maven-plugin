package com.eaton.maven.plugin.karaf.validation.osgi.export;

import com.eaton.maven.plugin.karaf.validation.report.Report;
import com.eaton.maven.plugin.karaf.validation.report.CriticalityLevel;
import com.eaton.maven.plugin.karaf.validation.report.Issue;
import com.eaton.maven.plugin.karaf.validation.util.MavenUtil;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.StringJoiner;
import org.apache.felix.utils.manifest.Clause;
import org.apache.maven.project.MavenProject;

/**
 *
 */
public class PackageExportAnalyzer {

        Report report;

        Map<String, Set<MavenProject>> projectsByClauseNames = new HashMap<>();

        public PackageExportAnalyzer(Report report) {
            this.report = report;
        }

        public void add(MavenProject project, Clause clause) {
            var name = clause.getName();

            projectsByClauseNames.compute(name, (actualKey, actualValue) -> {
                var projects = actualValue == null ? new HashSet<MavenProject>(): actualValue;
                projects.add(project);
                return projects;
            });
        }

        public void validate() {
            for (var projectsByClauseName: projectsByClauseNames.entrySet()) {
                if (projectsByClauseName.getValue().size() > 1){
                    var projectJoiner = new StringJoiner(",\n\t", "[\n\t", "\n]");
                    for (var project :projectsByClauseName.getValue()) {
                        projectJoiner.add(MavenUtil.getModuleIdentifier(project));
                    }
                    report.add(new Issue(CriticalityLevel.ERROR,
                            String.format("'%s' package is exported by two or more modules: %s",
                                    projectsByClauseName.getKey(),
                                    projectJoiner.toString())));
                    report.setSuccess(false);
                }
            }
        }
    }