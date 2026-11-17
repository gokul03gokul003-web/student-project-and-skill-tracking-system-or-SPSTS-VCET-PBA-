package com.vcet.tracking.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Model representing an IT student with academic profile and skill associations.
 * Demonstrates Unit 1: OOP, Unit 2: Inheritance, Unit 5: Collections Framework (List).
 */
public class Student extends BaseEntity {
    private String registerNo;
    private String name;
    private String department;
    private String classSection;
    private String email;
    private String phone;
    private List<Skill> skills;

    public Student() {
        super();
        this.skills = new ArrayList<>();
        this.department = "Information Technology";
    }

    public Student(String registerNo, String name, String classSection, String email, String phone) {
        super();
        this.registerNo = registerNo;
        this.name = name;
        this.department = "Information Technology";
        this.classSection = classSection;
        this.email = email;
        this.phone = phone;
        this.skills = new ArrayList<>();
    }

    public String getRegisterNo() {
        return registerNo;
    }

    public void setRegisterNo(String registerNo) {
        this.registerNo = registerNo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getClassSection() {
        return classSection;
    }

    public void setClassSection(String classSection) {
        this.classSection = classSection;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public List<Skill> getSkills() {
        return skills;
    }

    public void setSkills(List<Skill> skills) {
        this.skills = (skills != null) ? skills : new ArrayList<>();
    }

    public void addSkill(Skill skill) {
        if (skill != null && !this.skills.contains(skill)) {
            this.skills.add(skill);
        }
    }

    @Override
    public String getEntityIdentifier() {
        return "Student-" + registerNo + " (" + name + ")";
    }

    @Override
    public String toString() {
        return String.format("[%s] %s | %s | %s | %s", 
            registerNo, name, classSection, email, phone != null ? phone : "N/A");
    }
}
