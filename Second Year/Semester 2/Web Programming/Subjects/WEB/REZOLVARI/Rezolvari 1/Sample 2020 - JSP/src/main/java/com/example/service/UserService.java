package com.example.service;


import com.example.model.DbManager;
import com.example.model.User;

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

            String sql = "select * from users where username = ?";
            statement = connection.prepareStatement(sql);
            statement.setString(1, username);

            ResultSet resultSet = statement.executeQuery();

            if(!resultSet.next()){
                return null;
            }

            Integer id = resultSet.getInt("id");
            String password = resultSet.getString("password");

            User user = new User(username, password);
            user.setId(id);

            resultSet.close();

            return user;

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return null;
    }

    public static User getUserById(Integer Id){
        Connection connection = null;
        PreparedStatement statement = null;

        try{
            connection = DbManager.getConnection();

            String sql = "select * from users where id = ?";
            statement = connection.prepareStatement(sql);
            statement.setInt(1, Id);

            ResultSet resultSet = statement.executeQuery();

            if(!resultSet.next()){
                return null;
            }

            String username = resultSet.getString("username");
            String password = resultSet.getString("password");

            User user = new User(username, password);
            user.setId(Id);

            resultSet.close();

            return user;

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return null;
    }

    public static Boolean checkUsernameUnique(String username){
        Connection connection = null;
        PreparedStatement statement = null;

        try{
            connection = DbManager.getConnection();

            String sql = "select * from users where username = ?";
            statement = connection.prepareStatement(sql);
            statement.setString(1, username);

            ResultSet resultSet = statement.executeQuery();

            if(resultSet.next()){
                return false;
            }
            return true;

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return false;
    }

    public static Boolean checkCredentialsValid(String username, String password){
        Connection connection = null;
        PreparedStatement statement = null;

        try{
            connection = DbManager.getConnection();

            String sql = "select * from Users where username = ?";
            statement = connection.prepareStatement(sql);
            statement.setString(1, username);

            ResultSet resultSet = statement.executeQuery();

            if(!resultSet.next()){
                return false;
            }

            String user_password = resultSet.getString("password");

            if(password.equals(user_password))
                return true;

            resultSet.close();

            return false;

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return false;
    }
}
