package mvcIntelliJIdea.dbHelper;

import mvcIntelliJIdea.model.Asset;
import mvcIntelliJIdea.model.User;

import java.sql.*;

public class ReadQuery {
    private Statement stmt;
    private ResultSet result;
    private Connection con;

    public ReadQuery() {
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

    public void doRead(int userId) {
        String query = "SELECT * FROM assets WHERE userid=? ORDER BY id ASC";
        PreparedStatement ps = null;
        try {
            ps = con.prepareStatement(query);
            ps.setInt(1, userId);

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
                "<td>Description</td>" +
                "<td>Value</td>" +
                "</tr> </thead>";
        table += "<tbody>";

        try {
            while (this.result.next()) {
                Asset asset = new Asset();
                asset.setId(this.result.getInt("id"));
                asset.setName(this.result.getString("name"));
                asset.setDescription(this.result.getString("description"));
                asset.setValue(this.result.getInt("value"));

                table += "<tr>";

                table += "<td>";
                table += asset.getId();
                table += "</td>";

                table += "<td>";
                table += asset.getName();
                table += "</td>";

                table += "<td>";
                table += asset.getDescription();
                table += "</td>";

                if (asset.getValue() > 10) {
                    table += "<td style='color:red;'>";
                    table += asset.getValue();
                    table += "</td>";
                } else {
                    table += "<td>";
                    table += asset.getValue();
                    table += "</td>";
                }

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
