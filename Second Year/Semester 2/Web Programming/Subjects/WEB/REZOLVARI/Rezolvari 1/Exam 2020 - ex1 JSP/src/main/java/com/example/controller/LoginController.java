package com.example.controller;

import com.example.Service.UserService;
import com.example.model.User;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;

public class LoginController extends HttpServlet {
    public LoginController(){
        super();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        // prints text data to a character stream.
        PrintWriter out = response.getWriter();

        response.setContentType("text/html");

        // get the parameters from the request
        String username = request.getParameter("username");
        String password = request.getParameter("password");


        User myUser = UserService.getUserByUsername(username);

        // create a new session and save the user for the session
        HttpSession session = request.getSession();
        assert myUser != null;
        //session.setAttribute("Id", myUser.getId());
        session.setAttribute("Username", username);

        //ProductService.createCart();
        //ProductService.emptyCart();
        // if the session has been shut down unexpectedly, we need to delete the state information stored in the database
        //CitiesService.deleteData(myUser.getId());
        response.sendRedirect("success.jsp");
    }
}
