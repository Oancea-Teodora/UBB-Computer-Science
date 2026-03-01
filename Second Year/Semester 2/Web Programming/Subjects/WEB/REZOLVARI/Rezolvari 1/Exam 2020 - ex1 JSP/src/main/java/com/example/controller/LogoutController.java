package com.example.controller;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class LogoutController extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession();
        //Integer userId = Integer.parseInt(request.getParameter("Id"));
        //System.out.println("USER Name " + session.getAttribute("Username") + " ID: " + session.getAttribute("Id"));
        //Integer userId = (Integer)session.getAttribute("name");
        session.invalidate();
//        session.setAttribute("Id", null);
//        session.setAttribute("Username", null);
        response.sendRedirect("index.jsp");
    }
}
