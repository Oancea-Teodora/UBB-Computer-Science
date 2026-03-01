package com.example.controller;

import com.example.model.Template;
import com.example.model.document;
import com.example.model.keyword;
import com.example.service.websiteService;
import org.json.JSONException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

public class websiteController extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {


        String key = request.getParameter("key");
        String value = request.getParameter("value");

        websiteService.insertKeyword(key, value);
        response.sendRedirect("index.jsp");

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String action = request.getParameter("action");

        if(action != null && action.equals("getDocuments")) {
            response.setContentType("application/json");


            String filter = request.getParameter("filter");
            List<document> documents = websiteService.getDocs(filter);

            JSONArray document = new JSONArray();
//
            for(document d: documents){
                JSONObject jsonObject = new JSONObject();
//
                jsonObject.put("id", d.getId());
                jsonObject.put("title", d.getTitle());
                jsonObject.put("list", d.getListOfTemplates());

                document.add(jsonObject);
                //
            }

            System.out.println(document.toJSONString());
            PrintWriter out = new PrintWriter(response.getOutputStream());
            out.println(document.toJSONString());
            out.flush();
        }
        if(action != null && action.equals("renderDocument")){
            response.setContentType("application/json");

            int docid = Integer.parseInt(request.getParameter("docid"));
            List<Template> templates = websiteService.renderDocument(docid);

            for(Template t: templates)
                System.out.println(t.textContent);

            JSONArray document = new JSONArray();
//
            for(Template t: templates){
                JSONObject jsonObject = new JSONObject();

                jsonObject.put("text", t.textContent);
                document.add(jsonObject);
                //
            }

            System.out.println(document.toJSONString());
            PrintWriter out = new PrintWriter(response.getOutputStream());
            out.println(document.toJSONString());
            out.flush();
        }
        if(action!=null && action.equals("getKeywords")){
            response.setContentType("application/json");


            List<keyword> keywords = websiteService.getKeywords();


            JSONArray document = new JSONArray();
//
            for(keyword k: keywords){
                JSONObject jsonObject = new JSONObject();

                jsonObject.put("key", k.getKey());
                jsonObject.put("value", k.getValue());
                document.add(jsonObject);
                //
            }

            System.out.println(document.toJSONString());
            PrintWriter out = new PrintWriter(response.getOutputStream());
            out.println(document.toJSONString());
            out.flush();
        }

        // to do for keyword
    }


}
