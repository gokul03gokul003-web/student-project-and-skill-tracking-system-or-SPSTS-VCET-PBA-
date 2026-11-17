package com.vcet.tracking.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import com.vcet.tracking.dao.EvaluationDAO;
import com.vcet.tracking.dao.MilestoneDAO;
import com.vcet.tracking.dao.ProjectDAO;
import com.vcet.tracking.dao.StudentDAO;
import com.vcet.tracking.exception.DatabaseException;
import com.vcet.tracking.exception.InvalidDataException;
import com.vcet.tracking.exception.RecordNotFoundException;
import com.vcet.tracking.model.Evaluation;
import com.vcet.tracking.model.Milestone;
import com.vcet.tracking.model.Project;
import com.vcet.tracking.model.Skill;
import com.vcet.tracking.model.Student;
import com.vcet.tracking.util.InputValidator;

/**
 * Core Service Layer managing business transactions and domain workflows.
 * Demonstrates Unit 1: OOP, Unit 2: Layered Architecture, Unit 3: Exception Handling.
 */
public class TrackingService {

    private final StudentDAO studentDAO;
    private final ProjectDAO projectDAO;
    private final MilestoneDAO milestoneDAO;
    private final EvaluationDAO evaluationDAO;

    public TrackingService() {
        this.studentDAO = new StudentDAO();
        this.projectDAO = new ProjectDAO();
        this.milestoneDAO = new MilestoneDAO();
        this.evaluationDAO = new EvaluationDAO();
    }

    public boolean registerStudent(String regNo, String name, String classSection, String email, String phone) 
            throws InvalidDataException, DatabaseException {
        InputValidator.validateRegisterNo(regNo);
        InputValidator.validateNonEmpty(name, "Student Name");
        InputValidator.validateNonEmpty(classSection, "Class Section");
        InputValidator.validateEmail(email);

        Student existing = studentDAO.findById(regNo);
        if (existing != null) {
            throw new InvalidDataException("Student with Register Number [" + regNo + "] already exists.");
        }

        Student student = new Student(regNo.trim().toUpperCase(), name.trim(), classSection.trim(), email.trim(), phone != null ? phone.trim() : null);
        return studentDAO.insert(student);
    }

    public boolean assignSkill(String regNo, int skillId, String proficiency, int expYears, boolean certified) 
            throws InvalidDataException, RecordNotFoundException, DatabaseException {
        InputValidator.validateRegisterNo(regNo);
        InputValidator.validateProficiency(proficiency);

        Student student = studentDAO.findById(regNo);
        if (student == null) {
            throw new RecordNotFoundException("Student with Register No [" + regNo + "] not found.");
        }

        return studentDAO.addStudentSkill(regNo.trim().toUpperCase(), skillId, proficiency, expYears, certified);
    }

    public int registerProject(String regNo, String title, String domain, String description, String techStack, String startDate) 
            throws InvalidDataException, RecordNotFoundException, DatabaseException {
        InputValidator.validateRegisterNo(regNo);
        InputValidator.validateNonEmpty(title, "Project Title");
        InputValidator.validateNonEmpty(domain, "Domain");
        InputValidator.validateNonEmpty(techStack, "Technologies Used");

        Student student = studentDAO.findById(regNo);
        if (student == null) {
            throw new RecordNotFoundException("Cannot register project. Student [" + regNo + "] does not exist.");
        }

        String actualStartDate = (startDate != null && !startDate.trim().isEmpty()) ? startDate.trim() : LocalDate.now().toString();

        Project project = new Project(0, regNo.trim().toUpperCase(), title.trim(), domain.trim(), 
                                      description != null ? description.trim() : "", techStack.trim(), 
                                      actualStartDate, "In Progress");

        boolean success = projectDAO.insert(project);
        if (success) {
            return project.getProjectId();
        }
        throw new DatabaseException("Failed to register project in database.");
    }

    public boolean addMilestone(int projectId, String name, String dueDate) 
            throws InvalidDataException, RecordNotFoundException, DatabaseException {
        InputValidator.validateNonEmpty(name, "Milestone Name");
        InputValidator.validateNonEmpty(dueDate, "Due Date");

        Project project = projectDAO.findById(projectId);
        if (project == null) {
            throw new RecordNotFoundException("Project #" + projectId + " does not exist.");
        }

        Milestone m = new Milestone(0, projectId, name.trim(), dueDate.trim(), null, "Pending", null);
        return milestoneDAO.insert(m);
    }

    public boolean recordMilestoneSubmission(int milestoneId, String submissionDate, String remarks) 
            throws InvalidDataException, RecordNotFoundException, DatabaseException {
        InputValidator.validateNonEmpty(submissionDate, "Submission Date");

        Milestone m = milestoneDAO.findById(milestoneId);
        if (m == null) {
            throw new RecordNotFoundException("Milestone #" + milestoneId + " not found.");
        }

        // Compare submission date with due date
        String status = "Submitted";
        try {
            LocalDate due = LocalDate.parse(m.getDueDate());
            LocalDate sub = LocalDate.parse(submissionDate.trim());
            if (sub.isAfter(due)) {
                status = "Delayed";
            }
        } catch (Exception ex) {
            // Keep default
        }

        return milestoneDAO.recordSubmission(milestoneId, submissionDate.trim(), status, remarks);
    }

    public boolean recordEvaluation(int projectId, int milestoneId, 
                                    double survey, double design, double impl, double test, double viva) 
            throws InvalidDataException, RecordNotFoundException, DatabaseException {
        InputValidator.validateRubricMarks(survey, 20.0, "Literature Survey");
        InputValidator.validateRubricMarks(design, 20.0, "System Design");
        InputValidator.validateRubricMarks(impl, 30.0, "Implementation");
        InputValidator.validateRubricMarks(test, 20.0, "Testing");
        InputValidator.validateRubricMarks(viva, 10.0, "Presentation/Viva");

        Project project = projectDAO.findById(projectId);
        if (project == null) {
            throw new RecordNotFoundException("Project #" + projectId + " not found.");
        }

        Milestone milestone = milestoneDAO.findById(milestoneId);
        if (milestone == null) {
            throw new RecordNotFoundException("Milestone #" + milestoneId + " not found.");
        }

        Evaluation eval = new Evaluation(0, projectId, milestoneId, survey, design, impl, test, viva, LocalDate.now().toString());
        boolean inserted = evaluationDAO.insert(eval);
        if (inserted) {
            // Approve milestone once evaluation is completed
            milestoneDAO.recordSubmission(milestoneId, milestone.getSubmissionDate() != null ? milestone.getSubmissionDate() : LocalDate.now().toString(), "Approved", "Evaluation completed. Grade: " + eval.getGrade());
        }
        return inserted;
    }

    public List<Student> getAllStudents() throws DatabaseException {
        return studentDAO.findAll();
    }

    public Student getStudent(String regNo) throws DatabaseException {
        return studentDAO.findById(regNo);
    }

    public List<Skill> getMasterSkills() throws DatabaseException {
        return studentDAO.getAllSkillsMaster();
    }

    public List<Project> getAllProjects() throws DatabaseException {
        return projectDAO.findAll();
    }

    public List<Project> getProjectsByDomain(String domain) throws DatabaseException {
        return projectDAO.findByDomain(domain);
    }

    public List<Milestone> getMilestonesByProject(int projectId) throws DatabaseException {
        return milestoneDAO.findByProject(projectId);
    }

    public List<Milestone> getDelayedMilestones() throws DatabaseException {
        return milestoneDAO.getDelayedMilestones();
    }

    public List<Evaluation> getAllEvaluations() throws DatabaseException {
        return evaluationDAO.findAll();
    }

    public List<Evaluation> getEvaluationsByProject(int projectId) throws DatabaseException {
        return evaluationDAO.findByProject(projectId);
    }

    public Map<String, Double> getAverageDomainScores() throws DatabaseException {
        return evaluationDAO.getAverageMarksByDomain();
    }
}
