package com.vcet.tracking;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import com.vcet.tracking.exception.DatabaseException;
import com.vcet.tracking.exception.InvalidDataException;
import com.vcet.tracking.exception.RecordNotFoundException;
import com.vcet.tracking.model.Evaluation;
import com.vcet.tracking.model.Milestone;
import com.vcet.tracking.model.Project;
import com.vcet.tracking.model.Skill;
import com.vcet.tracking.model.Student;
import com.vcet.tracking.service.AsyncReportWorker;
import com.vcet.tracking.service.ReportGenerator;
import com.vcet.tracking.service.TrackingService;
import com.vcet.tracking.util.TableFormatter;

/**
 * Main Interactive Console Application for the Student Project and Skill Tracking System.
 *
 * Project-Based Assessment Integration:
 * - Course 25ITT32: Java Programming (OOP, I/O, Inheritance, Exceptions, Multithreading, Streams, JDBC)
 * - Course 25ITT31: Database Management Systems (SQLite Relational Model)
 * - Course 25ITT33: Software Engineering (Architecture & Verification)
 *
 * Author: Gokul . S (Reg. No: 732925ITR032)
 * Class: 25IT3A, Department of Information Technology
 * Institution: Velalar College of Engineering and Technology, Erode - 12
 */
public class Main {

    private static final TrackingService service = new TrackingService();
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        printBanner();

        boolean running = true;
        while (running) {
            printMainMenu();
            System.out.print("Select an option [0 - 7]: ");
            String choice = scanner.nextLine().trim();

            try {
                switch (choice) {
                    case "1":
                        handleStudentManagement();
                        break;
                    case "2":
                        handleSkillManagement();
                        break;
                    case "3":
                        handleProjectManagement();
                        break;
                    case "4":
                        handleMilestoneTracking();
                        break;
                    case "5":
                        handleEvaluationRubrics();
                        break;
                    case "6":
                        handleAnalyticalStreamReports();
                        break;
                    case "7":
                        handleAsyncAuditWorker();
                        break;
                    case "0":
                        System.out.println("\nExiting Student Project and Skill Tracking System. Thank you!");
                        running = false;
                        break;
                    default:
                        System.out.println("\n[!] Invalid selection. Please enter a valid menu number between 0 and 7.\n");
                }
            } catch (Exception ex) {
                System.err.println("\n[!] Operational Error: " + ex.getMessage() + "\n");
            }
        }
    }

    private static void printBanner() {
        System.out.println("==========================================================================================");
        System.out.println("       VELALAR COLLEGE OF ENGINEERING AND TECHNOLOGY (AUTONOMOUS), ERODE - 638012         ");
        System.out.println("                       DEPARTMENT OF INFORMATION TECHNOLOGY                               ");
        System.out.println("                    CLASS: 25IT3A | ACADEMIC YEAR: 2026-27 (ODD)                          ");
        System.out.println("          PROJECT-BASED ASSESSMENT: STUDENT PROJECT AND SKILL TRACKING SYSTEM             ");
        System.out.println("                 Student: Gokul . S  |  Register Number: 732925ITR032                     ");
        System.out.println("==========================================================================================");
    }

    private static void printMainMenu() {
        System.out.println("\n------------- MAIN MENU -------------");
        System.out.println("  1. Student Profile & Registry");
        System.out.println("  2. Technical Skills Inventory");
        System.out.println("  3. Student Project Registry");
        System.out.println("  4. Milestone Progress & Tracking");
        System.out.println("  5. Assessment & Rubric Evaluations");
        System.out.println("  6. Advanced Stream Analytics & Reports");
        System.out.println("  7. Run Asynchronous Audit Worker (Thread)");
        System.out.println("  0. Exit System");
        System.out.println("-------------------------------------");
    }

    // =========================================================================
    // 1. Student Management
    // =========================================================================
    private static void handleStudentManagement() throws DatabaseException, InvalidDataException {
        System.out.println("\n--- [1] STUDENT MANAGEMENT ---");
        System.out.println("  a. View All Registered Students");
        System.out.println("  b. Register New Student");
        System.out.println("  c. Search Student Profile by Register Number");
        System.out.print("Choose action [a/b/c]: ");
        String sub = scanner.nextLine().trim().toLowerCase();

        if ("a".equals(sub)) {
            List<Student> students = service.getAllStudents();
            String[] headers = {"Reg No", "Student Name", "Class", "Department", "Email Address", "Phone"};
            List<String[]> rows = new ArrayList<>();
            for (Student s : students) {
                rows.add(new String[]{
                    s.getRegisterNo(), s.getName(), s.getClassSection(), 
                    s.getDepartment(), s.getEmail(), s.getPhone() != null ? s.getPhone() : "N/A"
                });
            }
            TableFormatter.printTable(headers, rows);

        } else if ("b".equals(sub)) {
            System.out.print("Enter Register Number (e.g. 732925ITR099): ");
            String regNo = scanner.nextLine().trim();
            System.out.print("Enter Student Full Name: ");
            String name = scanner.nextLine().trim();
            System.out.print("Enter Class Section (e.g. 25IT3A): ");
            String cls = scanner.nextLine().trim();
            System.out.print("Enter Email Address: ");
            String email = scanner.nextLine().trim();
            System.out.print("Enter Mobile Number: ");
            String phone = scanner.nextLine().trim();

            boolean ok = service.registerStudent(regNo, name, cls, email, phone);
            if (ok) {
                System.out.println("[SUCCESS] Student [" + regNo + "] registered successfully.");
            }

        } else if ("c".equals(sub)) {
            System.out.print("Enter Register Number to Search: ");
            String regNo = scanner.nextLine().trim();
            Student s = service.getStudent(regNo);
            if (s != null) {
                System.out.println("\n=== STUDENT PROFILE ===");
                System.out.println("Register No : " + s.getRegisterNo());
                System.out.println("Name        : " + s.getName());
                System.out.println("Class       : " + s.getClassSection());
                System.out.println("Department  : " + s.getDepartment());
                System.out.println("Email       : " + s.getEmail());
                System.out.println("Phone       : " + s.getPhone());
                System.out.println("\nAssociated Technical Skills (" + s.getSkills().size() + "):");
                if (s.getSkills().isEmpty()) {
                    System.out.println("  (No skills mapped yet)");
                } else {
                    for (Skill sk : s.getSkills()) {
                        System.out.println("  - " + sk.toString());
                    }
                }
            } else {
                System.out.println("[!] No student found with Register Number: " + regNo);
            }
        }
    }

    // =========================================================================
    // 2. Technical Skill Inventory
    // =========================================================================
    private static void handleSkillManagement() throws DatabaseException, InvalidDataException, RecordNotFoundException {
        System.out.println("\n--- [2] TECHNICAL SKILL INVENTORY ---");
        System.out.println("  a. View Master Skills Catalog");
        System.out.println("  b. Map Technical Skill to Student");
        System.out.print("Choose action [a/b]: ");
        String sub = scanner.nextLine().trim().toLowerCase();

        if ("a".equals(sub)) {
            List<Skill> master = service.getMasterSkills();
            String[] headers = {"Skill ID", "Skill / Language", "Category"};
            List<String[]> rows = new ArrayList<>();
            for (Skill sk : master) {
                rows.add(new String[]{String.valueOf(sk.getSkillId()), sk.getSkillName(), sk.getCategory()});
            }
            TableFormatter.printTable(headers, rows);

        } else if ("b".equals(sub)) {
            System.out.print("Enter Student Register Number: ");
            String regNo = scanner.nextLine().trim();
            System.out.print("Enter Skill ID (refer master catalog): ");
            int skillId = Integer.parseInt(scanner.nextLine().trim());
            System.out.print("Proficiency Level (Beginner / Intermediate / Advanced): ");
            String prof = scanner.nextLine().trim();
            System.out.print("Years of Hands-on Experience: ");
            int exp = Integer.parseInt(scanner.nextLine().trim());
            System.out.print("Is Student Certified? (yes/no): ");
            boolean cert = "yes".equalsIgnoreCase(scanner.nextLine().trim());

            boolean ok = service.assignSkill(regNo, skillId, prof, exp, cert);
            if (ok) {
                System.out.println("[SUCCESS] Skill successfully mapped to student [" + regNo + "].");
            }
        }
    }

    // =========================================================================
    // 3. Project Registration & Management
    // =========================================================================
    private static void handleProjectManagement() throws DatabaseException, InvalidDataException, RecordNotFoundException {
        System.out.println("\n--- [3] PROJECT REGISTRY ---");
        System.out.println("  a. View All Registered Projects");
        System.out.println("  b. Register New Student Project");
        System.out.println("  c. Filter Projects by Domain");
        System.out.print("Choose action [a/b/c]: ");
        String sub = scanner.nextLine().trim().toLowerCase();

        if ("a".equals(sub)) {
            List<Project> list = service.getAllProjects();
            printProjectTable(list);

        } else if ("b".equals(sub)) {
            System.out.print("Enter Student Register Number: ");
            String regNo = scanner.nextLine().trim();
            System.out.print("Enter Project Title: ");
            String title = scanner.nextLine().trim();
            System.out.println("Available Domains: Data Science, Web Development, Machine Learning, Cloud Computing, Internet of Things (IoT), Cyber Security, Mobile Applications");
            System.out.print("Enter Domain: ");
            String domain = scanner.nextLine().trim();
            System.out.print("Enter Brief Description: ");
            String desc = scanner.nextLine().trim();
            System.out.print("Enter Technologies Used (comma-separated): ");
            String tech = scanner.nextLine().trim();
            System.out.print("Enter Start Date (YYYY-MM-DD or leave blank for today): ");
            String sdate = scanner.nextLine().trim();

            int id = service.registerProject(regNo, title, domain, desc, tech, sdate);
            System.out.println("[SUCCESS] Project registered successfully with Generated ID: " + id);

        } else if ("c".equals(sub)) {
            System.out.print("Enter Domain to filter (e.g. Data Science, Machine Learning, Web Development): ");
            String domain = scanner.nextLine().trim();
            List<Project> list = service.getProjectsByDomain(domain);
            printProjectTable(list);
        }
    }

    private static void printProjectTable(List<Project> list) {
        String[] headers = {"ID", "Student RegNo", "Project Title", "Domain", "Status", "Technologies Used"};
        List<String[]> rows = new ArrayList<>();
        for (Project p : list) {
            rows.add(new String[]{
                String.valueOf(p.getProjectId()), p.getRegisterNo(), 
                p.getTitle(), p.getDomain(), p.getCompletionStatus(), p.getTechnologiesUsed()
            });
        }
        TableFormatter.printTable(headers, rows);
    }

    // =========================================================================
    // 4. Milestone Progress & Tracking
    // =========================================================================
    private static void handleMilestoneTracking() throws DatabaseException, InvalidDataException, RecordNotFoundException {
        System.out.println("\n--- [4] MILESTONE PROGRESS & TRACKING ---");
        System.out.println("  a. View Milestones for a Project");
        System.out.println("  b. Add Milestone to Project");
        System.out.println("  c. Record Deliverable Submission");
        System.out.println("  d. View Delayed Milestones Across Department");
        System.out.print("Choose action [a/b/c/d]: ");
        String sub = scanner.nextLine().trim().toLowerCase();

        if ("a".equals(sub)) {
            System.out.print("Enter Project ID: ");
            int pid = Integer.parseInt(scanner.nextLine().trim());
            List<Milestone> list = service.getMilestonesByProject(pid);
            printMilestoneTable(list);

        } else if ("b".equals(sub)) {
            System.out.print("Enter Project ID: ");
            int pid = Integer.parseInt(scanner.nextLine().trim());
            System.out.print("Enter Milestone Name (e.g. M1: SRS Submission): ");
            String name = scanner.nextLine().trim();
            System.out.print("Enter Scheduled Due Date (YYYY-MM-DD): ");
            String due = scanner.nextLine().trim();

            boolean ok = service.addMilestone(pid, name, due);
            if (ok) System.out.println("[SUCCESS] Milestone added successfully.");

        } else if ("c".equals(sub)) {
            System.out.print("Enter Milestone ID: ");
            int mid = Integer.parseInt(scanner.nextLine().trim());
            System.out.print("Enter Submission Date (YYYY-MM-DD): ");
            String sdate = scanner.nextLine().trim();
            System.out.print("Enter Remarks / Comments: ");
            String remarks = scanner.nextLine().trim();

            boolean ok = service.recordMilestoneSubmission(mid, sdate, remarks);
            if (ok) System.out.println("[SUCCESS] Submission recorded and status updated.");

        } else if ("d".equals(sub)) {
            List<Milestone> delayed = service.getDelayedMilestones();
            System.out.println("\n=== DELAYED / OVERDUE MILESTONES (" + delayed.size() + ") ===");
            printMilestoneTable(delayed);
        }
    }

    private static void printMilestoneTable(List<Milestone> list) {
        String[] headers = {"Milestone ID", "Project ID", "Milestone Name", "Due Date", "Submission Date", "Status", "Remarks"};
        List<String[]> rows = new ArrayList<>();
        for (Milestone m : list) {
            rows.add(new String[]{
                String.valueOf(m.getMilestoneId()), String.valueOf(m.getProjectId()),
                m.getMilestoneName(), m.getDueDate(), 
                m.getSubmissionDate() != null ? m.getSubmissionDate() : "Pending",
                m.getStatus(), m.getRemarks() != null ? m.getRemarks() : ""
            });
        }
        TableFormatter.printTable(headers, rows);
    }

    // =========================================================================
    // 5. Assessment & Rubric Evaluations
    // =========================================================================
    private static void handleEvaluationRubrics() throws DatabaseException, InvalidDataException, RecordNotFoundException {
        System.out.println("\n--- [5] ASSESSMENT & RUBRICS EVALUATIONS ---");
        System.out.println("  a. View All Evaluation Records");
        System.out.println("  b. Record Rubric Marks for Project Milestone");
        System.out.println("  c. View Top Performing Evaluations (Grade O & A+)");
        System.out.print("Choose action [a/b/c]: ");
        String sub = scanner.nextLine().trim().toLowerCase();

        if ("a".equals(sub)) {
            List<Evaluation> evals = service.getAllEvaluations();
            printEvaluationTable(evals);

        } else if ("b".equals(sub)) {
            System.out.print("Enter Project ID: ");
            int pid = Integer.parseInt(scanner.nextLine().trim());
            System.out.print("Enter Milestone ID: ");
            int mid = Integer.parseInt(scanner.nextLine().trim());
            System.out.print("Literature Survey Marks [Max 20.0]: ");
            double s = Double.parseDouble(scanner.nextLine().trim());
            System.out.print("System Design Marks [Max 20.0]: ");
            double d = Double.parseDouble(scanner.nextLine().trim());
            System.out.print("Implementation Marks [Max 30.0]: ");
            double i = Double.parseDouble(scanner.nextLine().trim());
            System.out.print("Testing & Validation Marks [Max 20.0]: ");
            double t = Double.parseDouble(scanner.nextLine().trim());
            System.out.print("Presentation & Viva Marks [Max 10.0]: ");
            double v = Double.parseDouble(scanner.nextLine().trim());

            boolean ok = service.recordEvaluation(pid, mid, s, d, i, t, v);
            if (ok) {
                double total = s + d + i + t + v;
                System.out.println("\n[SUCCESS] Evaluation recorded!");
                System.out.println("Total Score: " + total + " / 100.0 | Awarded Grade: " + Evaluation.computeGrade(total));
            }

        } else if ("c".equals(sub)) {
            List<Evaluation> all = service.getAllEvaluations();
            List<Evaluation> top = ReportGenerator.findTopEvaluations(all);
            System.out.println("\n=== TOP-PERFORMING EVALUATIONS (STREAMS FILTERED) ===");
            printEvaluationTable(top);
        }
    }

    private static void printEvaluationTable(List<Evaluation> list) {
        String[] headers = {"Eval ID", "Proj ID", "Survey (20)", "Design (20)", "Impl (30)", "Test (20)", "Viva (10)", "Total (100)", "Grade", "Date"};
        List<String[]> rows = new ArrayList<>();
        for (Evaluation e : list) {
            rows.add(new String[]{
                String.valueOf(e.getEvaluationId()), String.valueOf(e.getProjectId()),
                String.format("%.1f", e.getLiteratureSurveyMarks()),
                String.format("%.1f", e.getSystemDesignMarks()),
                String.format("%.1f", e.getImplementationMarks()),
                String.format("%.1f", e.getTestingMarks()),
                String.format("%.1f", e.getPresentationMarks()),
                String.format("%.1f", e.getTotalMarks()),
                e.getGrade(), e.getEvaluatedDate()
            });
        }
        TableFormatter.printTable(headers, rows);
    }

    // =========================================================================
    // 6. Advanced Stream Analytics & Reports (Java Streams API)
    // =========================================================================
    private static void handleAnalyticalStreamReports() throws DatabaseException {
        System.out.println("\n--- [6] ADVANCED STREAM ANALYTICS & REPORTS ---");
        List<Project> projects = service.getAllProjects();
        List<Student> students = service.getAllStudents();
        List<Evaluation> evals = service.getAllEvaluations();

        // 1. Domain Grouping using Collectors.groupingBy
        System.out.println("\n>>> [Report 1] Domain Distribution (Collectors.groupingBy & counting):");
        Map<String, Long> domainCounts = ReportGenerator.countProjectsByDomain(projects);
        domainCounts.forEach((domain, count) -> 
            System.out.println(String.format("  %-28s : %d projects", domain, count)));

        // 2. Cohort Evaluation Average
        double avgScore = ReportGenerator.computeCohortAverageScore(evals);
        System.out.println(String.format("\n>>> [Report 2] Overall Cohort Average Rubric Score: %.2f / 100.0", avgScore));

        // 3. Skill-based Student Filtering using Streams
        System.out.println("\n>>> [Report 3] Filter Students by Skill (e.g. Python / Java):");
        System.out.print("Enter skill name to query: ");
        String targetSkill = scanner.nextLine().trim();
        List<Student> skilledStudents = ReportGenerator.filterStudentsBySkill(students, targetSkill);
        System.out.println("Students proficient in '" + targetSkill + "' (Intermediate/Advanced): " + skilledStudents.size());
        for (Student s : skilledStudents) {
            System.out.println("  * [" + s.getRegisterNo() + "] " + s.getName() + " (" + s.getClassSection() + ")");
        }
    }

    // =========================================================================
    // 7. Multithreaded Background Audit Worker
    // =========================================================================
    private static void handleAsyncAuditWorker() {
        System.out.println("\n--- [7] MULTITHREADED ASYNCHRONOUS AUDIT WORKER ---");
        String logPath = "sample_outputs" + File.separator + "async_audit_snapshot.txt";

        AsyncReportWorker worker = new AsyncReportWorker(logPath);
        Thread workerThread = new Thread(worker, "AuditWorkerThread-1");
        workerThread.setPriority(Thread.NORM_PRIORITY + 2); // Priority setting
        workerThread.start();

        System.out.println("[Main Thread] Worker thread launched concurrently. Main thread continues without blocking!");
        try {
            // Wait for thread to complete demonstration
            workerThread.join();
            System.out.println("[Main Thread] Background worker execution finished successfully.");
        } catch (InterruptedException e) {
            System.err.println("[Main Thread] Thread interrupted: " + e.getMessage());
        }
    }
}
