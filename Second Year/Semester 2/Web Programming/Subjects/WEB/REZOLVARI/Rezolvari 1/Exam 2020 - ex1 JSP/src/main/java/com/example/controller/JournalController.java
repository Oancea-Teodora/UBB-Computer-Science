package com.example.controller;

import com.example.Service.JournalService;
import com.example.model.Article;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.simple.JSONArray;

public class JournalController extends HttpServlet {

    public JournalController(){
        super();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String action = request.getParameter("action");

        //if(request.getParameter("dropdownAction") != null){
        if(action!= null && action.equals("getJournals")){
            response.setContentType("application/json");

            System.out.println(request.getParameter("category"));
            int journalId = Integer.parseInt(request.getParameter("category"));
            HttpSession session = request.getSession();
            String user = (String) session.getAttribute("Username");
            System.out.println("user is: " + user);

            List<Article> articles = JournalService.getArticles(user, journalId);

            JSONArray article = new JSONArray();
//
            for(Article a: articles){
                JSONObject jsonObject = new JSONObject();
//
                try{
                    jsonObject.put("id", a.getId());
                    jsonObject.put("user", a.getUser());
                    jsonObject.put("journalId", a.getJournalid());
                    jsonObject.put("summary", a.getSummary());
                    jsonObject.put("date", a.getDate());

                    article.add(jsonObject);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
//
            }

            System.out.println(article.toJSONString());
            PrintWriter out = new PrintWriter(response.getOutputStream());
            out.println(article.toJSONString());
            out.flush();


            //ProductService.addToCart(user, Integer.parseInt(request.getParameter("category")), Integer.parseInt(request.getParameter("quantity")));
            //response.sendRedirect("success.jsp");
        }

        else if(request.getParameter("add") != null){
            HttpSession session = request.getSession();
            String user = (String) session.getAttribute("Username");
            System.out.println("user is: " + user);

            String name = request.getParameter("artname");
            String description = request.getParameter("description");
            String jname = request.getParameter("jname");

            JournalService.insertArticle(user, jname, description);

            response.sendRedirect("success.jsp");
        }
    }

    //
//        if(action != null && action.equals("getArticles")){
//        System.out.println("hey");
//    }
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String action = request.getParameter("action");

        if(action != null && action.equals("getArticles")) {

            List<Article> articles = JournalService.getAllArticles();

            JSONArray article = new JSONArray();
//
            for(Article a: articles){
                JSONObject jsonObject = new JSONObject();
//
                try{
                    jsonObject.put("id", a.getId());
                    jsonObject.put("user", a.getUser());
                    jsonObject.put("journalId", a.getJournalid());
                    jsonObject.put("summary", a.getSummary());
                    jsonObject.put("date", a.getDate());

                    article.add(jsonObject);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
//
            }

            System.out.println(article.toJSONString());
            PrintWriter out = new PrintWriter(response.getOutputStream());
            out.println(article.toJSONString());
            out.flush();
        }
    }


}
