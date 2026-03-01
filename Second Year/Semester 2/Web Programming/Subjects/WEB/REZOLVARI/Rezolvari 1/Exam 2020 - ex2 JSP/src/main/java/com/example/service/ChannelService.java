package com.example.service;

import com.example.domain.Channel;
import com.example.domain.DbManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import com.example.domain.Subscription;
import com.example.domain.User;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.simple.JSONArray;

public class ChannelService {

    public static boolean checkUserSubscribes(List<Subscription> subscriber, String username) throws JSONException {

        for(Subscription s: subscriber)
            if(Objects.equals(s.getName(), username))
                return true;
        return false;
    }

    public static String updateUser(String username, List<Subscription> subscriptions, int date) throws JSONException {

        JSONArray jsonArray = new JSONArray();

        for(Subscription sub: subscriptions){
            if(Objects.equals(sub.getName(), username))
                sub.setDate(date);

            JSONObject jObj = new JSONObject();
            jObj.put("name", sub.getName());
            jObj.put("date", sub.getDate());

            jsonArray.add(jObj);
        }

        System.out.println("ANSWER: " + jsonArray.toJSONString());

        return jsonArray.toJSONString();
    }

    public static boolean checkUser(String username) throws JSONException {
        List<Channel> getChannels = getChannels();

        //System.out.println("hello? " + getChannels.size());
        for(Channel c: getChannels){
            //System.out.println(">: " + c.getSubscribers());
            if(checkUserSubscribes(c.getSubscribers(), username) == true)
                return true;
        }

        return false;
    }

    public static String addSubscriber(String subscribers, int date, String username) throws JSONException {
        Subscription s = new Subscription(username, date);

        List<Subscription> subscriptions = getSubscribers(subscribers);
        subscriptions.add(s);

        JSONArray jsonArray = new JSONArray();

        for(Subscription sub: subscriptions){
            //System.out.println("SELECTED CITY: " + city);

            JSONObject jObj = new JSONObject();
            jObj.put("name", sub.getName());
            jObj.put("date", sub.getDate());

            jsonArray.add(jObj);
        }

        System.out.println("ANSWER: " + jsonArray.toJSONString());

        return jsonArray.toJSONString();
    }

    public static List<Subscription> getSubscribers(String subscribers) throws JSONException
    /**
     * Get the user's cities by parsing the json city array
     * stored in the database
     */
    {

        ArrayList<String> stringArray = new ArrayList<String>();
        ArrayList<Subscription> subscriptions = new ArrayList<>();

        org.json.JSONArray jsonArray = new org.json.JSONArray(subscribers);

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jObj = new JSONObject();
                    //System.out.println(jObj);
                    Subscription s = new Subscription(jsonArray.getJSONObject(i).getString("name"), jsonArray.getJSONObject(i).getInt("date"));
                    subscriptions.add(s);
                    //City city = new City(jsonArray.getJSONObject(i).getString("id"), jsonArray.getJSONObject(i).getString("name"));
                    //System.out.println("City: " + city.getId() + "; " + city.getName());
                    //cities.add(city);
                    //stringArray.add(jsonArray.getString(i));
                }


                return subscriptions;
    }

    public static void subscribe(int channelId, String username){
        int i = (int) (new Date().getTime()/1000);
        Connection connection = null;
        PreparedStatement statement = null;

        try {
            connection = DbManager.getConnection();

            //System.out.println("userId = " +  userId);
            String sql = "SELECT * FROM channels where id = ?";
            statement = connection.prepareStatement(sql);
            statement.setInt(1, channelId);

            ResultSet resultSet = statement.executeQuery();
            if (!resultSet.next()) {
                return;
            }

            String subscribers = resultSet.getString("subscribers");
            String res;
            System.out.println("s: " + subscribers);
            if(checkUserSubscribes(getSubscribers(subscribers), username) == false) {
                res = addSubscriber(subscribers, i, username);
            }
                else{
                   res = updateUser(username, getSubscribers(subscribers), i);
                }

                sql = "UPDATE channels SET subscribers=? WHERE id = ?";
                statement = connection.prepareStatement(sql);
                statement.setInt(2, channelId);
                statement.setString(1, res);

                statement.execute();



        } catch (SQLException | JSONException throwables) {
            throwables.printStackTrace();
        }
    }


    public static List<User> getUsers(){
        Connection connection = null;
        PreparedStatement statement = null;
        List<User> user = new ArrayList<>();

        try {
            connection = DbManager.getConnection();

            //    // SELECT * FROM `articles` WHERE journalid = 1 and user = "mick";
            String sql = "SELECT * FROM persons";
            statement = connection.prepareStatement(sql);

            statement.execute();

            ResultSet resultSet = statement.executeQuery();


            while (resultSet.next()) {
                // id,name, age, gender
                int id = resultSet.getInt("id");
                String username = resultSet.getString("name");
                String gender = resultSet.getString("gender");
                int age = resultSet.getInt("age");

                User u = new User(id, username, age, gender);
                user.add(u);
            }
            resultSet.close();
            return user;

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return new ArrayList<>();
    }

    public static List<Channel> getChannels(){
        Connection connection = null;
        PreparedStatement statement = null;
        List<Channel> channels = new ArrayList<>();

        try {
            connection = DbManager.getConnection();
            String sql = "SELECT * FROM channels";
            statement = connection.prepareStatement(sql);

            statement.execute();

            ResultSet resultSet = statement.executeQuery();

// id, ownerid, name, description, subscribers
            while (resultSet.next()) {
                int Id = resultSet.getInt("id");
                int ownerId = resultSet.getInt("ownerId");
                String name = resultSet.getString("name");
                String d = resultSet.getString("description");
                String sub = resultSet.getString("subscribers");
                List<Subscription> subs = getSubscribers(sub);
                System.out.println("subs: " + subs);

                Channel c = new Channel(Id, ownerId, name, d, subs);
                channels.add(c);
                System.out.println(">>>: " + c.getSubscribers());
            }
            resultSet.close();
            return channels;

        } catch (SQLException | JSONException throwables) {
            throwables.printStackTrace();
        }

        return new ArrayList<>();
    }


    public static List<Channel> getAllChannelsOwned(int userId){
        Connection connection = null;
        PreparedStatement statement = null;
        List<Channel> channels = new ArrayList<>();

        try {
            connection = DbManager.getConnection();

            //System.out.println("userId = " +  userId);
            String sql = "SELECT * FROM channels where ownerId = ?";
            statement = connection.prepareStatement(sql);
            statement.setInt(1, userId);

            statement.execute();

            ResultSet resultSet = statement.executeQuery();

// id, ownerid, name, description, subscribers
            while (resultSet.next()) {
                int Id = resultSet.getInt("id");
                int ownerId = resultSet.getInt("ownerId");
                String name = resultSet.getString("name");
                String d = resultSet.getString("description");
                String sub = resultSet.getString("subscribers");
                List<Subscription> subs = getSubscribers(sub);

                Channel c = new Channel(Id, ownerId, name, d, subs);
                channels.add(c);
            }
            resultSet.close();
            return channels;

        } catch (SQLException | JSONException throwables) {
            throwables.printStackTrace();
        }

        return new ArrayList<>();
    }
}
