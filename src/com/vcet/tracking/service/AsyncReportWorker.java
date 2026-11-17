package com.vcet.tracking.service;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import com.vcet.tracking.dao.ProjectDAO;
import com.vcet.tracking.dao.StudentDAO;
import com.vcet.tracking.model.Project;
import com.vcet.tracking.model.Student;

/**
 * Background Thread Worker performing asynchronous audit logging and data backup.
 * Demonstrates Unit 3: Java Thread Model, Runnable, Thread Priorities, and Synchronization.
 */
public class AsyncReportWorker implements Runnable {

    private final String logFilePath;
    private volatile boolean isRunning;

    public AsyncReportWorker(String logFilePath) {
        this.logFilePath = logFilePath;
        this.isRunning = true;
    }

    @Override
    public void run() {
        try {
            System.out.println("[Background Worker Thread: " + Thread.currentThread().getName() + 
                               " | Priority: " + Thread.currentThread().getPriority() + "] Initiated...");

            // Simulate quick background audit generation
            Thread.sleep(600);

            StudentDAO sdao = new StudentDAO();
            ProjectDAO pdao = new ProjectDAO();

            List<Student> students = sdao.findAll();
            List<Project> projects = pdao.findAll();

            synchronized (this) {
                try (PrintWriter writer = new PrintWriter(new FileWriter(logFilePath, false))) {
                    writer.println("==========================================================");
                    writer.println("SPSTS ASYNCHRONOUS AUDIT SNAPSHOT");
                    writer.println("Generated: " + LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
                    writer.println("==========================================================");
                    writer.println("Total Registered Students: " + students.size());
                    writer.println("Total Registered Projects: " + projects.size());
                    writer.println("----------------------------------------------------------");
                    writer.println("STUDENT REGISTRY:");
                    for (Student s : students) {
                        writer.println(" - [" + s.getRegisterNo() + "] " + s.getName() + " (" + s.getClassSection() + ")");
                    }
                    writer.println("----------------------------------------------------------");
                    writer.println("PROJECT REGISTRY:");
                    for (Project p : projects) {
                        writer.println(" - Proj #" + p.getProjectId() + ": " + p.getTitle() + " [" + p.getDomain() + "] - " + p.getCompletionStatus());
                    }
                    writer.println("==========================================================");
                }
            }

            System.out.println("[Background Worker Thread: " + Thread.currentThread().getName() + "] Audit snapshot saved to: " + logFilePath);

        } catch (InterruptedException e) {
            System.err.println("[Background Worker Thread] Interrupted: " + e.getMessage());
            Thread.currentThread().interrupt();
        } catch (Exception e) {
            System.err.println("[Background Worker Thread] Error: " + e.getMessage());
        } finally {
            isRunning = false;
        }
    }

    public boolean isRunning() {
        return isRunning;
    }
}
