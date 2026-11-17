package com.vcet.tracking.model;

/**
 * Model representing a technical skill or programming language.
 * Demonstrates Unit 1: OOP, Encapsulation, Constructors, Getters/Setters.
 */
public class Skill extends BaseEntity {
    private int skillId;
    private String skillName;
    private String category;
    private String proficiencyLevel; // Beginner, Intermediate, Advanced
    private int experienceYears;
    private boolean certified;

    public Skill() {
        super();
    }

    public Skill(int skillId, String skillName, String category) {
        super();
        this.skillId = skillId;
        this.skillName = skillName;
        this.category = category;
        this.proficiencyLevel = "Beginner";
        this.experienceYears = 1;
        this.certified = false;
    }

    public Skill(int skillId, String skillName, String category, String proficiencyLevel, int experienceYears, boolean certified) {
        super();
        this.skillId = skillId;
        this.skillName = skillName;
        this.category = category;
        this.proficiencyLevel = proficiencyLevel;
        this.experienceYears = experienceYears;
        this.certified = certified;
    }

    public int getSkillId() {
        return skillId;
    }

    public void setSkillId(int skillId) {
        this.skillId = skillId;
    }

    public String getSkillName() {
        return skillName;
    }

    public void setSkillName(String skillName) {
        this.skillName = skillName;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getProficiencyLevel() {
        return proficiencyLevel;
    }

    public void setProficiencyLevel(String proficiencyLevel) {
        this.proficiencyLevel = proficiencyLevel;
    }

    public int getExperienceYears() {
        return experienceYears;
    }

    public void setExperienceYears(int experienceYears) {
        this.experienceYears = experienceYears;
    }

    public boolean isCertified() {
        return certified;
    }

    public void setCertified(boolean certified) {
        this.certified = certified;
    }

    @Override
    public String getEntityIdentifier() {
        return "Skill-" + skillId + " (" + skillName + ")";
    }

    @Override
    public String toString() {
        return String.format("%s [%s] - %s (%d yrs%s)", 
            skillName, category, proficiencyLevel, experienceYears, certified ? ", Certified" : "");
    }
}
