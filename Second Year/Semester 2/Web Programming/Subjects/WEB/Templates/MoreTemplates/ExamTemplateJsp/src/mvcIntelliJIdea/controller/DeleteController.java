package mvcIntelliJIdea.controller;

import mvcIntelliJIdea.dbHelper.DeleteQuery;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class DeleteController extends HttpServlet {
    public DeleteController() { super(); }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //Get the id
        int id = Integer.parseInt(req.getParameter("id"));

        DeleteQuery dq = new DeleteQuery();
        dq.doDelete(id);

        //Pass the execution on to the ReadController
        String url = "/ReadController";

        RequestDispatcher dispatcher = req.getRequestDispatcher(url);
        dispatcher.forward(req, resp);
    }
}
