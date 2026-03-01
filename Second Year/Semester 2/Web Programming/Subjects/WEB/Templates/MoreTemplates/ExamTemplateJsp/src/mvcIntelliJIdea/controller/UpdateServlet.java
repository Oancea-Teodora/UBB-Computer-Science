package mvcIntelliJIdea.controller;

import mvcIntelliJIdea.dbHelper.UpdateQuery;
import mvcIntelliJIdea.model.Student;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class UpdateServlet extends HttpServlet {
    public UpdateServlet() { super(); }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //Get the data and set up student object
        int id = Integer.parseInt(req.getParameter("id"));
        String name = req.getParameter("name");
        int grade = Integer.parseInt(req.getParameter("grade"));

        Student student = new Student();
        student.setId(id);
        student.setName(name);
        student.setGrade(grade);

        //Create the UpdateQuery object
        UpdateQuery uq = new UpdateQuery();
        uq.doUpdate(student);

        //Pass execution control to the ReadController
        String url = "/ReadController";

        RequestDispatcher dispatcher = req.getRequestDispatcher(url);
        dispatcher.forward(req, resp);
    }
}
