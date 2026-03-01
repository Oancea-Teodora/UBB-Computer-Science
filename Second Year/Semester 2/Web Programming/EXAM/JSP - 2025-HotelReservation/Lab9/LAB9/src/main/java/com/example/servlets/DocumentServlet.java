package com.example.servlets;

import com.example.util.DBUtil;
import javax.servlet.ServletException;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class DocumentServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        List<Map<String, Object>> docs = null;
        try {
            docs = DBUtil.getAllDocuments();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        req.setAttribute("documents", docs);

        req.getRequestDispatcher("/document.jsp").forward(req, resp);

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String userId = (String) req.getSession().getAttribute("username");
        if (userId == null) {
            resp.sendRedirect("login");
            return;
        }

        String name = String.valueOf(req.getParameter("name"));
        String content = String.valueOf(req.getParameter("content"));

        try {

            req.setAttribute("name", name);
            req.setAttribute("content",content);

            DBUtil.insertDocument(name, content);
            DBUtil.addDocumentToAuthors(userId, name, content);

        } catch (Exception e) {
            throw new ServletException(e);
        }
        resp.sendRedirect("document");
     //   req.getRequestDispatcher("/document.jsp").forward(req, resp);
    }
}