package web.exam.jspexam.controller;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet(name = "MainServlet", value = "/home")
public class MainController extends HttpServlet {
    @Override
    public void init() throws ServletException {
        super.init();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        var action = request.getParameter("action");

        switch (action) {
            case "submitName" -> handleSubmitNameGet(request, response);
        }
    }

    private void handleSubmitNameGet(HttpServletRequest request, HttpServletResponse response) {
        var name = request.getParameter("name");
        HttpSession session = request.getSession();
        session.setAttribute("user", name);
    }
}
