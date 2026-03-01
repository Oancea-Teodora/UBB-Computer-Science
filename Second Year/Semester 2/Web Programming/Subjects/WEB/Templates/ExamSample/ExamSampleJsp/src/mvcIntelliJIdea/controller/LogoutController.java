package mvcIntelliJIdea.controller;

import mvcIntelliJIdea.model.User;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;


public class LogoutController extends HttpServlet {
    public LogoutController() {
        super();
    }

    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response) throws ServletException, IOException {

        RequestDispatcher rd;
        rd = request.getRequestDispatcher("/logout.jsp");

        HttpSession session = request.getSession();

        User user = (User) session.getAttribute("user");
        if (user == null || user.getUsername().equals("") || user.getPassword().equals("")) {
            return;
        } else {
            request.setAttribute("user", user);
            session.invalidate();
        }

        rd.forward(request, response);
    }
}

