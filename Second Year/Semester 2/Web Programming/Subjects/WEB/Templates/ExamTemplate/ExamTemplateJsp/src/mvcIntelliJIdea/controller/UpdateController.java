package mvcIntelliJIdea.controller;

import mvcIntelliJIdea.dbHelper.ReadRecord;
import mvcIntelliJIdea.model.Student;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class UpdateController extends HttpServlet {
    public UpdateController() { super(); }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int id = Integer.parseInt(req.getParameter("id"));

        ReadRecord rr = new ReadRecord(id);
        rr.doRead();
        Student student = rr.getStudent();

        req.setAttribute("student", student);

        String url = "/update.jsp";

        RequestDispatcher dispatcher = req.getRequestDispatcher(url);
        dispatcher.forward(req, resp);

    }
}
