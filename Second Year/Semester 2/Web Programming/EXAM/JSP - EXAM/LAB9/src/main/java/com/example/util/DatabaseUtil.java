package com.example.util;

import com.example.model.*;
import java.sql.*;
import java.util.*;

public class DatabaseUtil {
    private static final String JDBC_URL = "jdbc:sqlserver://localhost:1433;databaseName=ReservationDb;encrypt=false";
    private static final String JDBC_USERNAME = "newuser";
    private static final String JDBC_PASSWORD = "StrongPassword123";

    static {
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(JDBC_URL, JDBC_USERNAME, JDBC_PASSWORD);
    }

    public static User authenticateUser(String username) {
        String sql = "SELECT id, username FROM [User] WHERE username = ?";
        try (Connection conn = getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new User(rs.getInt("id"), rs.getString("username"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static List<Task> getAllTasks() {
        List<Task> tasks = new ArrayList<>();
        String sql = "SELECT t.id, t.title, t.status, t.assignedTo, t.lastUpdated, t.lastUpdatedBy, u.username " +
                "FROM Task t LEFT JOIN [User] u ON t.lastUpdatedBy = u.id ORDER BY t.id";
        try (Connection conn = getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Task task = new Task();
                task.setId(rs.getInt("id"));
                task.setTitle(rs.getString("title"));
                task.setStatus(rs.getString("status"));
                task.setAssignedTo(rs.getInt("assignedTo"));
                task.setLastUpdated(rs.getTimestamp("lastUpdated"));
                task.setLastUpdatedBy(rs.getInt("lastUpdatedBy"));
                task.setLastUpdatedByUsername(rs.getString("username"));
                tasks.add(task);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tasks;
    }

    public static boolean updateTaskStatus(int taskId, String newStatus, int userId) {
        String selectSql = "SELECT status FROM Task WHERE id = ?";
        String updateSql = "UPDATE Task SET status = ?, lastUpdatedBy = ?, lastUpdated = GETDATE() WHERE id = ?";
        String logSql = "INSERT INTO TaskLog (taskId, userId, oldStatus, newStatus) VALUES (?, ?, ?, ?)";

        try (Connection conn = getConnection()) {
            conn.setAutoCommit(false);

            // Get old status
            String oldStatus = null;
            try (PreparedStatement selectStmt = conn.prepareStatement(selectSql)) {
                selectStmt.setInt(1, taskId);
                ResultSet rs = selectStmt.executeQuery();
                if (rs.next()) {
                    oldStatus = rs.getString("status");
                }
            }

            // Update task
            try (PreparedStatement updateStmt = conn.prepareStatement(updateSql)) {
                updateStmt.setString(1, newStatus);
                updateStmt.setInt(2, userId);
                updateStmt.setInt(3, taskId);
                updateStmt.executeUpdate();
            }

            // Log the change
            try (PreparedStatement logStmt = conn.prepareStatement(logSql)) {
                logStmt.setInt(1, taskId);
                logStmt.setInt(2, userId);
                logStmt.setString(3, oldStatus);
                logStmt.setString(4, newStatus);
                logStmt.executeUpdate();
            }

            conn.commit();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        String sql = "SELECT id, username FROM [User] ORDER BY username";
        try (Connection conn = getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                users.add(new User(rs.getInt("id"), rs.getString("username")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }
}