package products.model;

import java.sql.*;

public class Authenticator {
    private Statement stmt;

    public Authenticator() {
        connect();
    }

    public void connect() {
        try {
            // http://localhost/phpmyadmin/index.php?route=/database/structure&db=transportation
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost/products", "root", "doareu123");
            stmt = con.createStatement();
        } catch(Exception ex) {
            System.out.println("Connect error:"+ex.getMessage());
            ex.printStackTrace();
        }
    }

    public String authenticate(String username, String password) {
        ResultSet rs;
        String result = "error";
        System.out.println(username+" "+password);
        try {
            rs = stmt.executeQuery("select * from users where username='"+username+"' and password='"+password+"'");
            if (rs.next()) {
                result = "success";
            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }
}
