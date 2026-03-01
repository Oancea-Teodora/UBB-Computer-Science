package mvcIntelliJIdea.dbHelper;

import mvcIntelliJIdea.model.Student;

import java.sql.*;

public class ReadRecord {
    private Statement stmt;
    private ResultSet result;
    private Connection con;
    private Student student = new Student();
    private int id;

    public ReadRecord(int id) {
        this.id = id;
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

    public void doRead() {
        String query = "SELECT * FROM students WHERE id=?";
        PreparedStatement ps = null;
        try {
            ps = con.prepareStatement(query);

            ps.setInt(1, id);

            this.result = ps.executeQuery();
            this.result.next();

            student.setId(this.result.getInt("id"));
            student.setName(this.result.getString("name"));
            student.setGrade(this.result.getInt("grade"));

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Student getStudent() {
        return student;
    }
}
