-- ==============================================================================
-- MYSQL COMPATIBLE DDL & DML SCRIPT
-- Student Project and Skill Tracking System (SPSTS)
-- Course: 25ITT31 - Database Management Systems
-- Student: Gokul . S (Reg. No: 732925ITR032) | Class: 25IT3A
-- Institution: Velalar College of Engineering and Technology, Erode - 12
-- Compatible with: MySQL 8.0+ / MariaDB / XAMPP / MySQL Workbench
-- ==============================================================================

CREATE DATABASE IF NOT EXISTS student_project_tracking_db;
USE student_project_tracking_db;

-- Disable foreign key checks for clean recreation
SET FOREIGN_KEY_CHECKS = 0;
DROP TABLE IF EXISTS evaluations;
DROP TABLE IF EXISTS milestones;
DROP TABLE IF EXISTS projects;
DROP TABLE IF EXISTS student_skills;
DROP TABLE IF EXISTS skills;
DROP TABLE IF EXISTS students;
SET FOREIGN_KEY_CHECKS = 1;

-- 1. Table: students
CREATE TABLE students (
    register_no VARCHAR(15) PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    department VARCHAR(50) DEFAULT 'Information Technology',
    class_section VARCHAR(10) NOT NULL,
    email VARCHAR(120) UNIQUE NOT NULL,
    phone VARCHAR(15),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 2. Table: skills
CREATE TABLE skills (
    skill_id INT AUTO_INCREMENT PRIMARY KEY,
    skill_name VARCHAR(50) UNIQUE NOT NULL,
    category ENUM('Programming Language', 'Web Framework', 'Database', 'Cloud / DevOps', 'AI / ML Tool', 'Mobile Development') NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 3. Table: student_skills
CREATE TABLE student_skills (
    id INT AUTO_INCREMENT PRIMARY KEY,
    register_no VARCHAR(15) NOT NULL,
    skill_id INT NOT NULL,
    proficiency_level ENUM('Beginner', 'Intermediate', 'Advanced') NOT NULL,
    experience_years INT NOT NULL DEFAULT 1 CHECK (experience_years >= 0),
    certified TINYINT(1) DEFAULT 0 CHECK (certified IN (0, 1)),
    CONSTRAINT fk_student_skills_student FOREIGN KEY (register_no) REFERENCES students(register_no) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT fk_student_skills_skill FOREIGN KEY (skill_id) REFERENCES skills(skill_id) ON DELETE CASCADE ON UPDATE CASCADE,
    UNIQUE KEY uk_student_skill (register_no, skill_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 4. Table: projects
CREATE TABLE projects (
    project_id INT AUTO_INCREMENT PRIMARY KEY,
    register_no VARCHAR(15) NOT NULL,
    title VARCHAR(255) NOT NULL,
    domain ENUM('Data Science', 'Web Development', 'Machine Learning', 'Cloud Computing', 'Internet of Things (IoT)', 'Cyber Security', 'Mobile Applications') NOT NULL,
    description TEXT,
    technologies_used VARCHAR(255) NOT NULL,
    start_date DATE NOT NULL,
    completion_status ENUM('In Progress', 'Completed', 'Delayed', 'On Hold') NOT NULL DEFAULT 'In Progress',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_projects_student FOREIGN KEY (register_no) REFERENCES students(register_no) ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 5. Table: milestones
CREATE TABLE milestones (
    milestone_id INT AUTO_INCREMENT PRIMARY KEY,
    project_id INT NOT NULL,
    milestone_name VARCHAR(100) NOT NULL,
    due_date DATE NOT NULL,
    submission_date DATE NULL,
    status ENUM('Pending', 'In Progress', 'Submitted', 'Approved', 'Delayed') NOT NULL DEFAULT 'Pending',
    remarks TEXT,
    CONSTRAINT fk_milestones_project FOREIGN KEY (project_id) REFERENCES projects(project_id) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 6. Table: evaluations
CREATE TABLE evaluations (
    evaluation_id INT AUTO_INCREMENT PRIMARY KEY,
    project_id INT NOT NULL,
    milestone_id INT NOT NULL,
    literature_survey_marks DECIMAL(5,2) NOT NULL DEFAULT 0.0 CHECK (literature_survey_marks BETWEEN 0.0 AND 20.0),
    system_design_marks DECIMAL(5,2) NOT NULL DEFAULT 0.0 CHECK (system_design_marks BETWEEN 0.0 AND 20.0),
    implementation_marks DECIMAL(5,2) NOT NULL DEFAULT 0.0 CHECK (implementation_marks BETWEEN 0.0 AND 30.0),
    testing_marks DECIMAL(5,2) NOT NULL DEFAULT 0.0 CHECK (testing_marks BETWEEN 0.0 AND 20.0),
    presentation_marks DECIMAL(5,2) NOT NULL DEFAULT 0.0 CHECK (presentation_marks BETWEEN 0.0 AND 10.0),
    total_marks DECIMAL(5,2) GENERATED ALWAYS AS (
        literature_survey_marks + system_design_marks + implementation_marks + testing_marks + presentation_marks
    ) STORED,
    grade ENUM('O', 'A+', 'A', 'B+', 'B', 'RA') NOT NULL,
    evaluated_date DATE DEFAULT (CURRENT_DATE),
    CONSTRAINT fk_evaluations_project FOREIGN KEY (project_id) REFERENCES projects(project_id) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT fk_evaluations_milestone FOREIGN KEY (milestone_id) REFERENCES milestones(milestone_id) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Views
CREATE OR REPLACE VIEW view_student_project_summary AS
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
