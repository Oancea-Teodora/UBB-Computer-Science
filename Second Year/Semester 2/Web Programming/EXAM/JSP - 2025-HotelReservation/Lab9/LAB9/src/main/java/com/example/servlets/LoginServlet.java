package com.example.servlets;

import com.example.util.DBUtil;

import javax.servlet.ServletException;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.*;
import java.util.Arrays;

public class LoginServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/login.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String user = req.getParameter("username");
        String pass = req.getParameter("namedm");

        if (user == null || pass == null || user.isEmpty() || pass.isEmpty()) {
            req.setAttribute("error", "Please enter both username and name.");
            req.getRequestDispatcher("/login.jsp").forward(req, resp);
            return;
        }

        try (Connection conn = DBUtil.getConnection()) {
            PreparedStatement ps = conn.prepareStatement(
                    "SELECT * FROM Authors WHERE name = ?");
            ps.setString(1, user);
            ResultSet rs = ps.executeQuery();
            if (!rs.next()) {
                rs.close();
                ps.close();
                req.setAttribute("error", "Invalid username/name.");
                req.getRequestDispatcher("/login.jsp").forward(req, resp);
                return;
            } else {
                int authorId = rs.getInt("id");
                String docs = rs.getString("documentList");
                String movies = rs.getString("movieList");

                boolean valid = false;
                if (docs != null && Arrays.asList(docs.split(",")).contains(pass))
                    valid = true;
                if (movies != null && Arrays.asList(movies.split(",")).contains(pass))
                    valid = true;

                if (valid) {
                    HttpSession session = req.getSession();
                    session.setAttribute("username", user);
                    session.setAttribute("userId", authorId);
                    resp.sendRedirect("home");
                    return;
                } else {
                    req.setAttribute("error", "Invalid credentials. Document/movie name not found for this user.");
                    req.getRequestDispatcher("/login.jsp").forward(req, resp);
                    return;
                }
            }

        } catch (SQLException e) {
            throw new ServletException(e);
        }
    }
}