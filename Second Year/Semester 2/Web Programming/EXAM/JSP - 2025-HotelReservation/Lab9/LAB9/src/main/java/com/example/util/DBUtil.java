package com.example.util;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DBUtil {
    // SQL Server connection URL with SQL Authentication
    private static final String JDBC_URL = "jdbc:sqlserver://TEO:1433;databaseName=ReservationDb;encrypt=true;trustServerCertificate=true";

    // Using SQL Server 'sa' account or create a simple user
    private static final String USERNAME = "newuser";
    private static final String PASSWORD = "StrongPassword123"; // Replace with your actual sa password

    static {
        try {
            // Load the Microsoft SQL Server JDBC driver
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Cannot load SQL Server JDBC driver", e);
        }
    }

    /**
     * Obtain a Connection to the ReservationsDb database using SQL Authentication.
     */
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
    }

    // Hotel reservation functions
    public static List<Map<String, Object>> getAllDocuments() throws SQLException {
        List<Map<String, Object>> docs = new ArrayList<>();
        String sql = "SELECT * FROM Documents";

        try (Connection conn = getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Map<String, Object> doc = new HashMap<>();
                doc.put("id", rs.getInt("id"));
                doc.put("name", rs.getString("name"));
                doc.put("contents", rs.getString("contents"));
                docs.add(doc);
            }
        }
        return docs;
    }

    public static void insertDocument(String name, String contents) throws SQLException
    {
        String sql = "INSERT INTO Documents (name, contents) VALUES (? ,?)";
        try (Connection conn = getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, name);
            ps.setString(2, contents);
            ps.executeUpdate();
        }
    }


    public static void addDocumentToAuthors(String userId, String name, String content) {
        String sql = "UPDATE Authors SET documentlist = ? WHERE name = ?";
        int ID = getDocument(userId, name, content);
        String docList = getDocumentList(userId);
        docList = docList + "," + ID;
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, docList);
            ps.setString(2, userId);
            ps.executeUpdate();
        }
        catch (SQLException e) {
            throw new RuntimeException("Error adding document to authors", e);
        }

    }

    public static String getDocumentList(String userId) {
        String sql = "SELECT documentList FROM Authors WHERE name = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, userId);
            ResultSet rs = ps.executeQuery();
            return rs.next() ? rs.getString("documentList") : null;
        }
        catch (SQLException e) {
            throw new RuntimeException("Error retrieving document list", e);
        }
    }

    public static int getDocument(String userId, String name, String content) {
        String sql = "SELECT ID FROM Documents WHERE name = ? AND contents = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, name);
            ps.setString(2, content);
            ResultSet rs = ps.executeQuery();
            return rs.next() ? rs.getInt(1) : 0;
        }
        catch (SQLException e) {
            throw new RuntimeException("Error adding document to authors", e);
        }

    }

    public static List<String> findDocumentsMovies(String userName) {
        List<String> files = new ArrayList<>();
        List<String> docsId = new ArrayList<>();
        List<String> movId = new ArrayList<>();

        String sql = "SELECT documentList, movieList FROM Authors WHERE name = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, userName);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                String docList = rs.getString("documentList");
                String movieList = rs.getString("movieList");

                if (docList != null) {
                    docsId = (List.of(docList.split(",")));
                }
                if (movieList != null) {
                    movId = (List.of(movieList.split(",")));
                }

                int i=0, j=0;
                while (i < docsId.size() || j < movId.size()) {
                    if (i < docsId.size()) {
                        String docName = getDocumentName(Integer.parseInt(docsId.get(i)));
                        if (docName != null) {
                            files.add(docName);
                        }
                        i++;
                    }
                    if (j < movId.size()) {
                        String movieName = getMovieName(Integer.parseInt(movId.get(j)));
                        if (movieName != null) {
                            files.add(movieName);
                        }
                        j++;
                    }
                }

            }
            return files;
        }
        catch (SQLException e) {
            throw new RuntimeException("Error", e);
        }
    }

    public static String getDocumentName(int id) {
        String sql = "SELECT name FROM Documents WHERE id = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            return rs.next() ? rs.getString("name") : null;
        } catch (SQLException e) {
            throw new RuntimeException("Error retrieving document name", e);
        }
    }

    public static String getMovieName(int id) {
        String sql = "SELECT title FROM Movies WHERE id = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            return rs.next() ? rs.getString("title") : null;
        } catch (SQLException e) {
            throw new RuntimeException("Error retrieving movie name", e);
        }
    }

    public static List<Map<String, Object>> getAllMovies() throws SQLException {
        List<Map<String, Object>> movies = new ArrayList<>();
        String sql = "SELECT * FROM Movies";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Map<String, Object> movie = new HashMap<>();
                movie.put("id", rs.getInt("id"));
                movie.put("title", rs.getString("title"));
                movie.put("duration", rs.getInt("duration"));
                movies.add(movie);
            }
        }
        return movies;
    }

    public static void deleteMovie(String title) throws SQLException
    {
        String sql = "DELETE FROM Movies WHERE title = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, title);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting movie", e);
        }
    }

    public static String getDocumentWithMostAuthors() {
        // We wrap documentList in commas so that searching for ",3," won't match 13 or 30
        String sql =
                "SELECT TOP 1 d.name                                   \n" +
                        "FROM Documents d                                      \n" +
                        "LEFT JOIN Authors a                                   \n" +
                        "  ON ',' + ISNULL(a.documentList,'') + ','           \n" +
                        "     LIKE '%,' + CAST(d.id AS VARCHAR(20)) + ',%'     \n" +
                        "GROUP BY d.name                                       \n" +
                        "ORDER BY COUNT(a.id) DESC";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            return rs.next()
                    ? rs.getString(1)   // the d.name column
                    : null;
        } catch (SQLException e) {
            throw new RuntimeException("Error retrieving document with most authors", e);
        }
    }

}
