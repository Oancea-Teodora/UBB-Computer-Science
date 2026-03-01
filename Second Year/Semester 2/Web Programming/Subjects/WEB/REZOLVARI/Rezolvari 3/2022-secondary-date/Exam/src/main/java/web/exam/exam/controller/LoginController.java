package web.exam.exam.controller;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet(name = "LoginServlet", value = "/login")
public class LoginController extends HttpServlet {
    @Override
    public void init() throws ServletException {
        super.init();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        var user = request.getParameter("user");
        HttpSession session = request.getSession();
        session.setAttribute("user", user);
        response.setHeader("Access-Control-Allow-Origin", "*");
    }
}
