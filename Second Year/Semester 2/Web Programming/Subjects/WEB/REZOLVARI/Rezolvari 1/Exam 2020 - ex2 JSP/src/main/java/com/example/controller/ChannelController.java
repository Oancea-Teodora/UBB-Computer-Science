package com.example.controller;

import com.example.domain.Channel;
import com.example.service.ChannelService;
import com.example.service.UserService;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.simple.JSONArray;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

public class ChannelController extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        System.out.println("hello>>");

        System.out.println(request.getParameter("getChannels"));
        if (request.getParameter("action")!= null && request.getParameter("action").equals("getChannels")) {

            response.setContentType("application/json");
            System.out.println("hello>>>");
            int owner = Integer.parseInt(request.getParameter("owner"));

            System.out.println("owner: " + owner);
            List<Channel> channels = ChannelService.getAllChannelsOwned(owner);

            System.out.println("channels: " + channels);

            JSONArray channel = new JSONArray();
//
            for(Channel c: channels){
                System.out.println("chanel: : "+ c.getSubscribers());
                JSONObject jsonObject = new JSONObject();
//
                try{
                    jsonObject.put("id", c.getId());
                    jsonObject.put("owner", c.getOwnerId());
                    jsonObject.put("name", c.getName());
                    jsonObject.put("description", c.getDescr());
                    jsonObject.put("subscribers", c.getSubscribers());

                    channel.add(jsonObject);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
//
            }

            System.out.println(channel);
            System.out.println(channel.toJSONString());
            PrintWriter out = new PrintWriter(response.getOutputStream());
            out.println(channel.toJSONString());
            out.flush();
            //response.sendRedirect("success.jsp");
        }
        else
            if(request.getParameter("subscribe")!=null){
                System.out.println("heey");
                int channelId = Integer.parseInt(request.getParameter("channel"));
                HttpSession session = request.getSession();
                String user = (String) session.getAttribute("Username");
                System.out.println("hello????");
                try {
                    System.out.println("User is subscribed: " + ChannelService.checkUser(user));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                ChannelService.subscribe(channelId, user);
                response.sendRedirect("success.jsp");
            }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

        if (request.getParameter("getUsers") != null) {
            String filter = request.getParameter("user");

        }
    }
}
