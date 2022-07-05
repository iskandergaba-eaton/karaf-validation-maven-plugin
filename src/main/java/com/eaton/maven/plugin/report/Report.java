package com.eaton.maven.plugin.report;

import java.util.EnumMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;

/**
 * The report object is used to generate a report with all reported {@link Issue},
 * based on their {@link CriticalityLevel}.
 *
 * Report looks like:
 * ${mainMessage}
 * ${criticalMessage}
 * ${criticalIssues}
 * ${warningMessage}
 * ${warningIssues}
 * ${informationMessage}
 * ${informationIssues}
 * Validation succeed/failed. (based on error messages)
 */
public class Report {

    Boolean success = true;

    String mainMessage;

    Map<CriticalityLevel, String> messages;

    Map<CriticalityLevel, List<Issue>> issuesByCriticality;

    public Report(String mainMessage, String criticalMessage, String warningMessage, String informationMessage) {
        this.mainMessage = mainMessage;

        messages = new EnumMap<>(CriticalityLevel.class);
        messages.put(CriticalityLevel.ERROR, criticalMessage);
        messages.put(CriticalityLevel.WARN, warningMessage);
        messages.put(CriticalityLevel.INFO, informationMessage);

        issuesByCriticality = new EnumMap<>(CriticalityLevel.class);
        for (var criticalityLevel: CriticalityLevel.values()) {
            issuesByCriticality.put(criticalityLevel, new LinkedList<>());
        }
    }

    public void add(Issue issue){
        issuesByCriticality.get(issue.getCriticalityLevel()).add(issue);
    }

    public String toString() {
        var reportJoiner = new StringJoiner("\n\t>", mainMessage+"\n", String.format("\n----> Validation %s.", success ? "succeed" : "failed"));
        for (var criticalityLevel: CriticalityLevel.values()) {
            var issues = issuesByCriticality.get(criticalityLevel);
            if(!issues.isEmpty()) {
                var issuesJoiner = new StringJoiner("\n\t\t-");
                for (var issue: issues) {
                    issuesJoiner.add(issue.toString().replaceAll("\n", "\n\t\t"));
                }
                reportJoiner.add(issuesJoiner.toString());
            }
        }
        return reportJoiner.toString();
    }

    public Boolean getSuccess(){
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }
}
