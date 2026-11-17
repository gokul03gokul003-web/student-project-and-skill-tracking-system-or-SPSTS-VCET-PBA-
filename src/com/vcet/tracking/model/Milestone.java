package com.vcet.tracking.model;

/**
 * Model representing a project stage/milestone.
 */
public class Milestone extends BaseEntity {
    private int milestoneId;
    private int projectId;
    private String milestoneName;
    private String dueDate;
    private String submissionDate;
    private String status; // Pending, In Progress, Submitted, Approved, Delayed
    private String remarks;

    public Milestone() {
        super();
        this.status = "Pending";
    }

    public Milestone(int milestoneId, int projectId, String milestoneName, 
                     String dueDate, String submissionDate, String status, String remarks) {
        super();
        this.milestoneId = milestoneId;
        this.projectId = projectId;
        this.milestoneName = milestoneName;
        this.dueDate = dueDate;
        this.submissionDate = submissionDate;
        this.status = (status != null) ? status : "Pending";
        this.remarks = remarks;
    }

    public int getMilestoneId() {
        return milestoneId;
    }

    public void setMilestoneId(int milestoneId) {
        this.milestoneId = milestoneId;
    }

    public int getProjectId() {
        return projectId;
    }

    public void setProjectId(int projectId) {
        this.projectId = projectId;
    }

    public String getMilestoneName() {
        return milestoneName;
    }

    public void setMilestoneName(String milestoneName) {
        this.milestoneName = milestoneName;
    }

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public String getSubmissionDate() {
        return submissionDate;
    }

    public void setSubmissionDate(String submissionDate) {
        this.submissionDate = submissionDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public boolean isDelayed() {
        return "Delayed".equalsIgnoreCase(this.status);
    }

    @Override
    public String getEntityIdentifier() {
        return "Milestone-" + milestoneId + " (" + milestoneName + ")";
    }

    @Override
    public String toString() {
        return String.format("Milestone #%d [Proj #%d]: %s | Due: %s | Status: %s", 
            milestoneId, projectId, milestoneName, dueDate, status);
    }
}
