# SOFTWARE REQUIREMENTS SPECIFICATION (SRS)
## Student Project and Skill Tracking System

---

### **Student & Project Metadata**
* **Institution:** Velalar College of Engineering and Technology (Autonomous), Erode – 638 012
* **Department:** Department of Information Technology
* **Academic Year:** 2026 – 2027 (Odd Semester)
* **Degree / Class:** B.Tech. Information Technology / Class 25IT3A
* **Course Code & Title:** 
  * 25ITT33 – Software Engineering
  * 25ITT32 – Java Programming
  * 25ITT31 – Database Management Systems
  * 25ITC31 – Foundations of Data Science
* **Activity:** Project-Based Assessment (Integrated Project)
* **Assigned Scenario:** Scenario 2 – Student Project and Skill Tracking System
* **Student Name:** Gokul . S
* **Register Number:** 732925ITR032
* **Document Version:** 1.0.0
* **Date:** November 17, 2026

---

## 1. INTRODUCTION

### 1.1 Purpose
This Software Requirements Specification (SRS) establishes the complete functional and non-functional requirements for the **Student Project and Skill Tracking System (SPSTS)**. The system is designed to streamline academic project administration, replace uncoordinated spreadsheet records, track student technical skills and proficiencies, monitor project milestones, record component evaluations, and feed analytical intelligence into Python-based visualization and reporting pipelines.

### 1.2 Document Conventions
* **Keywords:** SHALL / MUST indicates mandatory requirements; SHOULD indicates recommendations.
* **Typographical Conventions:** Class names, database tables, and code variables appear in `monospaced font`.
* **Standard:** This document adheres to IEEE Std 830-1998 (Recommended Practice for Software Requirements Specifications).

### 1.3 Intended Audience
* **Course Instructors & Project Review Committee:** To evaluate requirements compliance and system capabilities.
* **Software Developers (Java/Python):** For implementing business logic, data models, and analytical pipelines.
* **Database Administrators:** For schema design, indexing, and normalization verification.
* **Testing & Quality Assurance:** For executing unit, integration, and validation test cases.

### 1.4 Project Scope
The Student Project and Skill Tracking System addresses critical workflow bottlenecks in IT departments:
1. **Centralized Repository:** Consolidates student profiles, programming proficiencies, mini-project registrations, milestones, and assessment grades.
2. **Milestone & Progress Tracking:** Enables stage-wise tracking (Abstract, Design, Implementation, Testing, Final Demo) with real-time completion status.
3. **Assessment & Rubrics:** Manages quantitative scores across defined project components (Problem Formulation, System Architecture, Code Quality, Demo & Viva).
4. **Data Science Integration:** Exposes cleaned relational datasets to Python data science pipelines to uncover skill-gap distributions, popular tech-stack choices, and completion trajectories.

### 1.5 References
1. IEEE Std 830-1998: *IEEE Recommended Practice for Software Requirements Specifications*.
2. Pressman, R. S., *Software Engineering: A Practitioner's Approach*, 9th Edition, McGraw-Hill.
3. Elmasri, R., & Navathe, S. B., *Fundamentals of Database Systems*, 7th Edition, Pearson.
4. Schildt, H., *Java: The Complete Reference*, 12th Edition, Oracle Press.
5. McKinney, W., *Python for Data Analysis*, 3rd Edition, O'Reilly Media.

---

## 2. OVERALL DESCRIPTION

### 2.1 Product Perspective
SPSTS operates as an autonomous, self-contained desktop and data pipeline ecosystem. The architecture consists of:
* **Presentation & Business Layer:** Java Application (JDK 17+) implementing object-oriented principles, stream filtering, and robust input validation.
* **Data Persistence Layer:** SQLite embedded relational database with strict foreign keys, check constraints, and indexed views.
* **Analytical & Visualization Layer:** Python (NumPy, Pandas, Matplotlib, Seaborn) analytics engine retrieving relational data via SQL drivers for exploratory data analysis (EDA).

```
+-------------------------------------------------------------------+
|               Student Project & Skill Tracking System             |
+-------------------------------------------------------------------+
                                  |
         +------------------------+------------------------+
         |                                                 |
         v                                                 v
+-----------------------+                         +-----------------------+
|  Java Application     |                         |  Python Data Engine   |
|  - Student Management |                         |  - Pandas Extraction  |
|  - Project Registry   |                         |  - Data Cleansing     |
|  - Milestones & Marks |                         |  - Seaborn/Matplotlib |
|  - Reports & Streams  |                         |  - Visual Insights    |
+-----------------------+                         +-----------------------+
         |                                                 |
         +------------------------+------------------------+
                                  | JDBC / SQLite3
                                  v
                   +-------------------------------+
                   |     SQLite Relational DB      |
                   |   (student_tracking.db)       |
                   +-------------------------------+
```

### 2.2 Product Functions
* **FR-1 Student Profile Management:** Register, update, and search student records with academic metadata.
* **FR-2 Technical Skill Inventory:** Catalog programming languages, frameworks, proficiency ratings (Beginner, Intermediate, Advanced), and certifications.
* **FR-3 Project Registration:** Record title, domain (AI/ML, Web Development, Cloud, IoT, Cyber Security, etc.), summary description, and associated technology stack.
* **FR-4 Milestone Management:** Set milestone deadlines, track submissions, evaluate delay flags, and record completion status.
* **FR-5 Evaluation & Scoring:** Record component-wise assessment marks (Review 1, Review 2, Final Viva) and compute aggregated project ratings.
* **FR-6 Analytical Reporting:** Generate tabular progress reports, skill-gap analysis, and student-level summaries via Java Streams.
* **FR-7 Visual EDA:** Export project completion rates, skill distribution, domain frequencies, and correlation heatmaps.

### 2.3 User Classes and Characteristics
1. **Department Faculty / Project Coordinator:** High technical aptitude; requires detailed tracking of team milestones, deadlines, and grade entries.
2. **Project Review Committee / Evaluator:** Evaluates students against predefined rubrics and inspects milestone deliverables.
3. **Department Head (HoD) / Academic Auditor:** Reviews high-level visual charts, skill distribution graphs, and project completion summaries.

### 2.4 Operating Environment
* **Operating System:** Windows 10/11 (x64), Linux, or macOS.
* **Runtime Environments:** Java Virtual Machine (JDK 17 or higher) and Python 3.9+.
* **Database Engine:** SQLite 3.x (embedded, portable single-file database).
* **Storage Footprint:** Less than 50 MB including binary and sample datasets.

### 2.5 Design and Implementation Constraints
1. **Language Standards:** Java code must adhere to standard OOP principles (encapsulation, inheritance, polymorphism, abstraction) and leverage Java Collections and Streams API.
2. **Database Integrity:** Relational schema must satisfy Boyce-Codd Normal Form (BCNF) / Third Normal Form (3NF) to eliminate insertion, deletion, and update anomalies.
3. **Database Portability:** Must utilize zero-configuration SQLite for autonomous execution, complemented by standard ANSI SQL DDL/DML scripts compatible with MySQL.
4. **Data Science Libraries:** Exploratory data analysis must utilize standard data science libraries (`pandas`, `numpy`, `matplotlib`, `seaborn`).

### 2.6 Assumptions and Dependencies
* The database engine executes in single-tier embedded mode directly accessing `student_tracking.db`.
* Character encoding standard is UTF-8 across all modules.

---

## 3. SYSTEM FEATURES AND FUNCTIONAL REQUIREMENTS

### 3.1 Feature 1: Student and Technical Skill Management
* **Description:** Maintains comprehensive records of IT students and their evolving technical competencies.
* **Functional Requirements:**
  * **FR-1.1:** System shall capture Student Register Number (Primary Key), Name, Class Section, Email, and Contact Phone.
  * **FR-1.2:** System shall allow mapping multiple technical skills (e.g., Java, Python, C++, SQL, React, Flutter, TensorFlow) to each student.
  * **FR-1.3:** Each mapped skill shall record proficiency level (`Beginner`, `Intermediate`, `Expert`) and years of hands-on experience.
  * **FR-1.4:** System shall validate that duplicate skill entries for the same student are prevented.

### 3.2 Feature 2: Project Registration & Technology Mapping
* **Description:** Records individual student projects with domain classifications and technology tags.
* **Functional Requirements:**
  * **FR-2.1:** System shall register projects with unique Project ID, Title, Domain (e.g., *Data Science*, *Web Application*, *IoT & Embedded*, *Machine Learning*, *Cyber Security*), Abstract Description, and Start Date.
  * **FR-2.2:** System shall link each project to a primary student owner (Register Number).
  * **FR-2.3:** System shall record the primary programming languages and frameworks used in the project.
  * **FR-2.4:** System shall enforce validation ensuring project titles are non-empty and unique per academic year.

### 3.3 Feature 3: Milestone & Timeline Tracking
* **Description:** Tracks project phases, submission dates, and status transitions.
* **Functional Requirements:**
  * **FR-3.1:** System shall record milestone names (e.g., *SRS Submission*, *System Architecture & DB Design*, *Module 1 Development*, *Testing & Validation*, *Final Demo & Code Submission*).
  * **FR-3.2:** System shall record Target Due Date, Actual Submission Date, and Milestone Status (`Pending`, `In Progress`, `Submitted`, `Approved`, `Delayed`).
  * **FR-3.3:** System shall calculate if a submission is on-schedule or overdue based on target dates.

### 3.4 Feature 4: Evaluation and Assessment Rubrics
* **Description:** Records rubric-based marks for project components.
* **Functional Requirements:**
  * **FR-4.1:** System shall store evaluation records linked to Project ID and Milestone.
  * **FR-4.2:** System shall store marks for defined criteria:
    * Literature Survey / Requirement Clarity (Max 20)
    * System Design & Architecture (Max 20)
    * Implementation & Code Quality (Max 30)
    * Testing, Validation & Documentation (Max 20)
    * Presentation & Viva Voce (Max 10)
  * **FR-4.3:** System shall compute total percentage score and determine academic grade (`O`, `A+`, `A`, `B+`, `B`, `RA`).

### 3.5 Feature 5: Reporting and Stream Querying
* **Description:** Generates analytical summaries and filtered views.
* **Functional Requirements:**
  * **FR-5.1:** System shall display all students enrolled in a specific domain.
  * **FR-5.2:** System shall filter projects with pending or delayed milestones using Java Streams.
  * **FR-5.3:** System shall identify top-performing students based on aggregated evaluation marks.
  * **FR-5.4:** System shall display skill distribution summary across the entire cohort.

### 3.6 Feature 6: Python Data Retrieval & Visual EDA
* **Description:** Extracts database tables into Pandas DataFrames and generates analytical visualizations.
* **Functional Requirements:**
  * **FR-6.1:** Python script shall connect directly to SQLite database via standard `sqlite3` driver.
  * **FR-6.2:** System shall clean missing values, normalize date columns, and encode categorical variables.
  * **FR-6.3:** Script shall generate and save high-resolution plots:
    * Project Completion Rate (Donut / Pie Chart)
    * Most Popular Technologies & Languages (Horizontal Bar Chart)
    * Skill Proficiency Distribution (Grouped Bar Chart)
    * Domain-wise Project Breakdown (Count Plot)
    * Evaluation Marks Distribution (Histogram & KDE)
    * Milestone Status Summary (Stacked Bar Chart)

---

## 4. EXTERNAL INTERFACE REQUIREMENTS

### 4.1 User Interface
* **Console Command-Line Interface (CLI):** Interactive, menu-driven CLI formatted with ANSI box-drawing borders, validation prompts, and readable tabular outputs.
* **Exit and Recovery:** Clear escape routes and menu options (`0. Exit / Return to Main Menu`).

### 4.2 Software Interfaces
* **Database Driver:** `org.sqlite.JDBC` (SQLite JDBC Connector) for Java; `sqlite3` module for Python.
* **Data Processing Libraries:** Python 3 standard library, `pandas >= 1.3.0`, `numpy >= 1.20.0`, `matplotlib >= 3.4.0`, `seaborn >= 0.11.0`.

### 4.3 Hardware Interfaces
* Standard keyboard and terminal display; minimal 2 GB RAM and 200 MB disk space.

---

## 5. NON-FUNCTIONAL REQUIREMENTS

### 5.1 Performance Requirements
* Database queries on student and project tables shall execute in under 100 milliseconds for datasets up to 10,000 records.
* Python visualization rendering and export shall complete in under 5 seconds.

### 5.2 Reliability & Fault Tolerance
* All database transactions shall adhere to ACID principles.
* Java application shall implement custom exception handlers (`DatabaseException`, `RecordNotFoundException`, `InvalidDataException`) to prevent application crashes during invalid inputs or connection drops.

### 5.3 Security Requirements
* Relational schema implements `ON DELETE CASCADE` and `FOREIGN KEY` constraints to preserve referential integrity.
* SQL queries shall use `PreparedStatement` parameterized queries to mitigate SQL Injection vulnerabilities.

### 5.4 Maintainability & Extensibility
* Modular package design (`model`, `dao`, `service`, `util`, `exception`) conforming to Model-DAO-Service architectural patterns.
* Clean separation of concerns allows transitioning from SQLite to MySQL or PostgreSQL by simply modifying the JDBC connection URL.

---

## 6. VERIFICATION MATRIX (REQUIREMENT TRACEABILITY)

| Requirement ID | Description | Component / Class | Test Case ID |
|---|---|---|---|
| FR-1.1 to FR-1.4 | Student & Skill Registry | `StudentDAO`, `Student.java`, `Skill.java` | TC-STU-01, TC-STU-02 |
| FR-2.1 to FR-2.4 | Project Registration & Domains | `ProjectDAO`, `Project.java` | TC-PRJ-01, TC-PRJ-02 |
| FR-3.1 to FR-3.3 | Milestone Lifecycle Tracking | `MilestoneDAO`, `Milestone.java` | TC-MLS-01, TC-MLS-02 |
| FR-4.1 to FR-4.3 | Component Evaluation & Scoring | `EvaluationDAO`, `Evaluation.java` | TC-EVL-01, TC-EVL-02 |
| FR-5.1 to FR-5.4 | Analytical Reports via Streams | `ReportGenerator.java`, `TrackingService.java` | TC-REP-01, TC-REP-02 |
| FR-6.1 to FR-6.3 | Python EDA & Visualizations | `python/data_analysis.py`, Jupyter Notebook | TC-EDA-01, TC-EDA-02 |

---

## 7. SOFTWARE SIZING & LOC (LINES OF CODE) METRICS

### 7.1 Source Code Sizing Inventory
In conformance with Unit 1 (Software Project Estimation) of course **25ITT33: Software Engineering**, the software sizing has been formally measured:

| Curricular Component / Layer | Course | Files | Code LOC | Comment Lines | Blank Lines | Total Lines |
|---|:---:|:---:|:---:|:---:|:---:|:---:|
| **Java Core & JDBC Backend** | `25ITT32` | 21 | **1,912** | 155 | 309 | 2,376 |
| **Relational Database & SQL** | `25ITT31` | 4 | **441** | 151 | 59 | 651 |
| **Python Data Science & EDA** | `25ITC31` | 1 | **261** | 33 | 46 | 340 |
| **Web Dashboard (HTML5/JS/CSS)**| UI | 1 | **1,977** | 147 | 157 | 2,281 |
| **Software Engg Documentation** | `25ITT33` | 5 | **686** | 339 | 212 | 1,237 |
| **Tooling & Build Automation** | Tooling | 6 | **221** | 15 | 45 | 281 |
| **TOTAL PROJECT SIZING** | **Integrated** | **38** | **5,498** | **840** | **828** | **7,166** |

### 7.2 COCOMO I Effort & Schedule Estimation (Organic Model)
* **KLOC:** $5.498 \text{ KLOC}$
* **Development Effort ($E$):** $E = 2.4 \times (5.498)^{1.05} = \mathbf{14.37 \text{ Person-Months}}$
* **Development Schedule ($D$):** $D = 2.5 \times (14.37)^{0.38} = \mathbf{6.88 \text{ Months}}$
* **Average Staffing ($SS$):** $SS = \frac{E}{D} = \frac{14.37}{6.88} = \mathbf{2.09 \text{ Developers}}$
* **Productivity:** $\mathbf{382.63 \text{ LOC / Person-Month}}$
* **Comment Density:** $\mathbf{15.28\%}$ (840 comment lines / 5,498 code LOC)

*(For exhaustive file-by-file LOC breakdown, refer to [`docs/SOFTWARE_METRICS_AND_LOC.md`](file:///c:/project%201/docs/SOFTWARE_METRICS_AND_LOC.md)).*

---
**Approved by:** Gokul . S (Reg. No: 732925ITR032)  
**Date of Submission:** November 17, 2026
