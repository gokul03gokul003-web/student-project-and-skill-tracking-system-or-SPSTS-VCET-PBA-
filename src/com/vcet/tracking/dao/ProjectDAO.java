package com.vcet.tracking.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.vcet.tracking.exception.DatabaseException;
import com.vcet.tracking.model.Project;

/**
 * Data Access Object for Project registry and domain tracking.
 * Demonstrates Unit 4: JDBC & SQL Execution.
 */
public class ProjectDAO implements GenericDAO<Project, Integer> {

    @Override
    public boolean insert(Project project) throws DatabaseException {
        String sql = "INSERT INTO projects (register_no, title, domain, description, technologies_used, start_date, completion_status) VALUES (?, ?, ?, ?, ?, ?, ?)";
        Connection conn = DatabaseConnection.getInstance().getConnection();
        try (PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, project.getRegisterNo());
            pstmt.setString(2, project.getTitle());
            pstmt.setString(3, project.getDomain());
            pstmt.setString(4, project.getDescription());
            pstmt.setString(5, project.getTechnologiesUsed());
            pstmt.setString(6, project.getStartDate());
            pstmt.setString(7, project.getCompletionStatus());
            
            int affected = pstmt.executeUpdate();
            if (affected > 0) {
                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        project.setProjectId(generatedKeys.getInt(1));
                    }
                }
                return true;
            }
            return false;
        } catch (SQLException e) {
            throw new DatabaseException("Failed to register project: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean update(Project project) throws DatabaseException {
        String sql = "UPDATE projects SET title = ?, domain = ?, description = ?, technologies_used = ?, start_date = ?, completion_status = ? WHERE project_id = ?";
        Connection conn = DatabaseConnection.getInstance().getConnection();
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, project.getTitle());
            pstmt.setString(2, project.getDomain());
            pstmt.setString(3, project.getDescription());
            pstmt.setString(4, project.getTechnologiesUsed());
            pstmt.setString(5, project.getStartDate());
            pstmt.setString(6, project.getCompletionStatus());
            pstmt.setInt(7, project.getProjectId());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DatabaseException("Failed to update project #" + project.getProjectId() + ": " + e.getMessage(), e);
        }
    }

    @Override
    public boolean delete(Integer projectId) throws DatabaseException {
        String sql = "DELETE FROM projects WHERE project_id = ?";
        Connection conn = DatabaseConnection.getInstance().getConnection();
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, projectId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DatabaseException("Failed to delete project #" + projectId + ": " + e.getMessage(), e);
        }
    }

    @Override
    public Project findById(Integer projectId) throws DatabaseException {
        String sql = "SELECT * FROM projects WHERE project_id = ?";
        Connection conn = DatabaseConnection.getInstance().getConnection();
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, projectId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToProject(rs);
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error retrieving project #" + projectId + ": " + e.getMessage(), e);
        }
        return null;
    }

    @Override
    public List<Project> findAll() throws DatabaseException {
        List<Project> list = new ArrayList<>();
        String sql = "SELECT * FROM projects ORDER BY project_id ASC";
        Connection conn = DatabaseConnection.getInstance().getConnection();
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(mapResultSetToProject(rs));
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error retrieving all projects: " + e.getMessage(), e);
        }
        return list;
    }

    public List<Project> findByStudent(String registerNo) throws DatabaseException {
        List<Project> list = new ArrayList<>();
        String sql = "SELECT * FROM projects WHERE register_no = ? ORDER BY project_id ASC";
        Connection conn = DatabaseConnection.getInstance().getConnection();
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, registerNo);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    list.add(mapResultSetToProject(rs));
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error retrieving projects for student: " + e.getMessage(), e);
        }
        return list;
    }

    public List<Project> findByDomain(String domain) throws DatabaseException {
        List<Project> list = new ArrayList<>();
        String sql = "SELECT * FROM projects WHERE domain = ? ORDER BY project_id ASC";
        Connection conn = DatabaseConnection.getInstance().getConnection();
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, domain);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    list.add(mapResultSetToProject(rs));
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error filtering projects by domain: " + e.getMessage(), e);
        }
        return list;
    }

    public boolean updateStatus(int projectId, String newStatus) throws DatabaseException {
        String sql = "UPDATE projects SET completion_status = ? WHERE project_id = ?";
        Connection conn = DatabaseConnection.getInstance().getConnection();
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, newStatus);
            pstmt.setInt(2, projectId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DatabaseException("Failed to update status for project #" + projectId + ": " + e.getMessage(), e);
        }
    }

    private Project mapResultSetToProject(ResultSet rs) throws SQLException {
        Project p = new Project(
            rs.getInt("project_id"),
            rs.getString("register_no"),
            rs.getString("title"),
            rs.getString("domain"),
            rs.getString("description"),
            rs.getString("technologies_used"),
            rs.getString("start_date"),
            rs.getString("completion_status")
        );
        p.setCreatedAt(rs.getString("created_at"));
        return p;
    }
}
