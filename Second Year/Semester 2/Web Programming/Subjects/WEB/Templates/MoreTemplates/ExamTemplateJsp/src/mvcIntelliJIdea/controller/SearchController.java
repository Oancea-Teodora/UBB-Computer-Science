package mvcIntelliJIdea.controller;

import mvcIntelliJIdea.dbHelper.SearchQuery;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class SearchController extends HttpServlet {
    public SearchController() { super(); }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //Get the text to search
        String studentName = req.getParameter("searchValue");

        //Create a SearchQuery
        SearchQuery sq = new SearchQuery();

        //Get the HTML table
        sq.doSearch(studentName);
        String table = sq.getHTMLTable();

        //Pass the execution to read.jsp
        req.setAttribute("table", table);
        String url = "/read.jsp";

        RequestDispatcher dispatcher = req.getRequestDispatcher(url);
        dispatcher.forward(req, resp);

    }
}
