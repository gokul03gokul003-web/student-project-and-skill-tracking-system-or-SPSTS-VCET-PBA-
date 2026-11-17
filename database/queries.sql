-- ==============================================================================
-- ANALYTICAL SQL QUERIES: Student Project and Skill Tracking System (SPSTS)
-- Course: 25ITT31 - Database Management Systems (Unit 2: SQL & Unit 3: Analytics)
-- Student: Gokul . S (Reg. No: 732925ITR032) | Class: 25IT3A
-- Institution: Velalar College of Engineering and Technology, Erode - 12
-- ==============================================================================

-- ------------------------------------------------------------------------------
-- QUERY 1: Student Project Registry with Primary Technology & Status
-- Concepts: INNER JOIN, Column Aliasing, Ordering
-- ------------------------------------------------------------------------------
SELECT 
    s.register_no AS "Register No",
    s.name AS "Student Name",
    s.class_section AS "Class",
    p.project_id AS "Project ID",
    p.title AS "Project Title",
    p.domain AS "Domain",
    p.completion_status AS "Status"
FROM students s
INNER JOIN projects p ON s.register_no = p.register_no
ORDER BY p.domain ASC, s.name ASC;

-- ------------------------------------------------------------------------------
-- QUERY 2: Milestone Progress and Completion Percentage per Project
-- Concepts: LEFT JOIN, GROUP BY, Aggregate Functions (COUNT, SUM, ROUND)
-- ------------------------------------------------------------------------------
SELECT 
    p.project_id AS "Project ID",
    p.title AS "Project Title",
    COUNT(m.milestone_id) AS "Total Milestones",
    SUM(CASE WHEN m.status = 'Approved' THEN 1 ELSE 0 END) AS "Approved Count",
    SUM(CASE WHEN m.status = 'Delayed' THEN 1 ELSE 0 END) AS "Delayed Count",
    SUM(CASE WHEN m.status IN ('Pending', 'In Progress') THEN 1 ELSE 0 END) AS "Incomplete Count",
    ROUND(
        (CAST(SUM(CASE WHEN m.status = 'Approved' THEN 1 ELSE 0 END) AS FLOAT) / 
         NULLIF(COUNT(m.milestone_id), 0)) * 100.0, 
        2
    ) AS "Completion Rate (%)"
FROM projects p
LEFT JOIN milestones m ON p.project_id = m.project_id
GROUP BY p.project_id, p.title
ORDER BY "Completion Rate (%)" DESC;

-- ------------------------------------------------------------------------------
-- QUERY 3: Delayed Milestones and Turnaround Time Analysis
-- Concepts: Filtering (WHERE), Date Difference, String Concatenation
-- ------------------------------------------------------------------------------
SELECT 
    p.project_id AS "Project ID",
    s.name AS "Student",
    p.title AS "Project Title",
    m.milestone_name AS "Milestone",
    m.due_date AS "Target Due Date",
    COALESCE(m.submission_date, 'Not Submitted') AS "Actual Submission",
    m.status AS "Milestone Status",
    m.remarks AS "Evaluator Remarks"
FROM milestones m
JOIN projects p ON m.project_id = p.project_id
JOIN students s ON p.register_no = s.register_no
WHERE m.status = 'Delayed' OR (m.status != 'Approved' AND m.due_date < DATE('now'))
ORDER BY m.due_date ASC;

-- ------------------------------------------------------------------------------
-- QUERY 4: Comprehensive Rubric Evaluation Breakdown & Grade Assignment
-- Concepts: Multi-table JOIN, Average Aggregation, Conditional Formatting
-- ------------------------------------------------------------------------------
SELECT 
    s.register_no AS "Reg No",
    s.name AS "Student Name",
    p.title AS "Project Title",
    ROUND(AVG(e.literature_survey_marks), 2) AS "Avg Survey (20)",
    ROUND(AVG(e.system_design_marks), 2) AS "Avg Design (20)",
    ROUND(AVG(e.implementation_marks), 2) AS "Avg Impl (30)",
    ROUND(AVG(e.testing_marks), 2) AS "Avg Testing (20)",
    ROUND(AVG(e.presentation_marks), 2) AS "Avg Viva (10)",
    ROUND(AVG(e.total_marks), 2) AS "Overall Score (100)",
    MAX(e.grade) AS "Final Grade"
FROM students s
JOIN projects p ON s.register_no = p.register_no
JOIN evaluations e ON p.project_id = e.project_id
GROUP BY s.register_no, s.name, p.title
ORDER BY "Overall Score (100)" DESC;

-- ------------------------------------------------------------------------------
-- QUERY 5: Technical Skill Inventory & Proficiency Matrix across Cohort
-- Concepts: GROUP BY, Pivot-style CASE SUM Aggregations
-- ------------------------------------------------------------------------------
SELECT 
    sk.category AS "Category",
    sk.skill_name AS "Skill Name",
    COUNT(ss.register_no) AS "Student Count",
    SUM(CASE WHEN ss.proficiency_level = 'Advanced' THEN 1 ELSE 0 END) AS "Advanced",
    SUM(CASE WHEN ss.proficiency_level = 'Intermediate' THEN 1 ELSE 0 END) AS "Intermediate",
    SUM(CASE WHEN ss.proficiency_level = 'Beginner' THEN 1 ELSE 0 END) AS "Beginner",
    SUM(CASE WHEN ss.certified = 1 THEN 1 ELSE 0 END) AS "Certified Students"
FROM skills sk
LEFT JOIN student_skills ss ON sk.skill_id = ss.skill_id
GROUP BY sk.category, sk.skill_name
HAVING COUNT(ss.register_no) > 0
ORDER BY "Student Count" DESC, sk.skill_name ASC;

-- ------------------------------------------------------------------------------
-- QUERY 6: Domain-wise Performance & Evaluation Benchmarking
-- Concepts: GROUP BY domain, Statistical Averages (MIN, MAX, AVG)
-- ------------------------------------------------------------------------------
SELECT 
    p.domain AS "Domain",
    COUNT(DISTINCT p.project_id) AS "Total Projects",
    ROUND(AVG(e.total_marks), 2) AS "Avg Evaluation Score",
    MIN(e.total_marks) AS "Min Score",
    MAX(e.total_marks) AS "Max Score"
FROM projects p
JOIN evaluations e ON p.project_id = e.project_id
GROUP BY p.domain
ORDER BY "Avg Evaluation Score" DESC;

-- ------------------------------------------------------------------------------
-- QUERY 7: Skill Gap Analysis - Students Working on AI/ML without Advanced Math/Python
-- Concepts: Subqueries with NOT IN, Complex Boolean Filters
-- ------------------------------------------------------------------------------
SELECT 
    s.register_no AS "Reg No",
    s.name AS "Student Name",
    p.title AS "Project Title",
    p.domain AS "Domain"
FROM students s
JOIN projects p ON s.register_no = p.register_no
WHERE p.domain IN ('Machine Learning', 'Data Science')
  AND s.register_no NOT IN (
      SELECT ss.register_no 
      FROM student_skills ss
      JOIN skills sk ON ss.skill_id = sk.skill_id
      WHERE sk.skill_name IN ('Python', 'Pandas & NumPy') 
        AND ss.proficiency_level = 'Advanced'
  );

-- ------------------------------------------------------------------------------
-- QUERY 8: Window Function - Ranking Projects within each Domain
-- Concepts: Window Functions (DENSE_RANK() OVER (PARTITION BY ... ORDER BY ...))
-- ------------------------------------------------------------------------------
SELECT 
    p.domain AS "Domain",
    p.title AS "Project Title",
    s.name AS "Student Name",
    ROUND(AVG(e.total_marks), 2) AS "Average Score",
    DENSE_RANK() OVER (
        PARTITION BY p.domain 
        ORDER BY AVG(e.total_marks) DESC
    ) AS "Rank within Domain"
FROM projects p
JOIN students s ON p.register_no = s.register_no
JOIN evaluations e ON p.project_id = e.project_id
GROUP BY p.domain, p.project_id, p.title, s.name
ORDER BY p.domain ASC, "Rank within Domain" ASC;
