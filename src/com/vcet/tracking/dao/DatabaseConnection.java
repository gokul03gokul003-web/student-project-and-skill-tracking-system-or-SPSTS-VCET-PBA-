package com.vcet.tracking.dao;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import com.vcet.tracking.exception.DatabaseException;

/**
 * Singleton Database Connection Manager for SQLite JDBC.
 * Demonstrates Unit 1: Static Members, Unit 4: Database Connection (JDBC).
 */
public class DatabaseConnection {
    private static DatabaseConnection instance;
    private Connection connection;

    private static final String DEFAULT_DB_PATH = "database" + File.separator + "student_tracking.db";
    private String dbUrl;

    private DatabaseConnection() throws DatabaseException {
        try {
            // Find relative or absolute path to database
            File dbFile = new File(DEFAULT_DB_PATH);
            if (!dbFile.exists()) {
                // Check parent directory
                dbFile = new File(".." + File.separator + DEFAULT_DB_PATH);
            }
            this.dbUrl = "jdbc:sqlite:" + dbFile.getAbsolutePath();
            
            // Explicitly load SQLite JDBC driver class if available
            try {
                Class.forName("org.sqlite.JDBC");
            } catch (ClassNotFoundException e) {
                // SQLite driver may be registered automatically via ServiceLoader
            }
            
            this.connection = DriverManager.getConnection(this.dbUrl);
            // Enable foreign key constraints in SQLite
            this.connection.createStatement().execute("PRAGMA foreign_keys = ON;");
        } catch (SQLException e) {
            throw new DatabaseException("Failed to establish SQLite database connection: " + e.getMessage(), e);
        }
    }

    public static synchronized DatabaseConnection getInstance() throws DatabaseException {
        if (instance == null || instance.isClosed()) {
            instance = new DatabaseConnection();
        }
        return instance;
    }

    public Connection getConnection() throws DatabaseException {
        try {
            if (connection == null || connection.isClosed()) {
                connection = DriverManager.getConnection(this.dbUrl);
                connection.createStatement().execute("PRAGMA foreign_keys = ON;");
            }
            return connection;
        } catch (SQLException e) {
            throw new DatabaseException("Error retrieving active connection: " + e.getMessage(), e);
        }
    }

    public boolean isClosed() {
        try {
            return connection == null || connection.isClosed();
        } catch (SQLException e) {
            return true;
        }
    }

    public void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            System.err.println("Warning: Error closing database connection: " + e.getMessage());
        }
    }
}
