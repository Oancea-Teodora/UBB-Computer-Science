package com.example.model;


import java.sql.*;

public class Authenticate {
    private Statement stmt;

    public Authenticate() {
        connect();
    }

    public void connect() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost/familytree", "root", "doareu123");
            stmt = con.createStatement();
        } catch(Exception ex) {
            System.out.println("Connect error:"+ex.getMessage());
            ex.printStackTrace();
        }
    }


}
