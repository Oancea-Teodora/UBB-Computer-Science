package com.example.servlets;

import com.example.util.DBUtil;
import javax.servlet.ServletException;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class MovieServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        List<Map<String, Object>> movies = null;
        try {
            movies = DBUtil.getAllMovies();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        req.setAttribute("movies", movies);

        req.getRequestDispatcher("/movie.jsp").forward(req, resp);

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String userId = (String) req.getSession().getAttribute("username");
        if (userId == null) {
            resp.sendRedirect("login");
            return;
        }

        String title = String.valueOf(req.getParameter("title"));

        try {

            req.setAttribute("title", title);
            DBUtil.deleteMovie(title);

        } catch (Exception e) {
            throw new ServletException(e);
        }
        resp.sendRedirect("movie");
        //   req.getRequestDispatcher("/document.jsp").forward(req, resp);
    }
}