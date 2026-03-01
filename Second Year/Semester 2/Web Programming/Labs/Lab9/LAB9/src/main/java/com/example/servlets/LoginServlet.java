package com.example.servlets;

import com.example.util.DBUtil;

import javax.servlet.ServletException;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.*;

public class LoginServlet extends HttpServlet {
    private static final int GAME_ID = 1;

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

            conn.setAutoCommit(false);
            PreparedStatement lockPs = conn.prepareStatement(
                    "SELECT player1, player2 FROM game_state WHERE id = ? FOR UPDATE");
            lockPs.setInt(1, GAME_ID);
            ResultSet grs = lockPs.executeQuery();
            if (!grs.next()) {
                conn.rollback();
                req.setAttribute("error", "Game is not initialized.");
                req.getRequestDispatcher("/login.jsp").forward(req, resp);
                return;
            }
            String p1 = grs.getString("player1");
            String p2 = grs.getString("player2");
            grs.close();
            lockPs.close();

            boolean isP1 = user.equals(p1);
            boolean isP2 = user.equals(p2);

            if (p1 == null) {
                PreparedStatement upd = conn.prepareStatement(
                        "UPDATE game_state SET player1 = ? WHERE id = ?");
                upd.setString(1, user);
                upd.setInt(2, GAME_ID);
                upd.executeUpdate();
                upd.close();
                conn.commit();
            } else if (p2 == null && !isP1) {
                PreparedStatement upd = conn.prepareStatement(
                        "UPDATE game_state SET player2 = ?, status = 'ONGOING' WHERE id = ?");
                upd.setString(1, user);
                upd.setInt(2, GAME_ID);
                upd.executeUpdate();
                upd.close();
                conn.commit();
            } else if (isP1 || isP2) {
                conn.commit();
            } else {
                conn.rollback();
                req.setAttribute("error", "Game is full. Try again later.");
                req.getRequestDispatcher("/login.jsp").forward(req, resp);
                return;
            }

            HttpSession session = req.getSession();
            session.setAttribute("username", user);
            resp.sendRedirect("game");

        } catch (SQLException e) {
            throw new ServletException(e);
        }
    }
}