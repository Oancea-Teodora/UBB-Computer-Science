package products.service;

import products.model.DbManager;
import products.model.User;

import java.sql.*;

public class UserService {


    public static User getUserByUsername(String username){
        Connection connection = null;
        PreparedStatement statement = null;

        try{
            connection = DbManager.getConnection();

            String sql = "select * from orders where user = ?";
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
