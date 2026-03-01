package template.controller;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import template.domain.User;
import template.model.DBManager;

import java.io.IOException;



public class LoginController extends HttpServlet {

    public LoginController() {
        super();
    }

    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response) throws ServletException, IOException {

        String username = request.getParameter("username");
        String password = request.getParameter("password");
        //  RequestDispatcher rd = null;

        DBManager dbmanager = new DBManager();
        User user = dbmanager.authenticate(username, password);
        if (user != null) {
//            rd = request.getRequestDispatcher("/GameController");
//            request.setAttribute("user", user);
//            HttpSession session = request.getSession();
//            session.setAttribute("user", user);
//            response.sendRedirect(request.getContextPath() + "/GameController");
//
//        } else {
//            rd = request.getRequestDispatcher("/error.jsp");
//        }
//        rd.forward(request, response);
            HttpSession session = request.getSession();
            session.setAttribute("user", user);
            // Redirect to GameController
            response.sendRedirect(request.getContextPath() + "/main.jsp");
        } else {
            request.setAttribute("error", "Invalid username or password");
            RequestDispatcher rd = request.getRequestDispatcher("login.jsp");
            rd.forward(request, response);

        }
    }}


