package com.example.controller;

import com.example.service.UserService;

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

public class FamilyController extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

        if (request.getParameter("dropdownAction") != null) {

            String child = (String) request.getParameter("family");
            String mother = (String) request.getParameter("mother");
            String father = request.getParameter("father");

            System.out.println("Child: " + child + " " + mother + " " + father);
            UserService.addParentRecord(child, mother, father);

            request.getRequestDispatcher("success.jsp").forward(request, response);
            response.sendRedirect("success.jsp");
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String action = request.getParameter("action");
        System.out.println("action = " + action);
        if ((action != null) && action.equals("getFathers")) {
            response.setContentType("application/json");

            String child = request.getParameter("child");
            System.out.println("child: "+ child);
            List<String> fathers = UserService.getFathers(child);
            //System.out.println(res);

//            request.setAttribute("products", res);
//            //request.getRequestDispatcher("products.jsp").forward(request, response);
//            HttpSession session = request.getSession();
//            session.setAttribute("products", res);
//            //response.sendRedirect("products.jsp");
//            request.getRequestDispatcher("success.jsp").forward(request, response);
            System.out.println("fathers: " + fathers);
            JSONArray father = new JSONArray();
//
                for(String f: fathers){
                JSONObject jsonObject = new JSONObject();
//
                try{
                    jsonObject.put("father", f);
                    father.add(jsonObject);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
//
            }

            System.out.println(father.toJSONString());
            PrintWriter out = new PrintWriter(response.getOutputStream());
            out.println(father.toJSONString());
            out.flush();
            //response.sendRedirect("success.jsp");
        }
    }
}
