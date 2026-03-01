package com.example.Service;

import com.example.model.Article;
import com.example.model.DbManager;
import com.example.model.Journal;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class JournalService {

    private static void addJournal(String name){
        Connection connection = null;
        PreparedStatement statement = null;

        try {
            connection = DbManager.getConnection();

            System.out.println("adding: " + name);
            String sql = "INSERT INTO journal(name) values (?)";
            statement = connection.prepareStatement(sql);
            statement.setString(1, name);

            statement.execute();
            System.out.println("added: " + name);

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    private static int getJournal(String name) {
        Connection connection = null;
        PreparedStatement statement = null;

        try {
            connection = DbManager.getConnection();

            //System.out.println("userId = " +  userId);
            String checkJournal = "SELECT * FROM journal where name = ?";
            statement = connection.prepareStatement(checkJournal);
            statement.setString(1, name);

            ResultSet resultSet = statement.executeQuery();
            if (!resultSet.next()) {
                return -1;
            } else
                return resultSet.getInt("id");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return -1;
    }

        public static void insertArticle(String user, String journal, String summary){
        int i = (int) (new Date().getTime()/1000);
        Connection connection = null;
        PreparedStatement statement = null;

        try {
            connection = DbManager.getConnection();

            //System.out.println("userId = " +  userId);
            String checkJournal = "SELECT * FROM journal where name = ?";
            statement = connection.prepareStatement(checkJournal);
            statement.setString(1, journal);

            ResultSet resultSet = statement.executeQuery();
            if (!resultSet.next()) {
                addJournal(journal);
            }

            String sql = "INSERT INTO articles(user, journalid, summary, date) VALUES (?, ?, ?, ?)";
            statement = connection.prepareStatement(sql);
            statement.setString(1, user);
            System.out.println("Journal: " + getJournal(journal));
            statement.setInt(2, getJournal(journal));
            statement.setString(3, summary);
            statement.setInt(4, i);

            statement.execute();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public static List<Journal> getJournals(){
        Connection connection = null;
        PreparedStatement statement = null;
        List<Journal> journals = new ArrayList<>();

        try {
            connection = DbManager.getConnection();

            //System.out.println("userId = " +  userId);
            String sql = "SELECT * FROM journal";
            statement = connection.prepareStatement(sql);

            statement.execute();

            ResultSet resultSet = statement.executeQuery();



            while (resultSet.next()) {
                int productId = resultSet.getInt("id");
                String name = resultSet.getString("name");

                Journal j = new Journal(productId, name);
                journals.add(j);
            }
            resultSet.close();
            return journals;

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return new ArrayList<>();
    }

    public static List<Article> getArticles(String user, int journalId){
        Connection connection = null;
        PreparedStatement statement = null;
        List<Article> articles = new ArrayList<>();

        try {
            connection = DbManager.getConnection();

            //    // SELECT * FROM `articles` WHERE journalid = 1 and user = "mick";
            String sql = "SELECT * FROM articles WHERE user = ? and journalid = ?";
            statement = connection.prepareStatement(sql);
            statement.setString(1, user);
            statement.setInt(2, journalId);

            statement.execute();

            ResultSet resultSet = statement.executeQuery();



            while (resultSet.next()) {
                // id, user, jorunalid, summary, date
                int productId = resultSet.getInt("id");
                String username = resultSet.getString("user");
                String summary = resultSet.getString("summary");
                int journal = resultSet.getInt("journalid");
                int date = resultSet.getInt("date");

                Article a = new Article(productId, username, journal, summary, date);
                articles.add(a);
            }
            resultSet.close();
            return articles;

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return new ArrayList<>();
    }

    public static List<Article> getAllArticles(){
        Connection connection = null;
        PreparedStatement statement = null;
        List<Article> articles = new ArrayList<>();

        try {
            connection = DbManager.getConnection();

            //    // SELECT * FROM `articles` WHERE journalid = 1 and user = "mick";
            String sql = "SELECT * FROM articles ";
            statement = connection.prepareStatement(sql);

            statement.execute();

            ResultSet resultSet = statement.executeQuery();



            while (resultSet.next()) {
                // id, user, jorunalid, summary, date
                int productId = resultSet.getInt("id");
                String username = resultSet.getString("user");
                String summary = resultSet.getString("summary");
                int journal = resultSet.getInt("journalid");
                int date = resultSet.getInt("date");

                Article a = new Article(productId, username, journal, summary, date);
                articles.add(a);
            }
            resultSet.close();
            return articles;

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return new ArrayList<>();
    }
}
