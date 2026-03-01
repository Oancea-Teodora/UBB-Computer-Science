package com.example.service;

import com.example.model.DbManager;
import com.example.model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class UserService {

    static List<String> fathers;

    private static String getFather(String child){
        Connection connection = null;
        PreparedStatement statement = null;

        try {
            connection = DbManager.getConnection();

            String sql = "select * from familyrelations where username = ? LIMIT 1";
            statement = connection.prepareStatement(sql);
            statement.setString(1, child);

            ResultSet resultSet = statement.executeQuery();
            if (!resultSet.next()) {
                return "";
            }

            String father = resultSet.getString("father");

            System.out.println("Father of: " + child +" is: " + father);
            resultSet.close();
            return father;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return "";
    }

    public static List<String> getFathers(String child){
        fathers = new ArrayList<>();
        System.out.println("in serv: " + child);
        String father = getFather(child);
        System.out.println("father: " + father);

        while(!Objects.equals(father, "")){
            fathers.add(father);
            child = father;
            father = getFather(child);
        }

        System.out.println("fathers:  " + fathers);
        return fathers;
    }

    public static void addParentRecord(String child, String mother, String father){
        Connection connection = null;
        PreparedStatement statement = null;

        try {
            connection = DbManager.getConnection();

            //System.out.println("userId = " +  userId);
            String sql = "INSERT INTO familyrelations( username, mother, father) VALUES (?, ?, ?)";
            statement = connection.prepareStatement(sql);
            statement.setString(1, child);
            statement.setString(2, mother);
            statement.setString(3, father);

            statement.execute();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public static User getUserByUsername(String username, String parent)
    /*
    flag = 0 => mother name
    flag = 1 => father name
     */
    {
        Connection connection = null;
        PreparedStatement statement = null;

        try{
            connection = DbManager.getConnection();

            String sql = "select * from familyrelations where username = ? and mother = ?";
            statement = connection.prepareStatement(sql);
            statement.setString(1, username);
            statement.setString(2, parent);

            ResultSet resultSet = statement.executeQuery();

            if(!resultSet.next()){
                String sql2 = "select * from familyrelations where username = ? and father = ?";
                statement = connection.prepareStatement(sql2);
                statement.setString(1, username);
                statement.setString(2, parent);

                ResultSet resultSet2 = statement.executeQuery();

                if(!resultSet2.next()){
                    return null;
                }

                String uname = resultSet2.getString("username");
                String mother = resultSet2.getString("mother");
                String father = resultSet2.getString("father");

                resultSet2.close();
                return new User(uname, mother, father);
            }
            else {

                String uname = resultSet.getString("username");
                String mother = resultSet.getString("mother");
                String father = resultSet.getString("father");

                resultSet.close();
                return new User(uname, mother, father);

            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return null;
    }

    public static Boolean checkCredentialsValid(String username, String parent){
        Connection connection = null;
        PreparedStatement statement = null;

        try{
            connection = DbManager.getConnection();

            String sql = "select * from familyrelations where username = ? and mother = ?";
            statement = connection.prepareStatement(sql);
            statement.setString(1, username);
            statement.setString(2, parent);

            ResultSet resultSet = statement.executeQuery();

            if(!resultSet.next()){
                String sql2 = "select * from familyrelations where username = ? and father = ?";
                statement = connection.prepareStatement(sql2);
                statement.setString(1, username);
                statement.setString(2, parent);

                ResultSet resultSet2 = statement.executeQuery();

                if(!resultSet2.next()){
                    return false;
                }
                else
                    resultSet2.close();
                return true;
            }
            else {
                return true;


            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return false;
    }
}
