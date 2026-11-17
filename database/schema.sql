-- ==============================================================================
-- DATABASE SCHEMA: Student Project and Skill Tracking System (SPSTS)
-- Course: 25ITT31 - Database Management Systems
-- Student: Gokul . S (Reg. No: 732925ITR032) | Class: 25IT3A
-- Institution: Velalar College of Engineering and Technology, Erode - 12
-- Database Engine: SQLite 3
-- ==============================================================================

PRAGMA foreign_keys = ON;

-- ------------------------------------------------------------------------------
-- 1. Table: students
-- Stores core academic identity and contact details of IT students.
-- ------------------------------------------------------------------------------
DROP TABLE IF EXISTS evaluations;
DROP TABLE IF EXISTS milestones;
DROP TABLE IF EXISTS projects;
DROP TABLE IF EXISTS student_skills;
DROP TABLE IF EXISTS skills;
DROP TABLE IF EXISTS students;

CREATE TABLE students (
    register_no VARCHAR(15) PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    department VARCHAR(50) DEFAULT 'Information Technology',
    class_section VARCHAR(10) NOT NULL,
    email VARCHAR(120) UNIQUE NOT NULL,
    phone VARCHAR(15),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- ------------------------------------------------------------------------------
-- 2. Table: skills
-- Master catalog of technical skills, programming languages, and frameworks.
-- ------------------------------------------------------------------------------
CREATE TABLE skills (
    skill_id INTEGER PRIMARY KEY AUTOINCREMENT,
    skill_name VARCHAR(50) UNIQUE NOT NULL,
    category VARCHAR(50) NOT NULL CHECK(category IN ('Programming Language', 'Web Framework', 'Database', 'Cloud / DevOps', 'AI / ML Tool', 'Mobile Development'))
);

-- ------------------------------------------------------------------------------
-- 3. Table: student_skills
-- Junction table mapping students to their technical skills and proficiencies.
-- Normalized to 3NF/BCNF (resolves multi-valued dependency).
-- ------------------------------------------------------------------------------
CREATE TABLE student_skills (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    register_no VARCHAR(15) NOT NULL,
    skill_id INTEGER NOT NULL,
    proficiency_level VARCHAR(20) NOT NULL CHECK(proficiency_level IN ('Beginner', 'Intermediate', 'Advanced')),
    experience_years INTEGER NOT NULL DEFAULT 1 CHECK(experience_years >= 0),
    certified INTEGER DEFAULT 0 CHECK(certified IN (0, 1)),
    FOREIGN KEY (register_no) REFERENCES students(register_no) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (skill_id) REFERENCES skills(skill_id) ON DELETE CASCADE ON UPDATE CASCADE,
    UNIQUE(register_no, skill_id)
);

-- ------------------------------------------------------------------------------
-- 4. Table: projects
-- Stores student mini-project registrations, domains, and technologies used.
-- ------------------------------------------------------------------------------
CREATE TABLE projects (
    project_id INTEGER PRIMARY KEY AUTOINCREMENT,
    register_no VARCHAR(15) NOT NULL,
    title VARCHAR(255) NOT NULL,
    domain VARCHAR(50) NOT NULL CHECK(domain IN ('Data Science', 'Web Development', 'Machine Learning', 'Cloud Computing', 'Internet of Things (IoT)', 'Cyber Security', 'Mobile Applications')),
    description TEXT,
    technologies_used VARCHAR(255) NOT NULL,
    start_date DATE NOT NULL,
    completion_status VARCHAR(20) NOT NULL DEFAULT 'In Progress' CHECK(completion_status IN ('In Progress', 'Completed', 'Delayed', 'On Hold')),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (register_no) REFERENCES students(register_no) ON DELETE RESTRICT ON UPDATE CASCADE
);

-- ------------------------------------------------------------------------------
-- 5. Table: milestones
-- Tracks stage-wise project deliverables, due dates, submissions, and status.
-- ------------------------------------------------------------------------------
CREATE TABLE milestones (
    milestone_id INTEGER PRIMARY KEY AUTOINCREMENT,
    project_id INTEGER NOT NULL,
    milestone_name VARCHAR(100) NOT NULL,
    due_date DATE NOT NULL,
    submission_date DATE,
    status VARCHAR(20) NOT NULL DEFAULT 'Pending' CHECK(status IN ('Pending', 'In Progress', 'Submitted', 'Approved', 'Delayed')),
    remarks TEXT,
    FOREIGN KEY (project_id) REFERENCES projects(project_id) ON DELETE CASCADE ON UPDATE CASCADE
);

-- ------------------------------------------------------------------------------
-- 6. Table: evaluations
-- Stores rubric-based assessment marks across defined project components.
-- Total marks is automatically computed from the 5 component criteria.
-- ------------------------------------------------------------------------------
CREATE TABLE evaluations (
    evaluation_id INTEGER PRIMARY KEY AUTOINCREMENT,
    project_id INTEGER NOT NULL,
    milestone_id INTEGER NOT NULL,
    literature_survey_marks NUMERIC(5,2) NOT NULL DEFAULT 0.0 CHECK(literature_survey_marks BETWEEN 0.0 AND 20.0),
    system_design_marks NUMERIC(5,2) NOT NULL DEFAULT 0.0 CHECK(system_design_marks BETWEEN 0.0 AND 20.0),
    implementation_marks NUMERIC(5,2) NOT NULL DEFAULT 0.0 CHECK(implementation_marks BETWEEN 0.0 AND 30.0),
    testing_marks NUMERIC(5,2) NOT NULL DEFAULT 0.0 CHECK(testing_marks BETWEEN 0.0 AND 20.0),
    presentation_marks NUMERIC(5,2) NOT NULL DEFAULT 0.0 CHECK(presentation_marks BETWEEN 0.0 AND 10.0),
    total_marks NUMERIC(5,2) GENERATED ALWAYS AS (
        literature_survey_marks + system_design_marks + implementation_marks + testing_marks + presentation_marks
    ) STORED,
    grade VARCHAR(5) NOT NULL CHECK(grade IN ('O', 'A+', 'A', 'B+', 'B', 'RA')),
    evaluated_date DATE DEFAULT CURRENT_DATE,
    FOREIGN KEY (project_id) REFERENCES projects(project_id) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (milestone_id) REFERENCES milestones(milestone_id) ON DELETE CASCADE ON UPDATE CASCADE
);

-- ------------------------------------------------------------------------------
-- INDEXES FOR QUERY OPTIMIZATION
-- ------------------------------------------------------------------------------
CREATE INDEX idx_student_skills_reg ON student_skills(register_no);
CREATE INDEX idx_student_skills_skill ON student_skills(skill_id);
CREATE INDEX idx_projects_reg ON projects(register_no);
CREATE INDEX idx_projects_domain ON projects(domain);
CREATE INDEX idx_milestones_proj ON milestones(project_id);
CREATE INDEX idx_evaluations_proj ON evaluations(project_id);

-- ------------------------------------------------------------------------------
-- VIEWS
-- ------------------------------------------------------------------------------

-- View 1: Student Project Performance Summary
CREATE VIEW IF NOT EXISTS view_student_project_summary AS
SELECT 
    s.register_no,
    s.name AS student_name,
    s.class_section,
    p.project_id,
    p.title AS project_title,
    p.domain,
    p.completion_status,
    COUNT(m.milestone_id) AS total_milestones,
    SUM(CASE WHEN m.status = 'Approved' THEN 1 ELSE 0 END) AS approved_milestones,
    COALESCE(ROUND(AVG(e.total_marks), 2), 0.0) AS average_score
FROM students s
JOIN projects p ON s.register_no = p.register_no
LEFT JOIN milestones m ON p.project_id = m.project_id
LEFT JOIN evaluations e ON p.project_id = e.project_id
GROUP BY s.register_no, p.project_id;

-- View 2: Skill Distribution Overview
CREATE VIEW IF NOT EXISTS view_skill_inventory AS
SELECT 
    sk.skill_name,
    sk.category,
    COUNT(ss.register_no) AS total_students_count,
    SUM(CASE WHEN ss.proficiency_level = 'Advanced' THEN 1 ELSE 0 END) AS advanced_count,
    SUM(CASE WHEN ss.proficiency_level = 'Intermediate' THEN 1 ELSE 0 END) AS intermediate_count,
    SUM(CASE WHEN ss.proficiency_level = 'Beginner' THEN 1 ELSE 0 END) AS beginner_count
FROM skills sk
LEFT JOIN student_skills ss ON sk.skill_id = ss.skill_id
GROUP BY sk.skill_id;

-- ------------------------------------------------------------------------------
-- TRIGGERS
-- ------------------------------------------------------------------------------

-- Trigger 1: Automatically mark milestone as 'Delayed' if submitted after due_date
CREATE TRIGGER IF NOT EXISTS trigger_check_milestone_delay
AFTER UPDATE OF submission_date ON milestones
FOR EACH ROW
WHEN NEW.submission_date IS NOT NULL AND NEW.submission_date > NEW.due_date AND NEW.status != 'Approved'
BEGIN
    UPDATE milestones 
    SET status = 'Delayed' 
    WHERE milestone_id = NEW.milestone_id;
END;

-- Trigger 2: Auto-update project completion_status to 'Completed' when all milestones are Approved
CREATE TRIGGER IF NOT EXISTS trigger_auto_complete_project
AFTER UPDATE OF status ON milestones
FOR EACH ROW
WHEN NEW.status = 'Approved'
BEGIN
    UPDATE projects
    SET completion_status = 'Completed'
    WHERE project_id = NEW.project_id
      AND (SELECT COUNT(*) FROM milestones WHERE project_id = NEW.project_id AND status != 'Approved') = 0;
END;
