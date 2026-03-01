package com.example.servlets;

import com.example.util.DBUtil;

import javax.servlet.ServletException;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.*;

public class LogoutServlet extends HttpServlet {
    private static final int GAME_ID = 1;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        if (session != null) {
            try (Connection conn = DBUtil.getConnection()) {
                PreparedStatement ps = conn.prepareStatement(
                        "UPDATE game_state " +
                                "SET player1 = NULL, player2 = NULL, board = ?, turn = 'X', status = 'WAITING' " +
                                "WHERE id = ?");
                ps.setString(1, "_________");
                ps.setInt(2, GAME_ID);
                ps.executeUpdate();
                ps.close();
            } catch (SQLException e) {
                throw new ServletException(e);
            }
            session.invalidate();
        }
        resp.sendRedirect("login");
    }
}