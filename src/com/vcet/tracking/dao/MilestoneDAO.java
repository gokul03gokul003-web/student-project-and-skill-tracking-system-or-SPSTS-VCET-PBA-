package com.vcet.tracking.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.vcet.tracking.exception.DatabaseException;
import com.vcet.tracking.model.Milestone;

/**
 * Data Access Object for Project Milestones.
 */
public class MilestoneDAO implements GenericDAO<Milestone, Integer> {

    @Override
    public boolean insert(Milestone m) throws DatabaseException {
        String sql = "INSERT INTO milestones (project_id, milestone_name, due_date, submission_date, status, remarks) VALUES (?, ?, ?, ?, ?, ?)";
        Connection conn = DatabaseConnection.getInstance().getConnection();
        try (PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setInt(1, m.getProjectId());
            pstmt.setString(2, m.getMilestoneName());
            pstmt.setString(3, m.getDueDate());
            pstmt.setString(4, m.getSubmissionDate());
            pstmt.setString(5, m.getStatus());
            pstmt.setString(6, m.getRemarks());

            int affected = pstmt.executeUpdate();
            if (affected > 0) {
                try (ResultSet rs = pstmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        m.setMilestoneId(rs.getInt(1));
                    }
                }
                return true;
            }
            return false;
        } catch (SQLException e) {
            throw new DatabaseException("Failed to insert milestone: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean update(Milestone m) throws DatabaseException {
        String sql = "UPDATE milestones SET milestone_name = ?, due_date = ?, submission_date = ?, status = ?, remarks = ? WHERE milestone_id = ?";
        Connection conn = DatabaseConnection.getInstance().getConnection();
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, m.getMilestoneName());
            pstmt.setString(2, m.getDueDate());
            pstmt.setString(3, m.getSubmissionDate());
            pstmt.setString(4, m.getStatus());
            pstmt.setString(5, m.getRemarks());
            pstmt.setInt(6, m.getMilestoneId());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DatabaseException("Failed to update milestone #" + m.getMilestoneId() + ": " + e.getMessage(), e);
        }
    }

    @Override
    public boolean delete(Integer milestoneId) throws DatabaseException {
        String sql = "DELETE FROM milestones WHERE milestone_id = ?";
        Connection conn = DatabaseConnection.getInstance().getConnection();
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, milestoneId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DatabaseException("Failed to delete milestone #" + milestoneId + ": " + e.getMessage(), e);
        }
    }

    @Override
    public Milestone findById(Integer milestoneId) throws DatabaseException {
        String sql = "SELECT * FROM milestones WHERE milestone_id = ?";
        Connection conn = DatabaseConnection.getInstance().getConnection();
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, milestoneId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToMilestone(rs);
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error retrieving milestone #" + milestoneId + ": " + e.getMessage(), e);
        }
        return null;
    }

    @Override
    public List<Milestone> findAll() throws DatabaseException {
        List<Milestone> list = new ArrayList<>();
        String sql = "SELECT * FROM milestones ORDER BY project_id, due_date ASC";
        Connection conn = DatabaseConnection.getInstance().getConnection();
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(mapResultSetToMilestone(rs));
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error retrieving milestones: " + e.getMessage(), e);
        }
        return list;
    }

    public List<Milestone> findByProject(int projectId) throws DatabaseException {
        List<Milestone> list = new ArrayList<>();
        String sql = "SELECT * FROM milestones WHERE project_id = ? ORDER BY due_date ASC";
        Connection conn = DatabaseConnection.getInstance().getConnection();
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, projectId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    list.add(mapResultSetToMilestone(rs));
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error retrieving milestones for project #" + projectId + ": " + e.getMessage(), e);
        }
        return list;
    }

    public boolean recordSubmission(int milestoneId, String submissionDate, String status, String remarks) throws DatabaseException {
        String sql = "UPDATE milestones SET submission_date = ?, status = ?, remarks = ? WHERE milestone_id = ?";
        Connection conn = DatabaseConnection.getInstance().getConnection();
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, submissionDate);
            pstmt.setString(2, status);
            pstmt.setString(3, remarks);
            pstmt.setInt(4, milestoneId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DatabaseException("Failed to record milestone submission: " + e.getMessage(), e);
        }
    }

    public List<Milestone> getDelayedMilestones() throws DatabaseException {
        List<Milestone> list = new ArrayList<>();
        String sql = "SELECT * FROM milestones WHERE status = 'Delayed' OR (status != 'Approved' AND due_date < DATE('now')) ORDER BY due_date ASC";
        Connection conn = DatabaseConnection.getInstance().getConnection();
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(mapResultSetToMilestone(rs));
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error fetching delayed milestones: " + e.getMessage(), e);
        }
        return list;
    }

    private Milestone mapResultSetToMilestone(ResultSet rs) throws SQLException {
        return new Milestone(
            rs.getInt("milestone_id"),
            rs.getInt("project_id"),
            rs.getString("milestone_name"),
            rs.getString("due_date"),
            rs.getString("submission_date"),
            rs.getString("status"),
            rs.getString("remarks")
        );
    }
}
