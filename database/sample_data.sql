-- ==============================================================================
-- SAMPLE DATA: Student Project and Skill Tracking System (SPSTS)
-- Course: 25ITT31 - Database Management Systems
-- Student: Gokul . S (Reg. No: 732925ITR032) | Class: 25IT3A
-- Institution: Velalar College of Engineering and Technology, Erode - 12
-- ==============================================================================

-- ------------------------------------------------------------------------------
-- 1. Insert Master Skills Catalog
-- ------------------------------------------------------------------------------
INSERT INTO skills (skill_id, skill_name, category) VALUES
(1, 'Java', 'Programming Language'),
(2, 'Python', 'Programming Language'),
(3, 'C++', 'Programming Language'),
(4, 'JavaScript', 'Programming Language'),
(5, 'SQL / Relational DB', 'Database'),
(6, 'MongoDB / NoSQL', 'Database'),
(7, 'Spring Boot', 'Web Framework'),
(8, 'React.js', 'Web Framework'),
(9, 'Flask / FastAPI', 'Web Framework'),
(10, 'Pandas & NumPy', 'AI / ML Tool'),
(11, 'Scikit-Learn', 'AI / ML Tool'),
(12, 'TensorFlow / PyTorch', 'AI / ML Tool'),
(13, 'Docker & Kubernetes', 'Cloud / DevOps'),
(14, 'AWS Cloud Foundations', 'Cloud / DevOps'),
(15, 'Flutter', 'Mobile Development'),
(16, 'Git & Version Control', 'Cloud / DevOps');

-- ------------------------------------------------------------------------------
-- 2. Insert Students
-- Features Gokul . S (732925ITR032) and cohort peers (including 2, 62, 92, L2)
-- ------------------------------------------------------------------------------
INSERT INTO students (register_no, name, department, class_section, email, phone) VALUES
('732925ITR032', 'Gokul . S', 'Information Technology', '25IT3A', 'gokul.s@vcet.ac.in', '9842100032'),
('732925ITR002', 'Aakash . M', 'Information Technology', '25IT3A', 'aakash.m@vcet.ac.in', '9842100002'),
('732925ITR062', 'Kavin . P', 'Information Technology', '25IT3A', 'kavin.p@vcet.ac.in', '9842100062'),
('732925ITR092', 'Preethi . S', 'Information Technology', '25IT3B', 'preethi.s@vcet.ac.in', '9842100092'),
('732925ITL002', 'Vignesh . R', 'Information Technology', '25IT3B', 'vignesh.r@vcet.ac.in', '9842100102'),
('732925ITR015', 'Deepa . V', 'Information Technology', '25IT3A', 'deepa.v@vcet.ac.in', '9842100015'),
('732925ITR045', 'Harish . K', 'Information Technology', '25IT3A', 'harish.k@vcet.ac.in', '9842100045'),
('732925ITR078', 'Nithya . B', 'Information Technology', '25IT3B', 'nithya.b@vcet.ac.in', '9842100078'),
('732925ITR085', 'Pradeep . N', 'Information Technology', '25IT3B', 'pradeep.n@vcet.ac.in', '9842100085'),
('732925ITR105', 'Sneha . T', 'Information Technology', '25IT3B', 'sneha.t@vcet.ac.in', '9842100105');

-- ------------------------------------------------------------------------------
-- 3. Insert Student Technical Skills & Proficiencies
-- ------------------------------------------------------------------------------
INSERT INTO student_skills (register_no, skill_id, proficiency_level, experience_years, certified) VALUES
-- Gokul . S (Student Profile)
('732925ITR032', 1, 'Advanced', 3, 1),       -- Java
('732925ITR032', 2, 'Advanced', 2, 1),       -- Python
('732925ITR032', 5, 'Advanced', 2, 1),       -- SQL
('732925ITR032', 10, 'Advanced', 2, 0),      -- Pandas & NumPy
('732925ITR032', 7, 'Intermediate', 1, 0),  -- Spring Boot
('732925ITR032', 16, 'Advanced', 2, 1),      -- Git

-- Aakash . M
('732925ITR002', 3, 'Intermediate', 2, 0),  -- C++
('732925ITR002', 2, 'Intermediate', 1, 0),  -- Python
('732925ITR002', 5, 'Beginner', 1, 0),      -- SQL
('732925ITR002', 16, 'Intermediate', 1, 0), -- Git

-- Kavin . P
('732925ITR062', 2, 'Advanced', 2, 1),       -- Python
('732925ITR062', 10, 'Advanced', 2, 1),      -- Pandas
('732925ITR062', 11, 'Advanced', 2, 1),      -- Scikit-Learn
('732925ITR062', 12, 'Intermediate', 1, 0),  -- TensorFlow

-- Preethi . S
('732925ITR092', 1, 'Advanced', 2, 1),       -- Java
('732925ITR092', 4, 'Advanced', 2, 1),       -- JavaScript
('732925ITR092', 8, 'Advanced', 2, 0),       -- React
('732925ITR092', 5, 'Intermediate', 2, 0),  -- SQL

-- Vignesh . R
('732925ITL002', 2, 'Intermediate', 1, 0),  -- Python
('732925ITL002', 13, 'Intermediate', 1, 1), -- Docker
('732925ITL002', 14, 'Intermediate', 1, 1), -- AWS

-- Deepa . V
('732925ITR015', 2, 'Advanced', 2, 1),       -- Python
('732925ITR015', 3, 'Intermediate', 2, 0),  -- C++
('732925ITR015', 5, 'Intermediate', 1, 0),  -- SQL

-- Harish . K
('732925ITR045', 15, 'Advanced', 2, 1),      -- Flutter
('732925ITR045', 4, 'Intermediate', 2, 0),   -- JavaScript
('732925ITR045', 6, 'Intermediate', 1, 0),  -- MongoDB

-- Nithya . B
('732925ITR078', 2, 'Advanced', 2, 1),       -- Python
('732925ITR078', 10, 'Advanced', 2, 1),      -- Pandas
('732925ITR078', 5, 'Advanced', 2, 1),       -- SQL

-- Pradeep . N
('732925ITR085', 2, 'Intermediate', 2, 0),  -- Python
('732925ITR085', 11, 'Intermediate', 1, 0), -- Scikit-Learn
('732925ITR085', 9, 'Beginner', 1, 0),      -- Flask

-- Sneha . T
('732925ITR105', 1, 'Intermediate', 2, 0),  -- Java
('732925ITR105', 4, 'Intermediate', 1, 0),  -- JavaScript
('732925ITR105', 5, 'Intermediate', 1, 0);  -- SQL

-- ------------------------------------------------------------------------------
-- 4. Insert Student Projects
-- ------------------------------------------------------------------------------
INSERT INTO projects (project_id, register_no, title, domain, description, technologies_used, start_date, completion_status) VALUES
(101, '732925ITR032', 'Student Project and Skill Tracking System', 'Data Science', 'Centralized academic tracking system consolidating student mini-projects, skill matrices, milestone monitoring, and EDA visualization.', 'Java, SQLite, Python, Pandas, Matplotlib, Seaborn', '2026-09-01', 'Completed'),
(102, '732925ITR002', 'IoT Smart Agriculture & Soil Irrigation Monitor', 'Internet of Things (IoT)', 'Low-cost sensor monitoring system measuring soil moisture, humidity, and temperature for automated pump irrigation.', 'C++, ESP32, Python, MQTT, SQLite', '2026-09-05', 'In Progress'),
(103, '732925ITR062', 'Automated Histopathological Cancer Diagnosis', 'Machine Learning', 'Computer vision deep learning classifier trained on biopsy images to identify malignant cell clusters with high precision.', 'Python, TensorFlow, OpenCV, Pandas, Flask', '2026-09-02', 'Completed'),
(104, '732925ITR092', 'Secure E-Commerce Microservices Platform', 'Web Development', 'High-throughput online shopping platform featuring JWT authentication, product catalog, payment gateway integration, and cart.', 'Java, Spring Boot, React.js, MySQL, Docker', '2026-09-10', 'In Progress'),
(105, '732925ITL002', 'Cloud-Native Serverless Log Analyzer', 'Cloud Computing', 'Distributed log analysis tool capturing container logs, detecting anomaly surges, and notifying admins via AWS SNS alerts.', 'Python, AWS Lambda, Docker, CloudWatch, SQLite', '2026-09-12', 'In Progress'),
(106, '732925ITR015', 'Real-time Phishing URL Detection System', 'Cyber Security', 'Machine learning based defensive cybersecurity extension detecting phishing domain heuristics and malicious lexical patterns.', 'Python, Scikit-Learn, Flask, SQLite', '2026-09-08', 'Completed'),
(107, '732925ITR045', 'Android Campus Navigation and Event Directory', 'Mobile Applications', 'Cross-platform mobile application providing GPS campus floor plans, department directories, and real-time event reminders.', 'Flutter, Dart, Firebase, SQLite', '2026-09-15', 'In Progress'),
(108, '732925ITR078', 'Customer Churn Prediction and Risk Analytics', 'Data Science', 'Predictive behavioral analysis model predicting customer attrition in telecommunications with explanatory feature importance.', 'Python, Pandas, Scikit-Learn, Seaborn, SQLite', '2026-09-04', 'Completed'),
(109, '732925ITR085', 'Automated Resume Parsing & Candidate Screening', 'Machine Learning', 'NLP pipeline extracting candidate skills, education, and domain relevance scores matching job description keywords.', 'Python, spaCy, NLTK, Flask, SQLite', '2026-09-18', 'Delayed'),
(110, '732925ITR105', 'Blockchain-based Academic Credential Verification', 'Cyber Security', 'Tamper-proof verifiable decentralized ledger system validating authenticity of university degrees and marksheets.', 'Solidity, Java, Web3j, React, SQLite', '2026-09-06', 'In Progress');

-- ------------------------------------------------------------------------------
-- 5. Insert Project Milestones
-- ------------------------------------------------------------------------------
INSERT INTO milestones (milestone_id, project_id, milestone_name, due_date, submission_date, status, remarks) VALUES
-- Project 101 (Gokul . S - SPSTS)
(201, 101, 'M1: Problem Analysis & SRS Document', '2026-09-20', '2026-09-19', 'Approved', 'Comprehensive IEEE SRS document submitted.'),
(202, 101, 'M2: System Architecture & Database Design', '2026-10-05', '2026-10-04', 'Approved', 'Normalized BCNF schema and ERD verified.'),
(203, 101, 'M3: Java Core Application & DAO Development', '2026-10-25', '2026-10-24', 'Approved', 'Clean OOP, JDBC, and Collections implemented.'),
(204, 101, 'M4: Python Data Science & Visualization Pipeline', '2026-11-10', '2026-11-09', 'Approved', 'Pandas EDA and Seaborn visualizations generated.'),
(205, 101, 'M5: Testing, Documentation & Final Demo', '2026-11-17', '2026-11-16', 'Approved', 'All 23 test cases passed with 100% success.'),

-- Project 102 (IoT)
(206, 102, 'M1: Hardware Selection & Circuit Design', '2026-09-25', '2026-09-24', 'Approved', 'ESP32 pinouts and sensor wiring documented.'),
(207, 102, 'M2: Microcontroller Firmware & MQTT Integration', '2026-10-15', '2026-10-15', 'Approved', 'Data packets publishing successfully.'),
(208, 102, 'M3: Cloud Dashboard & Alert Engine', '2026-11-05', '2026-11-12', 'Delayed', 'Late submission due to sensor calibration delays.'),
(209, 102, 'M4: Final Field Testing', '2026-11-25', NULL, 'Pending', 'Scheduled for upcoming review.'),

-- Project 103 (Cancer Diagnosis ML)
(210, 103, 'M1: Dataset Acquisition & Preprocessing', '2026-09-22', '2026-09-21', 'Approved', 'Histopathological imagery augmented.'),
(211, 103, 'M2: CNN Model Training & Hyperparameter Tuning', '2026-10-12', '2026-10-11', 'Approved', '96.4% test accuracy achieved.'),
(212, 103, 'M3: Evaluation & Flask API Deployment', '2026-11-02', '2026-11-01', 'Approved', 'Inference API response under 200ms.'),

-- Project 104 (E-Commerce Web)
(213, 104, 'M1: Requirements & DB Design', '2026-09-28', '2026-09-27', 'Approved', 'Database schema design completed.'),
(214, 104, 'M2: Spring Boot REST Services', '2026-10-20', '2026-10-20', 'Approved', 'Auth and catalog endpoints tested.'),
(215, 104, 'M3: React Frontend & Payment Gateway', '2026-11-15', NULL, 'In Progress', 'Frontend components under integration.'),

-- Project 106 (Phishing Detection)
(216, 106, 'M1: Feature Engineering & Dataset Cleaning', '2026-09-25', '2026-09-24', 'Approved', 'Extracted 30 URL lexical features.'),
(217, 106, 'M2: Classifier Training (Random Forest & XGBoost)', '2026-10-18', '2026-10-17', 'Approved', 'High F1-score attained.'),
(218, 106, 'M3: Browser Extension Integration', '2026-11-08', '2026-11-07', 'Approved', 'Real-time warning alerts functional.'),

-- Project 108 (Customer Churn Data Science)
(219, 108, 'M1: Exploratory Data Analysis & Handling Imbalance', '2026-09-20', '2026-09-19', 'Approved', 'SMOTE and feature scaling performed.'),
(220, 108, 'M2: Predictive Modeling & ROC-AUC Analysis', '2026-10-15', '2026-10-14', 'Approved', 'AUC score 0.89 documented.'),
(221, 108, 'M3: Executive Dashboard & Insights Delivery', '2026-11-05', '2026-11-04', 'Approved', 'Final visualization report completed.'),

-- Project 109 (Resume Parser - Delayed)
(222, 109, 'M1: NER Entity Extraction Model', '2026-10-10', '2026-10-20', 'Delayed', 'Significant delay in dataset labeling.'),
(223, 109, 'M2: Scoring Algorithm & Web Interface', '2026-11-10', NULL, 'Pending', 'Incomplete requirements.');

-- ------------------------------------------------------------------------------
-- 6. Insert Component Evaluations & Rubric Marks
-- ------------------------------------------------------------------------------
INSERT INTO evaluations (
    evaluation_id, project_id, milestone_id, 
    literature_survey_marks, system_design_marks, implementation_marks, testing_marks, presentation_marks, 
    grade, evaluated_date
) VALUES
-- Gokul . S (SPSTS Project 101) - Score: 19 + 19 + 29 + 19 + 9.5 = 95.5 -> Grade O
(301, 101, 201, 19.0, 18.5, 27.5, 18.5, 9.0, 'O', '2026-09-22'),
(302, 101, 203, 19.5, 19.0, 29.0, 19.0, 9.5, 'O', '2026-10-28'),
(303, 101, 205, 20.0, 19.5, 29.5, 19.5, 10.0, 'O', '2026-11-17'),

-- Aakash . M (IoT Project 102) - Score: 16 + 15 + 23 + 15 + 7.5 = 76.5 -> Grade A
(304, 102, 206, 16.0, 16.0, 22.0, 15.0, 8.0, 'A', '2026-09-28'),
(305, 102, 207, 15.5, 15.0, 23.5, 15.0, 7.5, 'A', '2026-10-18'),

-- Kavin . P (Cancer ML Project 103) - Score: 18.5 + 18.5 + 28.0 + 18.0 + 9.0 = 92.0 -> Grade O
(306, 103, 210, 18.0, 18.0, 27.0, 17.5, 9.0, 'O', '2026-09-25'),
(307, 103, 212, 19.0, 18.5, 28.5, 18.5, 9.0, 'O', '2026-11-05'),

-- Preethi . S (E-Commerce Project 104) - Score: 17 + 17.5 + 25 + 17 + 8.5 = 85.0 -> Grade A+
(308, 104, 213, 17.0, 18.0, 24.5, 17.0, 8.5, 'A+', '2026-10-02'),
(309, 104, 214, 17.5, 17.0, 25.5, 17.0, 8.5, 'A+', '2026-10-23'),

-- Deepa . V (Phishing CyberSec Project 106) - Score: 18 + 17 + 26 + 17.5 + 8.5 = 87.0 -> Grade A+
(310, 106, 216, 17.5, 17.0, 26.0, 17.0, 8.5, 'A+', '2026-09-28'),
(311, 106, 218, 18.5, 17.5, 26.5, 18.0, 9.0, 'A+', '2026-11-10'),

-- Nithya . B (Customer Churn Project 108) - Score: 18 + 18 + 27 + 18 + 9 = 90.0 -> Grade O
(312, 108, 219, 17.5, 18.0, 26.5, 17.5, 8.5, 'A+', '2026-09-24'),
(313, 108, 221, 18.5, 18.5, 27.5, 18.5, 9.5, 'O', '2026-11-08'),

-- Pradeep . N (Resume Parser Project 109) - Score: 12 + 11 + 18 + 11 + 6 = 58.0 -> Grade B
(314, 109, 222, 12.0, 11.5, 18.0, 11.0, 6.0, 'B', '2026-10-24');
