package com.vcet.tracking.service;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.vcet.tracking.model.Evaluation;
import com.vcet.tracking.model.Milestone;
import com.vcet.tracking.model.Project;
import com.vcet.tracking.model.Student;

/**
 * Analytical Report Generator utilizing Java Streams, Lambdas, and Functional Interfaces.
 * Demonstrates Unit 5: Collections Framework, Lambda Expressions, and Stream API.
 */
public class ReportGenerator {

    /**
     * Filters and returns projects that are currently Delayed or have overdue milestones.
     * Uses Stream filter and lambda predicate.
     */
    public static List<Project> findDelayedProjects(List<Project> allProjects, List<Milestone> allMilestones) {
        List<Integer> delayedProjectIds = allMilestones.stream()
            .filter(Milestone::isDelayed)
            .map(Milestone::getProjectId)
            .distinct()
            .collect(Collectors.toList());

        return allProjects.stream()
            .filter(p -> "Delayed".equalsIgnoreCase(p.getCompletionStatus()) || delayedProjectIds.contains(p.getProjectId()))
            .sorted(Comparator.comparing(Project::getTitle))
            .collect(Collectors.toList());
    }

    /**
     * Groups projects by their technical domain and counts them.
     * Demonstrates Collectors.groupingBy() and Collectors.counting().
     */
    public static Map<String, Long> countProjectsByDomain(List<Project> projects) {
        return projects.stream()
            .collect(Collectors.groupingBy(Project::getDomain, Collectors.counting()));
    }

    /**
     * Filters evaluations to find top performers (Grade 'O' or 'A+').
     * Demonstrates stream filtering, sorted with comparator.
     */
    public static List<Evaluation> findTopEvaluations(List<Evaluation> evaluations) {
        return evaluations.stream()
            .filter(e -> "O".equalsIgnoreCase(e.getGrade()) || "A+".equalsIgnoreCase(e.getGrade()))
            .sorted(Comparator.comparingDouble(Evaluation::getTotalMarks).reversed())
            .collect(Collectors.toList());
    }

    /**
     * Computes the average evaluation score across all evaluated projects.
     * Demonstrates Collectors.averagingDouble().
     */
    public static double computeCohortAverageScore(List<Evaluation> evaluations) {
        return evaluations.stream()
            .collect(Collectors.averagingDouble(Evaluation::getTotalMarks));
    }

    /**
     * Finds students possessing a specific skill with at least Intermediate proficiency.
     * Demonstrates nested stream filtering and lambdas.
     */
    public static List<Student> filterStudentsBySkill(List<Student> students, String targetSkill) {
        return students.stream()
            .filter(s -> s.getSkills().stream().anyMatch(sk -> 
                sk.getSkillName().equalsIgnoreCase(targetSkill) && 
                ("Advanced".equalsIgnoreCase(sk.getProficiencyLevel()) || "Intermediate".equalsIgnoreCase(sk.getProficiencyLevel()))
            ))
            .sorted(Comparator.comparing(Student::getName))
            .collect(Collectors.toList());
    }
}
