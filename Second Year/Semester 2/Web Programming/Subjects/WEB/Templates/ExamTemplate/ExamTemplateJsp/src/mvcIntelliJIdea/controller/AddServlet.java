package mvcIntelliJIdea.controller;

import mvcIntelliJIdea.dbHelper.AddQuery;
import mvcIntelliJIdea.model.Student;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class AddServlet extends HttpServlet {
    public AddServlet() { super(); }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //Get the data
        String name = req.getParameter("name");
        int grade = Integer.parseInt(req.getParameter("grade"));

        //Set up a student object
        Student student = new Student();
        student.setName(name);
        student.setGrade(grade);

        //Set up an addQuery object
        AddQuery aq = new AddQuery();

        //Pass the student to addQuery to add to the database
        aq.doAdd(student);

        //Pass execution control to the ReadController
        String url = "/ReadController";

        RequestDispatcher dispatcher = req.getRequestDispatcher(url);
        dispatcher.forward(req, resp);
    }
}
