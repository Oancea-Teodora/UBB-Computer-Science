package com.example.controller;

import com.example.model.Asset;
import com.example.service.AssetService;
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

public class AssetController extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String action = request.getParameter("action");

        if((action != null) && action.equals("add")){
            System.out.println(request.getParameter("assets"));
            String assets = request.getParameter("assets");
            System.out.println("assets: " + assets);

            int userid = Integer.parseInt(request.getParameter("userId"));

            System.out.println("uid: " + userid);

            try {
                AssetService.addAssets(assets, userid);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            //System.out.println("hello1");
            //CitiesService.addCity(cityId, userId);
            //System.out.println("hello2");
        }



        //System.out.println("HELLO THERE");
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String action = request.getParameter("action");

        if(action != null && action.equals("getAssets")) {
            response.setContentType("application/json");

            HttpSession session = request.getSession();
            int userid = (int)session.getAttribute("Id");
            List<Asset> assets = AssetService.getAssets(userid);

            //System.out.println("initi: " + assets);
            JSONArray article = new JSONArray();
//
            for(Asset a: assets){
                JSONObject jsonObject = new JSONObject();
//
                try{
                    jsonObject.put("id", a.getId());
                    jsonObject.put("userid", a.getUserid());
                    jsonObject.put("name", a.getName());
                    jsonObject.put("description", a.getDescr());
                    jsonObject.put("value", a.getValue());

                    article.add(jsonObject);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
//
            }

            //System.out.println(article.toJSONString());
            PrintWriter out = new PrintWriter(response.getOutputStream());
            out.println(article.toJSONString());
            out.flush();
        }
    }
}
