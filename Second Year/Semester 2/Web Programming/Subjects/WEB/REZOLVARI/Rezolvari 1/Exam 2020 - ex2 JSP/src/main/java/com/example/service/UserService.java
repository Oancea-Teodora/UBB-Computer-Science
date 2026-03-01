package com.example.service;

import com.example.domain.DbManager;
import com.example.domain.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserService {
    public static User getUserByUsername(String username){
        Connection connection = null;
        PreparedStatement statement = null;

        try{
            connection = DbManager.getConnection();

            String sql = "select * from persons where name = ?";
            statement = connection.prepareStatement(sql);
            statement.setString(1, username);

            ResultSet resultSet = statement.executeQuery();

            if(!resultSet.next()){
                return null;
            }

            int id = resultSet.getInt("id");
            String uname = resultSet.getString("name");
            String gender = resultSet.getString("gender");
            int age = resultSet.getInt("age");

            User user = new User(id, uname, age, gender);

            resultSet.close();

            return user;

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return null;
    }

    public static Boolean checkCredentialsValid(String username){
        Connection connection = null;
        PreparedStatement statement = null;

        try{
            connection = DbManager.getConnection();

            String sql = "select * from persons where name = ?";
            statement = connection.prepareStatement(sql);
            statement.setString(1, username);

            ResultSet resultSet = statement.executeQuery();

            if(!resultSet.next()){
                return false;
            }

            resultSet.close();

            return true;

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return false;
    }
}
