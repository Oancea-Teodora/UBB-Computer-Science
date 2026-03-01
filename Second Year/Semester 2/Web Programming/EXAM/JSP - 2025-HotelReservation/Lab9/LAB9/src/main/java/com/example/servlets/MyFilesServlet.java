package com.example.servlets;

import com.example.util.DBUtil;

import javax.servlet.ServletException;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class MyFilesServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String userName = (String) req.getSession().getAttribute("username");
        if (userName == null) {
            resp.sendRedirect("login");
            return;
        }

        try {
            List<String> myList = DBUtil.findDocumentsMovies(userName);
            req.setAttribute("myRes", myList);

            String name = DBUtil.getDocumentWithMostAuthors();
            req.setAttribute("mostAuthors", name);

        } catch (Exception e) {
            throw new ServletException(e);
        }

        req.getRequestDispatcher("/myfiles.jsp").forward(req, resp);
    }
}