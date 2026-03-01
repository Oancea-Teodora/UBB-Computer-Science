package com.example.Service;

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

            String sql = "select * from articles where user = ?";
            statement = connection.prepareStatement(sql);
            statement.setString(1, username);

            ResultSet resultSet = statement.executeQuery();

//            if(!resultSet.next()){
//                // create new user
//            }

            //Integer id = resultSet.getInt("id");
            //String password = resultSet.getString("password");

            User user = new User(username);

            resultSet.close();

            return user;

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return null;
    }
}
