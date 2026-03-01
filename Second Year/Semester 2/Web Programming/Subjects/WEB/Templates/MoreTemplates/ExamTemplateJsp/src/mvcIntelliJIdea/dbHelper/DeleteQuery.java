package mvcIntelliJIdea.dbHelper;

import java.sql.*;

public class DeleteQuery {
    private Statement stmt;
    private ResultSet result;
    private Connection con;

    public DeleteQuery() {
        connect();
    }

    private void connect() {
        try {
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/exam?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC", "root", "");
            stmt = con.createStatement();
        } catch (Exception ex) {
            System.out.println("Connection error: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    public void doDelete(int id) {
        String query = "DELETE FROM students WHERE id=?";
        PreparedStatement ps = null;
        try {
            ps = con.prepareStatement(query);
            ps.setInt(1, id);

            //As long as we modify the DB, we shall use executeUpdate()
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
