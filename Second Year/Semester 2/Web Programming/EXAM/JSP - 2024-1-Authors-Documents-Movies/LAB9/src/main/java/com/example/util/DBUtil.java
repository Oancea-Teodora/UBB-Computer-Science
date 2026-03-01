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
    public static List<Map<String, Object>> findAvailableRooms(Date checkIn, Date checkOut) throws SQLException {
        List<Map<String, Object>> rooms = new ArrayList<>();
        String sql = "SELECT id, roomNumber, capacity, basePrice FROM HotelRoom WHERE id NOT IN " +
                "(SELECT roomId FROM Reservation WHERE checkInDate < ? AND checkOutDate > ?)";

        try (Connection conn = getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDate(1, checkOut);
            ps.setDate(2, checkIn);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Map<String, Object> room = new HashMap<>();
                room.put("id", rs.getInt("id"));
                room.put("roomNumber", rs.getInt("roomNumber"));
                room.put("capacity", rs.getInt("capacity"));
                room.put("basePrice", rs.getInt("basePrice"));
                rooms.add(room);
            }
        }
        return rooms;
    }

    public static int countGuestsOn(Date date) throws SQLException {
        String sql = "SELECT SUM(numberOfGuests) FROM Reservation WHERE checkInDate <= ? AND checkOutDate > ?";
        try (Connection conn = getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDate(1, date);
            ps.setDate(2, date);
            ResultSet rs = ps.executeQuery();
            return rs.next() ? rs.getInt(1) : 0;
        }
    }

    public static BigDecimal computePrice(int roomId, Date checkIn, Date checkOut) throws SQLException {
        String sql = "SELECT basePrice FROM HotelRoom WHERE id = ?";
        try (Connection conn = getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, roomId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                int basePrice = rs.getInt("basePrice");
                long days = (checkOut.getTime() - checkIn.getTime()) / (1000 * 60 * 60 * 24);

                // Simple pricing logic from exam requirements
                int totalReservations = countReservationsForDateRange(checkIn, checkOut);
                double multiplier = 1.0;
                if (totalReservations > 5)
                    multiplier = 1.5; // >50% booked
                else if (totalReservations > 2)
                    multiplier = 1.2; // >20% booked

                return new BigDecimal(basePrice * days * multiplier);
            }
        }
        return BigDecimal.ZERO;
    }

    private static int countReservationsForDateRange(Date checkIn, Date checkOut) throws SQLException {
        String sql = "SELECT COUNT(*) FROM Reservation WHERE checkInDate < ? AND checkOutDate > ?";
        try (Connection conn = getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDate(1, checkOut);
            ps.setDate(2, checkIn);
            ResultSet rs = ps.executeQuery();
            return rs.next() ? rs.getInt(1) : 0;
        }
    }

    public static boolean hasOverlap(int userId, Date checkIn, Date checkOut) throws SQLException {
        String sql = "SELECT COUNT(*) FROM Reservation WHERE userId = ? AND checkInDate < ? AND checkOutDate > ?";
        try (Connection conn = getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setDate(2, checkOut);
            ps.setDate(3, checkIn);
            ResultSet rs = ps.executeQuery();
            return rs.next() && rs.getInt(1) > 0;
        }
    }

    public static void insertReservation(int userId, int roomId, Date checkIn, Date checkOut, int guests,
            BigDecimal price) throws SQLException {
        String sql = "INSERT INTO Reservation (userId, roomId, checkInDate, checkOutDate, numberOfGuests, totalPrice) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setInt(2, roomId);
            ps.setDate(3, checkIn);
            ps.setDate(4, checkOut);
            ps.setInt(5, guests);
            ps.setInt(6, price.intValue());
            ps.executeUpdate();
        }
    }

    public static List<Map<String, Object>> findReservationsByUser(int userId) throws SQLException {
        List<Map<String, Object>> reservations = new ArrayList<>();
        String sql = "SELECT r.checkInDate, r.checkOutDate, r.numberOfGuests, r.totalPrice, h.roomNumber " +
                "FROM Reservation r JOIN HotelRoom h ON r.roomId = h.id WHERE r.userId = ?";

        try (Connection conn = getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Map<String, Object> res = new HashMap<>();
                res.put("roomNumber", rs.getInt("roomNumber"));
                res.put("checkInDate", rs.getDate("checkInDate"));
                res.put("checkOutDate", rs.getDate("checkOutDate"));
                res.put("numberOfGuests", rs.getInt("numberOfGuests"));
                res.put("totalPrice", rs.getInt("totalPrice"));
                reservations.add(res);
            }
        }
        return reservations;
    }

    public static int getUserId(String username) throws SQLException {
        String sql = "SELECT id FROM users WHERE username = ?";
        try (Connection conn = getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            return rs.next() ? rs.getInt("id") : -1;
        }
    }
}
