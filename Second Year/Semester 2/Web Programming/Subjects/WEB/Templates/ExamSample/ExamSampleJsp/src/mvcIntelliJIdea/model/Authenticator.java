package mvcIntelliJIdea.model;

import java.sql.*;

public class Authenticator {
    private Statement stmt;

    public Authenticator() {
        connect();
    }

    private void connect() {
        try {
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/exam?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC", "root", "");
            stmt = con.createStatement();
        } catch(Exception ex) {
            System.out.println("Connection error: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    public String authenticate(String username, String password) {
        ResultSet rs;
        String result = "error";
        try {
            rs = stmt.executeQuery("select id from users where username='" + username + "' and password='" + password+"'");
            if (rs.next()) {
                result = Integer.toString(((Number) rs.getObject(1)).intValue());
            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

}
