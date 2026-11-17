package com.vcet.tracking.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.vcet.tracking.exception.DatabaseException;
import com.vcet.tracking.model.Skill;
import com.vcet.tracking.model.Student;

/**
 * Data Access Object for Student and Skill management.
 * Demonstrates Unit 4: JDBC Queries, Unit 5: Collections Framework (ArrayList).
 */
public class StudentDAO implements GenericDAO<Student, String> {

    @Override
    public boolean insert(Student student) throws DatabaseException {
        String sql = "INSERT INTO students (register_no, name, department, class_section, email, phone) VALUES (?, ?, ?, ?, ?, ?)";
        Connection conn = DatabaseConnection.getInstance().getConnection();
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, student.getRegisterNo());
            pstmt.setString(2, student.getName());
            pstmt.setString(3, student.getDepartment());
            pstmt.setString(4, student.getClassSection());
            pstmt.setString(5, student.getEmail());
            pstmt.setString(6, student.getPhone());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DatabaseException("Failed to insert student record [" + student.getRegisterNo() + "]: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean update(Student student) throws DatabaseException {
        String sql = "UPDATE students SET name = ?, department = ?, class_section = ?, email = ?, phone = ? WHERE register_no = ?";
        Connection conn = DatabaseConnection.getInstance().getConnection();
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, student.getName());
            pstmt.setString(2, student.getDepartment());
            pstmt.setString(3, student.getClassSection());
            pstmt.setString(4, student.getEmail());
            pstmt.setString(5, student.getPhone());
            pstmt.setString(6, student.getRegisterNo());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DatabaseException("Failed to update student [" + student.getRegisterNo() + "]: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean delete(String registerNo) throws DatabaseException {
        String sql = "DELETE FROM students WHERE register_no = ?";
        Connection conn = DatabaseConnection.getInstance().getConnection();
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, registerNo);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DatabaseException("Failed to delete student [" + registerNo + "]: " + e.getMessage(), e);
        }
    }

    @Override
    public Student findById(String registerNo) throws DatabaseException {
        String sql = "SELECT * FROM students WHERE register_no = ?";
        Connection conn = DatabaseConnection.getInstance().getConnection();
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, registerNo);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    Student s = mapResultSetToStudent(rs);
                    s.setSkills(getStudentSkills(registerNo));
                    return s;
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error finding student by register number: " + e.getMessage(), e);
        }
        return null;
    }

    @Override
    public List<Student> findAll() throws DatabaseException {
        List<Student> list = new ArrayList<>();
        String sql = "SELECT * FROM students ORDER BY register_no ASC";
        Connection conn = DatabaseConnection.getInstance().getConnection();
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Student s = mapResultSetToStudent(rs);
                s.setSkills(getStudentSkills(s.getRegisterNo()));
                list.add(s);
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error retrieving student list: " + e.getMessage(), e);
        }
        return list;
    }

    public List<Skill> getStudentSkills(String registerNo) throws DatabaseException {
        List<Skill> skills = new ArrayList<>();
        String sql = "SELECT sk.skill_id, sk.skill_name, sk.category, ss.proficiency_level, ss.experience_years, ss.certified " +
                     "FROM student_skills ss " +
                     "JOIN skills sk ON ss.skill_id = sk.skill_id " +
                     "WHERE ss.register_no = ? ORDER BY sk.skill_name ASC";
        Connection conn = DatabaseConnection.getInstance().getConnection();
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, registerNo);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Skill sk = new Skill(
                        rs.getInt("skill_id"),
                        rs.getString("skill_name"),
                        rs.getString("category"),
                        rs.getString("proficiency_level"),
                        rs.getInt("experience_years"),
                        rs.getInt("certified") == 1
                    );
                    skills.add(sk);
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error retrieving student skills: " + e.getMessage(), e);
        }
        return skills;
    }

    public boolean addStudentSkill(String registerNo, int skillId, String proficiency, int expYears, boolean certified) throws DatabaseException {
        String sql = "INSERT INTO student_skills (register_no, skill_id, proficiency_level, experience_years, certified) VALUES (?, ?, ?, ?, ?)";
        Connection conn = DatabaseConnection.getInstance().getConnection();
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, registerNo);
            pstmt.setInt(2, skillId);
            pstmt.setString(3, proficiency);
            pstmt.setInt(4, expYears);
            pstmt.setInt(5, certified ? 1 : 0);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DatabaseException("Failed to associate skill to student: " + e.getMessage(), e);
        }
    }

    public List<Skill> getAllSkillsMaster() throws DatabaseException {
        List<Skill> skills = new ArrayList<>();
        String sql = "SELECT * FROM skills ORDER BY category, skill_name";
        Connection conn = DatabaseConnection.getInstance().getConnection();
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                skills.add(new Skill(
                    rs.getInt("skill_id"),
                    rs.getString("skill_name"),
                    rs.getString("category")
                ));
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error retrieving master skills catalog: " + e.getMessage(), e);
        }
        return skills;
    }

    private Student mapResultSetToStudent(ResultSet rs) throws SQLException {
        Student s = new Student(
            rs.getString("register_no"),
            rs.getString("name"),
            rs.getString("class_section"),
            rs.getString("email"),
            rs.getString("phone")
        );
        s.setDepartment(rs.getString("department"));
        s.setCreatedAt(rs.getString("created_at"));
        return s;
    }
}
