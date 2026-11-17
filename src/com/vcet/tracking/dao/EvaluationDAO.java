package com.vcet.tracking.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.vcet.tracking.exception.DatabaseException;
import com.vcet.tracking.model.Evaluation;

/**
 * Data Access Object for Project Rubric Evaluations and Grade computations.
 */
public class EvaluationDAO implements GenericDAO<Evaluation, Integer> {

    @Override
    public boolean insert(Evaluation e) throws DatabaseException {
        String sql = "INSERT INTO evaluations (project_id, milestone_id, literature_survey_marks, " +
                     "system_design_marks, implementation_marks, testing_marks, presentation_marks, grade, evaluated_date) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        Connection conn = DatabaseConnection.getInstance().getConnection();
        try (PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setInt(1, e.getProjectId());
            pstmt.setInt(2, e.getMilestoneId());
            pstmt.setDouble(3, e.getLiteratureSurveyMarks());
            pstmt.setDouble(4, e.getSystemDesignMarks());
            pstmt.setDouble(5, e.getImplementationMarks());
            pstmt.setDouble(6, e.getTestingMarks());
            pstmt.setDouble(7, e.getPresentationMarks());
            pstmt.setString(8, e.getGrade());
            pstmt.setString(9, e.getEvaluatedDate());

            int affected = pstmt.executeUpdate();
            if (affected > 0) {
                try (ResultSet rs = pstmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        e.setEvaluationId(rs.getInt(1));
                    }
                }
                return true;
            }
            return false;
        } catch (SQLException ex) {
            throw new DatabaseException("Failed to insert evaluation record: " + ex.getMessage(), ex);
        }
    }

    @Override
    public boolean update(Evaluation e) throws DatabaseException {
        String sql = "UPDATE evaluations SET literature_survey_marks = ?, system_design_marks = ?, " +
                     "implementation_marks = ?, testing_marks = ?, presentation_marks = ?, grade = ?, evaluated_date = ? " +
                     "WHERE evaluation_id = ?";
        Connection conn = DatabaseConnection.getInstance().getConnection();
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setDouble(1, e.getLiteratureSurveyMarks());
            pstmt.setDouble(2, e.getSystemDesignMarks());
            pstmt.setDouble(3, e.getImplementationMarks());
            pstmt.setDouble(4, e.getTestingMarks());
            pstmt.setDouble(5, e.getPresentationMarks());
            pstmt.setString(6, e.getGrade());
            pstmt.setString(7, e.getEvaluatedDate());
            pstmt.setInt(8, e.getEvaluationId());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException ex) {
            throw new DatabaseException("Failed to update evaluation #" + e.getEvaluationId() + ": " + ex.getMessage(), ex);
        }
    }

    @Override
    public boolean delete(Integer evaluationId) throws DatabaseException {
        String sql = "DELETE FROM evaluations WHERE evaluation_id = ?";
        Connection conn = DatabaseConnection.getInstance().getConnection();
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, evaluationId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException ex) {
            throw new DatabaseException("Failed to delete evaluation #" + evaluationId + ": " + ex.getMessage(), ex);
        }
    }

    @Override
    public Evaluation findById(Integer evaluationId) throws DatabaseException {
        String sql = "SELECT * FROM evaluations WHERE evaluation_id = ?";
        Connection conn = DatabaseConnection.getInstance().getConnection();
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, evaluationId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToEvaluation(rs);
                }
            }
        } catch (SQLException ex) {
            throw new DatabaseException("Error finding evaluation #" + evaluationId + ": " + ex.getMessage(), ex);
        }
        return null;
    }

    @Override
    public List<Evaluation> findAll() throws DatabaseException {
        List<Evaluation> list = new ArrayList<>();
        String sql = "SELECT * FROM evaluations ORDER BY project_id, evaluated_date DESC";
        Connection conn = DatabaseConnection.getInstance().getConnection();
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(mapResultSetToEvaluation(rs));
            }
        } catch (SQLException ex) {
            throw new DatabaseException("Error retrieving evaluations list: " + ex.getMessage(), ex);
        }
        return list;
    }

    public List<Evaluation> findByProject(int projectId) throws DatabaseException {
        List<Evaluation> list = new ArrayList<>();
        String sql = "SELECT * FROM evaluations WHERE project_id = ? ORDER BY evaluated_date ASC";
        Connection conn = DatabaseConnection.getInstance().getConnection();
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, projectId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    list.add(mapResultSetToEvaluation(rs));
                }
            }
        } catch (SQLException ex) {
            throw new DatabaseException("Error retrieving evaluations for project #" + projectId + ": " + ex.getMessage(), ex);
        }
        return list;
    }

    public Map<String, Double> getAverageMarksByDomain() throws DatabaseException {
        Map<String, Double> map = new HashMap<>();
        String sql = "SELECT p.domain, ROUND(AVG(e.total_marks), 2) AS avg_score " +
                     "FROM projects p " +
                     "JOIN evaluations e ON p.project_id = e.project_id " +
                     "GROUP BY p.domain";
        Connection conn = DatabaseConnection.getInstance().getConnection();
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                map.put(rs.getString("domain"), rs.getDouble("avg_score"));
            }
        } catch (SQLException ex) {
            throw new DatabaseException("Error aggregating average marks by domain: " + ex.getMessage(), ex);
        }
        return map;
    }

    private Evaluation mapResultSetToEvaluation(ResultSet rs) throws SQLException {
        Evaluation e = new Evaluation(
            rs.getInt("evaluation_id"),
            rs.getInt("project_id"),
            rs.getInt("milestone_id"),
            rs.getDouble("literature_survey_marks"),
            rs.getDouble("system_design_marks"),
            rs.getDouble("implementation_marks"),
            rs.getDouble("testing_marks"),
            rs.getDouble("presentation_marks"),
            rs.getString("evaluated_date")
        );
        e.setTotalMarks(rs.getDouble("total_marks"));
        e.setGrade(rs.getString("grade"));
        return e;
    }
}
