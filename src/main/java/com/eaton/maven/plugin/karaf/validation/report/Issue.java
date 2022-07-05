package com.eaton.maven.plugin.karaf.validation.report;

/**
 * Simple object representing an issue encountered in the analise.
 */
public class Issue {

    protected Exception exception = null;
    protected String reason;
    protected CriticalityLevel criticalityLevel;

    public Issue(CriticalityLevel criticalityLevel, String reason) {
        this.reason = reason;
        this.criticalityLevel = criticalityLevel;
    }

    public Issue(CriticalityLevel criticalityLevel, Exception exception) {
        this.exception = exception;
        this.reason = null;
        this.criticalityLevel = criticalityLevel;
    }

    @Override
    public String toString() {
        String result;
        if(exception == null || exception.getMessage() == null){
            result = reason;
        } else {
            result = String.format("%s (%s)", reason, exception.getMessage());
        }
        return result;
    }

    public CriticalityLevel getCriticalityLevel() {
        return criticalityLevel;
    }
}
