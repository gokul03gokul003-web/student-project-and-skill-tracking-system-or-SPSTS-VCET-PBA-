package com.vcet.tracking.model;

/**
 * Model representing quantitative evaluation rubrics and grades.
 * Demonstrates Unit 1: Business Logic, Methods, Unit 4: Calculation Logic.
 */
public class Evaluation extends BaseEntity {
    private int evaluationId;
    private int projectId;
    private int milestoneId;
    private double literatureSurveyMarks; // Max 20
    private double systemDesignMarks;      // Max 20
    private double implementationMarks;    // Max 30
    private double testingMarks;           // Max 20
    private double presentationMarks;      // Max 10
    private double totalMarks;             // Max 100
    private String grade;                  // O, A+, A, B+, B, RA
    private String evaluatedDate;

    public Evaluation() {
        super();
    }

    public Evaluation(int evaluationId, int projectId, int milestoneId, 
                      double literatureSurveyMarks, double systemDesignMarks, 
                      double implementationMarks, double testingMarks, 
                      double presentationMarks, String evaluatedDate) {
        super();
        this.evaluationId = evaluationId;
        this.projectId = projectId;
        this.milestoneId = milestoneId;
        this.literatureSurveyMarks = literatureSurveyMarks;
        this.systemDesignMarks = systemDesignMarks;
        this.implementationMarks = implementationMarks;
        this.testingMarks = testingMarks;
        this.presentationMarks = presentationMarks;
        this.totalMarks = calculateTotal();
        this.grade = computeGrade(this.totalMarks);
        this.evaluatedDate = evaluatedDate;
    }

    public int getEvaluationId() {
        return evaluationId;
    }

    public void setEvaluationId(int evaluationId) {
        this.evaluationId = evaluationId;
    }

    public int getProjectId() {
        return projectId;
    }

    public void setProjectId(int projectId) {
        this.projectId = projectId;
    }

    public int getMilestoneId() {
        return milestoneId;
    }

    public void setMilestoneId(int milestoneId) {
        this.milestoneId = milestoneId;
    }

    public double getLiteratureSurveyMarks() {
        return literatureSurveyMarks;
    }

    public void setLiteratureSurveyMarks(double literatureSurveyMarks) {
        this.literatureSurveyMarks = literatureSurveyMarks;
        this.totalMarks = calculateTotal();
        this.grade = computeGrade(this.totalMarks);
    }

    public double getSystemDesignMarks() {
        return systemDesignMarks;
    }

    public void setSystemDesignMarks(double systemDesignMarks) {
        this.systemDesignMarks = systemDesignMarks;
        this.totalMarks = calculateTotal();
        this.grade = computeGrade(this.totalMarks);
    }

    public double getImplementationMarks() {
        return implementationMarks;
    }

    public void setImplementationMarks(double implementationMarks) {
        this.implementationMarks = implementationMarks;
        this.totalMarks = calculateTotal();
        this.grade = computeGrade(this.totalMarks);
    }

    public double getTestingMarks() {
        return testingMarks;
    }

    public void setTestingMarks(double testingMarks) {
        this.testingMarks = testingMarks;
        this.totalMarks = calculateTotal();
        this.grade = computeGrade(this.totalMarks);
    }

    public double getPresentationMarks() {
        return presentationMarks;
    }

    public void setPresentationMarks(double presentationMarks) {
        this.presentationMarks = presentationMarks;
        this.totalMarks = calculateTotal();
        this.grade = computeGrade(this.totalMarks);
    }

    public double getTotalMarks() {
        return totalMarks;
    }

    public void setTotalMarks(double totalMarks) {
        this.totalMarks = totalMarks;
        this.grade = computeGrade(totalMarks);
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getEvaluatedDate() {
        return evaluatedDate;
    }

    public void setEvaluatedDate(String evaluatedDate) {
        this.evaluatedDate = evaluatedDate;
    }

    public double calculateTotal() {
        return Math.round((literatureSurveyMarks + systemDesignMarks + implementationMarks + testingMarks + presentationMarks) * 100.0) / 100.0;
    }

    /**
     * Autonomous College Grading Rubric:
     * >= 90 : O (Outstanding)
     * >= 80 : A+ (Excellent)
     * >= 70 : A (Very Good)
     * >= 60 : B+ (Good)
     * >= 50 : B (Average)
     * < 50  : RA (Re-Appear)
     */
    public static String computeGrade(double total) {
        if (total >= 90.0) return "O";
        if (total >= 80.0) return "A+";
        if (total >= 70.0) return "A";
        if (total >= 60.0) return "B+";
        if (total >= 50.0) return "B";
        return "RA";
    }

    @Override
    public String getEntityIdentifier() {
        return "Evaluation-" + evaluationId + " (Proj #" + projectId + ")";
    }

    @Override
    public String toString() {
        return String.format("Eval #%d [Proj #%d]: Survey=%.1f, Design=%.1f, Impl=%.1f, Test=%.1f, Viva=%.1f -> Total=%.1f (Grade %s)", 
            evaluationId, projectId, literatureSurveyMarks, systemDesignMarks, implementationMarks, testingMarks, presentationMarks, totalMarks, grade);
    }
}
