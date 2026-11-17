"""
==============================================================================
DATA SCIENCE & EXPLORATORY DATA ANALYSIS (EDA) PIPELINE
Student Project and Skill Tracking System (SPSTS)
==============================================================================
Course: 25ITC31 - Foundations of Data Science
Student: Gokul . S (Reg. No: 732925ITR032) | Class: 25IT3A
Institution: Velalar College of Engineering and Technology, Erode - 12
Libraries: Pandas, NumPy, Matplotlib, Seaborn, SQLite3
==============================================================================
"""

import os
import sqlite3
import numpy as np
import pandas as pd
import matplotlib.pyplot as plt
import seaborn as sns

# Set aesthetics and plotting style
sns.set_theme(style="whitegrid", palette="muted")
plt.rcParams['font.sans-serif'] = 'DejaVu Sans'
plt.rcParams['figure.autolayout'] = True

BASE_DIR = os.path.dirname(os.path.dirname(os.path.abspath(__file__)))
DB_PATH = os.path.join(BASE_DIR, "database", "student_tracking.db")
VIZ_DIR = os.path.join(BASE_DIR, "visualizations")
OUTPUT_DIR = os.path.join(BASE_DIR, "sample_outputs")

os.makedirs(VIZ_DIR, exist_ok=True)
os.makedirs(OUTPUT_DIR, exist_ok=True)

def connect_database():
    """Establish connection to SQLite database."""
    if not os.path.exists(DB_PATH):
        raise FileNotFoundError(f"Database not found at: {DB_PATH}")
    return sqlite3.connect(DB_PATH)

def load_and_preprocess_data():
    """
    Extract data from SQLite database, clean, transform, and compute metrics.
    Satisfies Unit 3 (Data Wrangling) and Unit 4 (Data Cleaning & Transformation).
    """
    conn = connect_database()

    # Load relational tables
    df_students = pd.read_sql_query("SELECT * FROM students", conn)
    df_skills = pd.read_sql_query("SELECT * FROM skills", conn)
    df_student_skills = pd.read_sql_query("SELECT * FROM student_skills", conn)
    df_projects = pd.read_sql_query("SELECT * FROM projects", conn)
    df_milestones = pd.read_sql_query("SELECT * FROM milestones", conn)
    df_evaluations = pd.read_sql_query("SELECT * FROM evaluations", conn)
    conn.close()

    print("=== DATA RETRIEVAL SUMMARY ===")
    print(f"Students count:       {len(df_students)}")
    print(f"Skills catalog count: {len(df_skills)}")
    print(f"Student skills links: {len(df_student_skills)}")
    print(f"Projects count:       {len(df_projects)}")
    print(f"Milestones count:     {len(df_milestones)}")
    print(f"Evaluations count:    {len(df_evaluations)}")

    # ---------------------------------------------------------
    # Data Cleaning & Transformation (Unit 4)
    # ---------------------------------------------------------
    # 1. Missing Values Treatment
    df_milestones['submission_date_clean'] = df_milestones['submission_date'].fillna('Pending / Unsubmitted')
    df_milestones['remarks'] = df_milestones['remarks'].fillna('No remarks recorded')

    # Convert date strings to datetime objects
    df_milestones['due_date_dt'] = pd.to_datetime(df_milestones['due_date'])
    df_milestones['submission_date_dt'] = pd.to_datetime(df_milestones['submission_date'], errors='coerce')
    df_projects['start_date_dt'] = pd.to_datetime(df_projects['start_date'])

    # 2. Feature Engineering: Milestone Turnaround (Days difference)
    df_milestones['turnaround_days'] = (
        df_milestones['submission_date_dt'] - df_milestones['due_date_dt']
    ).dt.days

    # 3. Merge Datasets (Unit 3 Wrangling)
    # Student + Skills
    df_student_skill_detail = df_student_skills.merge(
        df_students, on="register_no"
    ).merge(
        df_skills, on="skill_id"
    )

    # Project + Milestone Aggregation
    milestone_agg = df_milestones.groupby('project_id').agg(
        total_milestones=('milestone_id', 'count'),
        approved_milestones=('status', lambda s: (s == 'Approved').sum()),
        delayed_milestones=('status', lambda s: (s == 'Delayed').sum()),
        pending_milestones=('status', lambda s: s.isin(['Pending', 'In Progress']).sum())
    ).reset_index()

    milestone_agg['milestone_completion_rate'] = (
        milestone_agg['approved_milestones'] / milestone_agg['total_milestones'] * 100.0
    ).round(2)

    df_projects_summary = df_projects.merge(milestone_agg, on="project_id", how="left")
    df_projects_summary['milestone_completion_rate'] = df_projects_summary['milestone_completion_rate'].fillna(0.0)

    # Evaluations Aggregation
    eval_agg = df_evaluations.groupby('project_id').agg(
        avg_score=('total_marks', 'mean'),
        min_score=('total_marks', 'min'),
        max_score=('total_marks', 'max'),
        eval_count=('evaluation_id', 'count')
    ).reset_index().round(2)

    df_projects_summary = df_projects_summary.merge(eval_agg, on="project_id", how="left")
    df_projects_summary['avg_score'] = df_projects_summary['avg_score'].fillna(0.0)

    return {
        "students": df_students,
        "skills": df_skills,
        "student_skills": df_student_skill_detail,
        "projects": df_projects_summary,
        "milestones": df_milestones,
        "evaluations": df_evaluations
    }

def generate_visualizations(data):
    """
    Generate and save all publication-quality visualizations (Unit 5 EDA).
    """
    df_projects = data["projects"]
    df_student_skills = data["student_skills"]
    df_milestones = data["milestones"]
    df_evaluations = data["evaluations"]

    # =========================================================================
    # VIZ 1: Project Completion Status Rate (Donut Chart)
    # =========================================================================
    plt.figure(figsize=(8, 8))
    status_counts = df_projects['completion_status'].value_counts()
    colors = ['#2ecc71', '#3498db', '#e74c3c', '#f1c40f']
    
    wedges, texts, autotexts = plt.pie(
        status_counts, 
        labels=status_counts.index, 
        autopct='%1.1f%%',
        startangle=140, 
        colors=colors[:len(status_counts)],
        wedgeprops=dict(width=0.4, edgecolor='white', linewidth=2),
        textprops=dict(size=12, weight='bold')
    )
    plt.title("Student Project Completion Status Distribution\nAcademic Year 2026-27 (Odd)", fontsize=14, weight='bold', pad=20)
    plt.tight_layout()
    viz1_path = os.path.join(VIZ_DIR, "project_completion_rate.png")
    plt.savefig(viz1_path, dpi=300)
    plt.close()
    print(f"[Saved] {viz1_path}")

    # =========================================================================
    # VIZ 2: Popular Technology Choices & Programming Languages (Horizontal Bar)
    # =========================================================================
    plt.figure(figsize=(10, 6))
    tech_counts = df_student_skills['skill_name'].value_counts().head(10)
    
    ax = sns.barplot(x=tech_counts.values, y=tech_counts.index, hue=tech_counts.index, palette="viridis", legend=False, edgecolor="black")
    plt.title("Top 10 Technical Skills & Technologies Chosen by Students", fontsize=14, weight='bold', pad=15)
    plt.xlabel("Number of Students Possessing Skill", fontsize=12, weight='bold')
    plt.ylabel("Technology / Programming Language", fontsize=12, weight='bold')
    
    # Add data value labels on bars
    for i, v in enumerate(tech_counts.values):
        ax.text(v + 0.1, i, str(v), color='black', va='center', weight='bold')

    plt.tight_layout()
    viz2_path = os.path.join(VIZ_DIR, "popular_technologies.png")
    plt.savefig(viz2_path, dpi=300)
    plt.close()
    print(f"[Saved] {viz2_path}")

    # =========================================================================
    # VIZ 3: Skill Proficiency Level Distribution (Grouped Bar Chart)
    # =========================================================================
    plt.figure(figsize=(12, 6))
    top_skills = df_student_skills['skill_name'].value_counts().head(8).index
    df_top_skills = df_student_skills[df_student_skills['skill_name'].isin(top_skills)]
    
    skill_prof_pivot = pd.crosstab(
        df_top_skills['skill_name'], 
        df_top_skills['proficiency_level']
    )[['Beginner', 'Intermediate', 'Advanced']].fillna(0)
    
    skill_prof_pivot.plot(
        kind='bar', 
        figsize=(12, 6), 
        color=['#f39c12', '#3498db', '#27ae60'], 
        edgecolor='black'
    )
    plt.title("Technical Skill Distribution by Proficiency Level", fontsize=14, weight='bold', pad=15)
    plt.xlabel("Technical Skill", fontsize=12, weight='bold')
    plt.ylabel("Number of Students", fontsize=12, weight='bold')
    plt.xticks(rotation=30, ha='right')
    plt.legend(title="Proficiency Tier", fontsize=11)
    plt.grid(axis='y', linestyle='--', alpha=0.7)
    plt.tight_layout()
    viz3_path = os.path.join(VIZ_DIR, "skill_distribution.png")
    plt.savefig(viz3_path, dpi=300)
    plt.close()
    print(f"[Saved] {viz3_path}")

    # =========================================================================
    # VIZ 4: Domain-wise Project Registration & Mean Scores (Dual Plot)
    # =========================================================================
    plt.figure(figsize=(11, 6))
    domain_order = df_projects['domain'].value_counts().index
    ax = sns.countplot(data=df_projects, y='domain', order=domain_order, hue='domain', palette="mako", legend=False, edgecolor="black")
    plt.title("Distribution of Student Mini-Projects Across Domains", fontsize=14, weight='bold', pad=15)
    plt.xlabel("Number of Registered Projects", fontsize=12, weight='bold')
    plt.ylabel("Domain / Specialization", fontsize=12, weight='bold')
    
    for i, p in enumerate(ax.patches):
        ax.annotate(f"{int(p.get_width())}", (p.get_width() + 0.05, p.get_y() + p.get_height() / 2),
                    ha='left', va='center', weight='bold')

    plt.tight_layout()
    viz4_path = os.path.join(VIZ_DIR, "domain_wise_projects.png")
    plt.savefig(viz4_path, dpi=300)
    plt.close()
    print(f"[Saved] {viz4_path}")

    # =========================================================================
    # VIZ 5: Evaluation Rubric Scores Distribution (Boxplot & Distribution)
    # =========================================================================
    fig, axes = plt.subplots(1, 2, figsize=(14, 6))
    
    # 5A: Total Marks KDE Distribution
    sns.histplot(df_evaluations['total_marks'], kde=True, ax=axes[0], color='#2980b9', bins=6)
    axes[0].set_title("Overall Project Evaluation Score Distribution (Max 100)", fontsize=12, weight='bold')
    axes[0].set_xlabel("Total Assessment Marks", fontsize=11, weight='bold')
    axes[0].set_ylabel("Frequency", fontsize=11, weight='bold')
    
    # 5B: Component-wise Box Plots
    rubric_cols = [
        'literature_survey_marks', 'system_design_marks', 
        'implementation_marks', 'testing_marks', 'presentation_marks'
    ]
    rubric_names = ['Survey\n(20)', 'Design\n(20)', 'Impl\n(30)', 'Testing\n(20)', 'Viva\n(10)']
    
    sns.boxplot(data=df_evaluations[rubric_cols], ax=axes[1], palette="Set2")
    axes[1].set_xticks(range(len(rubric_names)))
    axes[1].set_xticklabels(rubric_names, fontsize=10, weight='bold')
    axes[1].set_title("Component-Wise Assessment Mark Spread", fontsize=12, weight='bold')
    axes[1].set_ylabel("Marks Awarded", fontsize=11, weight='bold')

    plt.tight_layout()
    viz5_path = os.path.join(VIZ_DIR, "evaluation_marks_distribution.png")
    plt.savefig(viz5_path, dpi=300)
    plt.close()
    print(f"[Saved] {viz5_path}")

    # =========================================================================
    # VIZ 6: Project Milestone Progress Breakdown (Stacked Bar)
    # =========================================================================
    plt.figure(figsize=(12, 6))
    milestone_pivot = pd.crosstab(df_milestones['project_id'], df_milestones['status'])
    cols_order = [c for c in ['Approved', 'Submitted', 'In Progress', 'Delayed', 'Pending'] if c in milestone_pivot.columns]
    milestone_pivot = milestone_pivot[cols_order]
    
    palette_map = {
        'Approved': '#27ae60',
        'Submitted': '#2980b9',
        'In Progress': '#f39c12',
        'Delayed': '#c0392b',
        'Pending': '#95a5a6'
    }
    use_colors = [palette_map[c] for c in cols_order]
    
    ax = milestone_pivot.plot(kind='bar', stacked=True, color=use_colors, figsize=(12, 6), edgecolor='black')
    plt.title("Milestone Execution Status per Project (Project ID 101 to 110)", fontsize=14, weight='bold', pad=15)
    plt.xlabel("Project ID", fontsize=12, weight='bold')
    plt.ylabel("Milestone Count", fontsize=12, weight='bold')
    plt.xticks(rotation=0)
    plt.legend(title="Milestone Status", bbox_to_anchor=(1.02, 1), loc='upper left')
    plt.tight_layout()
    viz6_path = os.path.join(VIZ_DIR, "milestone_status_breakdown.png")
    plt.savefig(viz6_path, dpi=300)
    plt.close()
    print(f"[Saved] {viz6_path}")

def generate_summary_report(data):
    """
    Print and write statistical summary report.
    """
    df_projects = data["projects"]
    df_evaluations = data["evaluations"]
    df_skills = data["student_skills"]

    summary_file = os.path.join(OUTPUT_DIR, "python_execution_output.txt")
    with open(summary_file, "w", encoding="utf-8") as f:
        f.write("="*80 + "\n")
        f.write("PYTHON DATA SCIENCE & EDA SUMMARY REPORT\n")
        f.write("Student Project and Skill Tracking System\n")
        f.write("Course: 25ITC31 - Foundations of Data Science\n")
        f.write(f"Student: Gokul . S (Reg: 732925ITR032) | Class: 25IT3A\n")
        f.write("="*80 + "\n\n")

        f.write("1. COHORT PROJECT STATISTICS:\n")
        f.write(f"   Total Projects Registered:       {len(df_projects)}\n")
        completed_count = (df_projects['completion_status'] == 'Completed').sum()
        in_progress_count = (df_projects['completion_status'] == 'In Progress').sum()
        delayed_count = (df_projects['completion_status'] == 'Delayed').sum()
        f.write(f"   Completed Projects:              {completed_count} ({completed_count/len(df_projects)*100:.1f}%)\n")
        f.write(f"   In Progress Projects:            {in_progress_count} ({in_progress_count/len(df_projects)*100:.1f}%)\n")
        f.write(f"   Delayed Projects:                {delayed_count} ({delayed_count/len(df_projects)*100:.1f}%)\n")
        f.write(f"   Mean Milestone Completion Rate:  {df_projects['milestone_completion_rate'].mean():.2f}%\n\n")

        f.write("2. ASSESSMENT & EVALUATION METRICS (Max 100):\n")
        f.write(f"   Mean Total Score:                {df_evaluations['total_marks'].mean():.2f}\n")
        f.write(f"   Standard Deviation:              {df_evaluations['total_marks'].std():.2f}\n")
        f.write(f"   Median Score:                    {df_evaluations['total_marks'].median():.2f}\n")
        f.write(f"   Minimum Score:                   {df_evaluations['total_marks'].min():.2f}\n")
        f.write(f"   Maximum Score:                   {df_evaluations['total_marks'].max():.2f}\n")
        f.write(f"   Grade Distribution:\n")
        grade_dist = df_evaluations['grade'].value_counts()
        for grade, count in grade_dist.items():
            f.write(f"     - Grade {grade:<3}: {count} evaluations\n")
        f.write("\n")

        f.write("3. TOP 5 STUDENT TECHNICAL SKILLS:\n")
        top_skills = df_skills['skill_name'].value_counts().head(5)
        for skill, count in top_skills.items():
            f.write(f"     - {skill:<22}: {count} students\n")
        f.write("\n")
        f.write("="*80 + "\n")
        f.write("ALL 6 VISUALIZATION CHARTS GENERATED IN 'visualizations/' DIRECTORY.\n")
        f.write("="*80 + "\n")

    print(f"[Done] Summary written to: {summary_file}")

if __name__ == "__main__":
    print("Starting Python Data Science Analysis Pipeline...")
    data = load_and_preprocess_data()
    generate_visualizations(data)
    generate_summary_report(data)
    print("Analysis & Visualizations Completed Successfully!")
