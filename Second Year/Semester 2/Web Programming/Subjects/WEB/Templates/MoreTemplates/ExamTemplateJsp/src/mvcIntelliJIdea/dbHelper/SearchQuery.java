package mvcIntelliJIdea.dbHelper;

import mvcIntelliJIdea.model.Student;

import java.sql.*;

public class SearchQuery {
    private Statement stmt;
    private ResultSet result;
    private Connection con;

    public SearchQuery() { connect(); }

    private void connect() {
        try {
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/exam?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC", "root", "");
            stmt = con.createStatement();
        } catch (Exception ex) {
            System.out.println("Connection error: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    public void doSearch(String studentName) {
        //UPPER is being used for case sensitive
        String query = "SELECT * FROM students WHERE UPPER(name) LIKE ? ORDER BY id ASC";
        PreparedStatement ps;
        try {
            ps = con.prepareStatement(query);
            ps.setString(1, "%" + studentName.toUpperCase() + "%");

            this.result = ps.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public String getHTMLTable() {
        //Get the result from doRead and process it into a HTML table
        String table = "";
        table += "<table border=1>";
        table += "<thead> <tr> " +
                "<td>Id</td>" +
                "<td>Name</td>" +
                "<td>Grade</td>" +
                "<td>Update</td>" +
                "<td>Delete</td>" +
                "</tr> </thead>";
        table += "<tbody>";

        try {
            while (this.result.next()) {
                Student student = new Student();
                student.setId(this.result.getInt("id"));
                student.setName(this.result.getString("name"));
                student.setGrade(this.result.getInt("grade"));

                table += "<tr>";

                table += "<td>";
                table += student.getId();
                table += "</td>";

                table += "<td>";
                table += student.getName();
                table += "</td>";

                table += "<td>";
                table += student.getGrade();
                table += "</td>";

                table += "<td>";
                table += "<a style=\"text-decoration:none; color: green; text-align: center\" href=UpdateController?id=" + student.getId() +"> * </a>";
                table += "</td>";

                table += "<td>";
                table += "<a style=\"text-decoration:none; color: red; text-align: center\" href=DeleteController?id=" + student.getId() + " onclick=\"return confirm('Are you sure you want to delete this?')\"> * </a>";
                table += "</td>";

                table += "</tr>";
            }

        } catch (SQLException ex) {
            System.out.println("HTML table generate error: " + ex.getMessage());
            ex.printStackTrace();
        }

        table += "</tbody></table>";
        return table;
    }
}
