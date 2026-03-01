package com.example.service;

import com.example.model.DbManager;
import com.example.model.Template;
import com.example.model.document;
import com.example.model.keyword;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class websiteService {

    public static void insertKeyword(String key, String value){
        Connection connection = null;
        PreparedStatement statement = null;

        try {
            connection = DbManager.getConnection();

            String sql = "INSERT INTO keyword(kkey, value) VALUES(?, ?)";
            statement = connection.prepareStatement(sql);
            statement.setString(1, key);
            statement.setString(2, value);

            statement.execute();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public static List<document> getDocs(String filter){
        //SELECT * FROM `document` WHERE title LIKE ?
        filter = "%" + filter + "%";
        Connection connection = null;
        PreparedStatement statement = null;
        List<document> documents = new ArrayList<>();

        try {
            connection = DbManager.getConnection();

            //System.out.println("userId = " +  userId);
            String sql = "SELECT * FROM `document` WHERE title LIKE ?";
            statement = connection.prepareStatement(sql);
            statement.setString(1, filter);

            statement.execute();

            ResultSet resultSet = statement.executeQuery();



            while (resultSet.next()) {
                int productId = resultSet.getInt("id");
                String name = resultSet.getString("title");
                String list = resultSet.getString("listOfTemplates");

                document d = new document(productId, name, list);
                documents.add(d);
            }
            resultSet.close();
            return documents;

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return new ArrayList<>();
    }

    public static List<keyword> getKeywords(){
        Connection connection = null;
        PreparedStatement statement = null;
        List<keyword> keywords = new ArrayList<>();

        try {
            connection = DbManager.getConnection();

            //System.out.println("userId = " +  userId);
            String sql = "SELECT * FROM `keyword`";
            statement = connection.prepareStatement(sql);

            statement.execute();

            ResultSet resultSet = statement.executeQuery();


            while (resultSet.next()) {
                int productId = resultSet.getInt("id");
                String name = resultSet.getString("kkey");
                String list = resultSet.getString("value");

                keyword k = new keyword(productId, name, list);
                keywords.add(k);
            }
            resultSet.close();
            return keywords;

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return new ArrayList<>();
    }

    public static List<Template> renderDocument(int documentId){
        // get list of templates from document
        // SELECT * FROM `document` WHERE id = ?
        Connection connection = null;
        PreparedStatement statement = null;

        try {
            connection = DbManager.getConnection();

            //System.out.println("userId = " +  userId);
            String sql = "SELECT * FROM `document` WHERE id = ?";
            statement = connection.prepareStatement(sql);
            statement.setInt(1, documentId);

            statement.execute();

            ResultSet resultSet = statement.executeQuery();
            resultSet.next();

            String list = resultSet.getString("listOfTemplates");
            System.out.println(list);

            resultSet.close();

            List<Template> templates = new ArrayList<>();
            String[] templateIDS = list.split(",");

            for(String s: templateIDS){
                // find the template and add it to the array
                sql = "SELECT * FROM `template` WHERE id = ?";
                statement = connection.prepareStatement(sql);
                statement.setString(1, s);

                statement.execute();

                resultSet = statement.executeQuery();
                resultSet.next();

                Template t = new Template(resultSet.getInt("ID"), resultSet.getString("name"), resultSet.getString("textContent"));
                templates.add(t);
            }
            return templates;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return new ArrayList<>();
    }
}
