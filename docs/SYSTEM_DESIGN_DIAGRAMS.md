# SYSTEM DESIGN AND UML DIAGRAMS
## Student Project and Skill Tracking System

* **Student Name:** Gokul . S
* **Register Number:** 732925ITR032
* **Class:** 25IT3A
* **Department:** Information Technology
* **Institution:** Velalar College of Engineering and Technology, Erode – 12
* **Course:** 25ITT33 – Software Engineering

---

## 1. OVERALL SYSTEM ARCHITECTURE

The Student Project and Skill Tracking System adopts a **Modular N-Tier Layered Architecture**, separating user interaction, business logic, data persistence, and data intelligence.

```mermaid
flowchart TD
    subgraph UI_Layer["1. Presentation Layer (UI)"]
        CLI["Interactive Console Menu (CLI)<br/>User Input & Formatted Tables"]
        CLI_Admin["Admin & Faculty Dashboard"]
    end

    subgraph Service_Layer["2. Business Logic & Service Layer"]
        Service["TrackingService<br/>Business Validations & Workflow"]
        ReportGen["ReportGenerator<br/>Java 8+ Streams & Summaries"]
        FilterService["DataFilterService<br/>Milestone & Skill Filtering"]
    end

    subgraph DAO_Layer["3. Data Access Object (DAO) Layer"]
        DBConn["DatabaseConnection<br/>Singleton JDBC Manager"]
        StudentDAO["StudentDAO"]
        ProjectDAO["ProjectDAO"]
        MilestoneDAO["MilestoneDAO"]
        EvaluationDAO["EvaluationDAO"]
    end

    subgraph Persistence_Layer["4. Database Persistence Layer"]
        SQLiteDB[("SQLite Database<br/>student_tracking.db")]
        SQLScripts["DDL / DML / Views / Triggers"]
    end

    subgraph Analytics_Layer["5. Data Science & Visualization Layer"]
        PyExtract["Python Data Extraction<br/>sqlite3 / SQL Queries"]
        PandasPrep["Data Cleansing & Transformation<br/>Pandas & NumPy"]
        SeabornViz["Exploratory Data Analysis<br/>Matplotlib & Seaborn"]
        Charts["Generated High-Res Charts<br/>(visualizations/*.png)"]
    end

    UI_Layer --> Service_Layer
    Service_Layer --> DAO_Layer
    DAO_Layer --> DBConn
    DBConn --> Persistence_Layer
    Persistence_Layer --> PyExtract
    PyExtract --> PandasPrep
    PandasPrep --> SeabornViz
    SeabornViz --> Charts
```

---

## 2. USE CASE DIAGRAM

The Use Case diagram illustrates interactions between the primary actors (**Department Faculty / Project Coordinator**, **Student**, and **Data Analyst / Evaluator**) and the core system functions.

```mermaid
flowchart LR
    Faculty(("Faculty / Project Coordinator"))
    Evaluator(("Project Evaluator"))
    Analyst(("Academic Auditor / Analyst"))

    subgraph SPSTS["Student Project and Skill Tracking System"]
        UC1(["UC-1: Register Student Details"])
        UC2(["UC-2: Manage Student Skills & Proficiency"])
        UC3(["UC-3: Register Student Project & Tech Stack"])
        UC4(["UC-4: Define & Update Milestones"])
        UC5(["UC-5: Record Assessment & Component Marks"])
        UC6(["UC-6: Generate Progress & Skill Gap Reports"])
        UC7(["UC-7: Extract Data for Python Analytics"])
        UC8(["UC-8: Generate Visualizations & Insights"])
    end

    Faculty --> UC1
    Faculty --> UC2
    Faculty --> UC3
    Faculty --> UC4
    Faculty --> UC6

    Evaluator --> UC4
    Evaluator --> UC5

    Analyst --> UC6
    Analyst --> UC7
    Analyst --> UC8

    UC3 -.->|<<includes>>| UC1
    UC4 -.->|<<includes>>| UC3
    UC5 -.->|<<includes>>| UC4
    UC8 -.->|<<includes>>| UC7
```

### Use Case Specifications

| Use Case ID | Name | Actor | Pre-condition | Post-condition |
|---|---|---|---|---|
| **UC-1** | Register Student | Faculty | Faculty authenticated | Student record saved in database |
| **UC-2** | Manage Skills | Faculty | Student registered | Technical skills & proficiency mapped |
| **UC-3** | Register Project | Faculty | Student registered | Project record created with title & domain |
| **UC-4** | Update Milestones | Faculty / Evaluator | Project registered | Milestone status & submission date logged |
| **UC-5** | Record Marks | Evaluator | Milestone reached | Component scores saved & grade calculated |
| **UC-6** | Generate Reports | Faculty / Auditor | Active records exist | Formatted console report displayed |
| **UC-7** | Extract Dataset | Analyst | SQLite DB accessible | Data loaded into Pandas DataFrames |
| **UC-8** | Generate Visualizations | Analyst | Data cleaned | Charts generated and saved to disk |

---

## 3. CLASS DIAGRAM

The Class Diagram illustrates the object-oriented structure of the Java application, depicting attributes, methods, interfaces, and relationships.

```mermaid
classDiagram
    class BaseEntity {
        <<abstract>>
        #int id
        #String createdAt
        +getId() int
        +getCreatedAt() String
    }

    class Student {
        -String registerNo
        -String name
        -String department
        -String classSection
        -String email
        -List~Skill~ skills
        +getRegisterNo() String
        +getName() String
        +getSkills() List~Skill~
        +addSkill(Skill skill) void
        +toString() String
    }

    class Skill {
        -int skillId
        -String skillName
        -String category
        -String proficiencyLevel
        -int experienceYears
        +getSkillName() String
        +getCategory() String
        +getProficiencyLevel() String
        +toString() String
    }

    class Project {
        -int projectId
        -String registerNo
        -String title
        -String domain
        -String description
        -String technologiesUsed
        -String startDate
        -String status
        +getProjectId() int
        +getRegisterNo() String
        +getTitle() String
        +getDomain() String
        +getTechnologiesUsed() String
        +getStatus() String
    }

    class Milestone {
        -int milestoneId
        -int projectId
        -String milestoneName
        -String dueDate
        -String submissionDate
        -String status
        -String remarks
        +isDelayed() boolean
        +getStatus() String
        +getMilestoneName() String
    }

    class Evaluation {
        -int evaluationId
        -int projectId
        -int milestoneId
        -double literatureSurveyMarks
        -double systemDesignMarks
        -double implementationMarks
        -double testingMarks
        -double presentationMarks
        -double totalMarks
        -String grade
        +calculateTotal() double
        +computeGrade() String
    }

    class DatabaseConnection {
        <<singleton>>
        -Connection connection
        -DatabaseConnection instance$
        +getInstance() DatabaseConnection$
        +getConnection() Connection
        +closeConnection() void
    }

    class GenericDAO~T~ {
        <<interface>>
        +insert(T entity) boolean
        +update(T entity) boolean
        +delete(int id) boolean
        +findById(int id) T
        +findAll() List~T~
    }

    class StudentDAO {
        +findByRegisterNo(String regNo) Student
        +getStudentSkills(String regNo) List~Skill~
        +addStudentSkill(String regNo, Skill skill) boolean
    }

    class ProjectDAO {
        +findByStudent(String regNo) List~Project~
        +findByDomain(String domain) List~Project~
        +updateStatus(int projectId, String status) boolean
    }

    class MilestoneDAO {
        +findByProject(int projectId) List~Milestone~
        +getDelayedMilestones() List~Milestone~
    }

    class EvaluationDAO {
        +findByProject(int projectId) List~Evaluation~
        +getAverageScoreByDomain() Map~String, Double~
    }

    BaseEntity <|-- Student
    BaseEntity <|-- Project
    BaseEntity <|-- Milestone
    BaseEntity <|-- Evaluation
    BaseEntity <|-- Skill

    GenericDAO <|.. StudentDAO
    GenericDAO <|.. ProjectDAO
    GenericDAO <|.. MilestoneDAO
    GenericDAO <|.. EvaluationDAO

    Student "1" *-- "0..*" Skill : possesses
    Student "1" *-- "1..*" Project : undertakes
    Project "1" *-- "1..*" Milestone : contains
    Milestone "1" *-- "0..1" Evaluation : evaluated in
```

---

## 4. SEQUENCE DIAGRAM: PROJECT REGISTRATION & EVALUATION WORKFLOW

```mermaid
sequenceDiagram
    autonumber
    actor Faculty as Faculty / Evaluator
    participant UI as Console CLI (Main)
    participant Svc as TrackingService
    participant SDAO as StudentDAO
    participant PDAO as ProjectDAO
    participant EDAO as EvaluationDAO
    participant DB as SQLite Database

    Faculty->>UI: Enter Student RegNo & Project Details
    UI->>Svc: registerProject(studentRegNo, title, domain, techStack)
    Svc->>SDAO: findByRegisterNo(studentRegNo)
    SDAO->>DB: SELECT * FROM students WHERE register_no = ?
    DB-->>SDAO: Student record found
    SDAO-->>Svc: Student object

    alt Student Not Found
        Svc-->>UI: Throw RecordNotFoundException
        UI-->>Faculty: Display "Student not found. Register student first."
    else Student Exists
        Svc->>PDAO: insert(project)
        PDAO->>DB: INSERT INTO projects (...) VALUES (...)
        DB-->>PDAO: Success (Project ID: 101)
        PDAO-->>Svc: Return true
        Svc-->>UI: Project Registered Successfully
        UI-->>Faculty: Display Project ID & Confirmation
    end

    Faculty->>UI: Submit Evaluation Marks for Milestone
    UI->>Svc: evaluateMilestone(projectId, milestoneId, rubricScores)
    Svc->>Svc: computeTotalMarks() & assignGrade()
    Svc->>EDAO: insert(evaluation)
    EDAO->>DB: INSERT INTO evaluations (...) VALUES (...)
    DB-->>EDAO: Evaluation Logged
    EDAO-->>Svc: Return true
    Svc-->>UI: Display Total Marks & Grade (e.g., 92.5%, Grade O)
    UI-->>Faculty: Output Evaluation Summary
```

---

## 5. STATE MACHINE DIAGRAM: MILESTONE LIFECYCLE

```mermaid
stateDiagram-v2
    [*] --> Pending : Milestone Defined
    Pending --> In_Progress : Work Commenced
    In_Progress --> Submitted : Student Submits Deliverables
    Submitted --> Approved : Review Committee Approves
    Submitted --> Revision_Required : Modifications Requested
    Revision_Required --> In_Progress : Resuming Corrections
    In_Progress --> Delayed : Due Date Exceeded
    Delayed --> Submitted : Late Deliverable Submitted
    Approved --> [*] : Milestone Completed
```
