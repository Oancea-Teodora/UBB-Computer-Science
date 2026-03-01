package com.example.servlets;

import com.example.util.DBUtil;

import javax.servlet.ServletException;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.*;

public class LoginServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/login.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String user = req.getParameter("username");
        String pass = req.getParameter("password");

        if (user == null || pass == null || user.isEmpty() || pass.isEmpty()) {
            req.setAttribute("error", "Please enter both username and password.");
            req.getRequestDispatcher("/login.jsp").forward(req, resp);
            return;
        }

        try (Connection conn = DBUtil.getConnection()) {
            PreparedStatement ps = conn.prepareStatement(
                    "SELECT * FROM users WHERE username = ? AND password = ?");
            ps.setString(1, user);
            ps.setString(2, pass);
            ResultSet rs = ps.executeQuery();
            if (!rs.next()) {
                rs.close();
                ps.close();
                req.setAttribute("error", "Invalid username/password.");
                req.getRequestDispatcher("/login.jsp").forward(req, resp);
                return;
            }
            rs.close();
            ps.close();

            HttpSession session = req.getSession();
            session.setAttribute("username", user);

            // Also store userId for hotel reservations
            int userId = DBUtil.getUserId(user);
            session.setAttribute("userId", userId);

            resp.sendRedirect("home");

        } catch (SQLException e) {
            throw new ServletException(e);
        }
    }
}