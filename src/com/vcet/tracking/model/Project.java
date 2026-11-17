package com.vcet.tracking.model;

/**
 * Model representing a student mini-project registration.
 */
public class Project extends BaseEntity {
    private int projectId;
    private String registerNo;
    private String title;
    private String domain;
    private String description;
    private String technologiesUsed;
    private String startDate;
    private String completionStatus; // In Progress, Completed, Delayed, On Hold

    public Project() {
        super();
        this.completionStatus = "In Progress";
    }

    public Project(int projectId, String registerNo, String title, String domain, 
                   String description, String technologiesUsed, String startDate, String completionStatus) {
        super();
        this.projectId = projectId;
        this.registerNo = registerNo;
        this.title = title;
        this.domain = domain;
        this.description = description;
        this.technologiesUsed = technologiesUsed;
        this.startDate = startDate;
        this.completionStatus = (completionStatus != null) ? completionStatus : "In Progress";
    }

    public int getProjectId() {
        return projectId;
    }

    public void setProjectId(int projectId) {
        this.projectId = projectId;
    }

    public String getRegisterNo() {
        return registerNo;
    }

    public void setRegisterNo(String registerNo) {
        this.registerNo = registerNo;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTechnologiesUsed() {
        return technologiesUsed;
    }

    public void setTechnologiesUsed(String technologiesUsed) {
        this.technologiesUsed = technologiesUsed;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getCompletionStatus() {
        return completionStatus;
    }

    public void setCompletionStatus(String completionStatus) {
        this.completionStatus = completionStatus;
    }

    @Override
    public String getEntityIdentifier() {
        return "Project-" + projectId + ": " + title;
    }

    @Override
    public String toString() {
        return String.format("Project #%d | %s | Domain: %s | Status: %s | Tech: %s", 
            projectId, title, domain, completionStatus, technologiesUsed);
    }
}
