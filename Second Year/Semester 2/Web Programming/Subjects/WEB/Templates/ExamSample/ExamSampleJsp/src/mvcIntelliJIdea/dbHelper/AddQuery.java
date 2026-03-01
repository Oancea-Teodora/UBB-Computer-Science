package mvcIntelliJIdea.dbHelper;

import mvcIntelliJIdea.model.Asset;

import java.sql.*;

public class AddQuery {
    private Statement stmt;
    private ResultSet result;
    private Connection con;

    public AddQuery() { connect(); }

    private void connect() {
        try {
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/exam?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC", "root", "");
            stmt = con.createStatement();
        } catch (Exception ex) {
            System.out.println("Connection error: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    public void doAdd(Asset asset) {
        String query = "INSERT INTO assets (userid, name, description, value)  VALUES (?, ?, ?, ?)";
        PreparedStatement ps;
        try {
            ps = con.prepareStatement(query);
            ps.setInt(1, asset.getUserId());
            ps.setString(2, asset.getName());
            ps.setString(3, asset.getDescription());
            ps.setInt(4, asset.getValue());

            //As long as we modify the DB, we shall use executeUpdate()
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
