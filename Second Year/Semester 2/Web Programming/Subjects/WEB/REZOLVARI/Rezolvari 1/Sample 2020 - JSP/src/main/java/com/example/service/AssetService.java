package com.example.service;

import com.example.model.Asset;
import com.example.model.DbManager;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AssetService {

    public static void addAssets(String assetList, int userid) throws JSONException {
        // split assetList
        System.out.println("assetList: " + assetList);
        String[] parts = assetList.split("}");

        for(String p: parts){
            p = p + "}";
            if(p.charAt(0) == ',')
                p=p.substring(1);
            System.out.println("p= " + p);
            JSONObject details  = new JSONObject(p);
            System.out.println(details.getString("name"));
            System.out.println(details.getString("description"));
            System.out.println(details.getString("value"));

            // insert each in table
            Connection connection = null;
            PreparedStatement statement = null;

            try {
                connection = DbManager.getConnection();

                String sql = "INSERT INTO assets( userid, name, description, value) VALUES (?, ?, ?, ?)";
                statement = connection.prepareStatement(sql);
                statement.setInt(1, userid);
                statement.setString(2, details.getString("name"));
                statement.setString(3, details.getString("description"));
                statement.setInt(4, details.getInt("value"));

                statement.execute();

            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }

        }

    }

    //todo - get id from session in controller
    public static List<Asset> getAssets(int userid){
        Connection connection = null;
        PreparedStatement statement = null;
        List<Asset> assets = new ArrayList<>();

        try {
            connection = DbManager.getConnection();

            //    // SELECT * FROM `articles` WHERE journalid = 1 and user = "mick";
            String sql = "SELECT * FROM assets WHERE userid = ?";
            statement = connection.prepareStatement(sql);
            statement.setInt(1, userid);

            statement.execute();

            ResultSet resultSet = statement.executeQuery();


            while (resultSet.next()) {
                //  id, userid, name, descr, value
                int Id = resultSet.getInt("id");
                int userId = resultSet.getInt("userid");
                String name = resultSet.getString("name");
                String descr = resultSet.getString("description");
                int value = resultSet.getInt("value");

                Asset a = new Asset(Id, userId, name, descr, value);
                assets.add(a);
            }
            resultSet.close();
            return assets;

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return new ArrayList<>();
    }

}
