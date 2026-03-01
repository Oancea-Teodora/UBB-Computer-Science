package com.example.servlets;

import com.example.util.DBUtil;

import javax.servlet.ServletException;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.*;

public class GameServlet extends HttpServlet {
    private static final int GAME_ID = 1;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("username") == null) {
            resp.sendRedirect("login");
            return;
        }
        String user = (String) session.getAttribute("username");

        try (Connection conn = DBUtil.getConnection()) {
            PreparedStatement ps = conn.prepareStatement(
                    "SELECT player1, player2, board, turn, status FROM game_state WHERE id = ?");
            ps.setInt(1, GAME_ID);
            ResultSet rs = ps.executeQuery();
            if (!rs.next()) {
                throw new ServletException("Game not found.");
            }
            String p1 = rs.getString("player1");
            String p2 = rs.getString("player2");
            String board = rs.getString("board");
            String turn = rs.getString("turn");
            String status = rs.getString("status");
            rs.close();
            ps.close();

            if (!(user.equals(p1) || user.equals(p2))) {
                req.setAttribute("error", "You are not part of this game.");
                req.getRequestDispatcher("/login.jsp").forward(req, resp);
                return;
            }

            if ("WAITING".equals(status)) {
                req.getRequestDispatcher("/waiting.jsp").forward(req, resp);
                return;
            }

            if ("OVER".equals(status)) {
                String winner = determineWinner(board);
                String resultMessage;
                if ("DRAW".equals(winner)) {
                    resultMessage = "It's a draw!";
                } else if ((user.equals(p1) && "X".equals(winner)) || (user.equals(p2) && "O".equals(winner))) {
                    resultMessage = "You won!";
                } else {
                    resultMessage = "You lost!";
                }

                PreparedStatement resetUpd = conn.prepareStatement(
                        "UPDATE game_state SET board = ?, turn = 'X', status = 'ONGOING' WHERE id = ?");
                resetUpd.setString(1, "_________");
                resetUpd.setInt(2, GAME_ID);
                resetUpd.executeUpdate();
                resetUpd.close();

                req.setAttribute("board", board);
                req.setAttribute("resultMessage", resultMessage);
                req.getRequestDispatcher("/game.jsp").forward(req, resp);
                return;
            }

            String mySymbol = user.equals(p1) ? "X" : "O";
            boolean myTurn = turn.equals(mySymbol);

            req.setAttribute("board", board);
            req.setAttribute("turn", turn);
            req.setAttribute("mySymbol", mySymbol);
            req.setAttribute("myTurn", myTurn);
            req.getRequestDispatcher("/game.jsp").forward(req, resp);

        } catch (SQLException e) {
            throw new ServletException(e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("username") == null) {
            resp.sendRedirect("login");
            return;
        }
        String user = (String) session.getAttribute("username");
        String cellStr = req.getParameter("cell"); // expected "0" through "8"

        if (cellStr == null) {
            resp.sendRedirect("game");
            return;
        }

        int cell;
        try {
            cell = Integer.parseInt(cellStr);
            if (cell < 0 || cell > 8)
                throw new NumberFormatException();
        } catch (NumberFormatException ex) {
            resp.sendRedirect("game");
            return;
        }

        try (Connection conn = DBUtil.getConnection()) {
            conn.setAutoCommit(false);

            PreparedStatement ps = conn.prepareStatement(
                    "SELECT player1, player2, board, turn, status FROM game_state WHERE id = ? FOR UPDATE");
            ps.setInt(1, GAME_ID);
            ResultSet rs = ps.executeQuery();
            if (!rs.next()) {
                conn.rollback();
                throw new ServletException("Game not found.");
            }
            String p1 = rs.getString("player1");
            String p2 = rs.getString("player2");
            String board = rs.getString("board");
            String turn = rs.getString("turn");
            String status = rs.getString("status");
            rs.close();
            ps.close();

            if (!"ONGOING".equals(status) || !(user.equals(p1) || user.equals(p2))) {
                conn.rollback();
                resp.sendRedirect("game");
                return;
            }

            String mySymbol = user.equals(p1) ? "X" : "O";
            if (!turn.equals(mySymbol)) {
                conn.rollback();
                resp.sendRedirect("game");
                return;
            }

            if (board == null || board.length() != 9) {
                if (board == null || board.length() == 0) {
                    System.out.println("DEBUG: Board is null or empty, initializing with underscores");
                    PreparedStatement fixBoard = conn.prepareStatement(
                            "UPDATE game_state SET board = ? WHERE id = ?");
                    fixBoard.setString(1, "_________");
                    fixBoard.setInt(2, GAME_ID);
                    fixBoard.executeUpdate();
                    fixBoard.close();
                    board = "_________";
                } else {
                    conn.rollback();
                    throw new ServletException(
                            "Invalid board state: " + (board == null ? "NULL" : "length=" + board.length()));
                }
            }
            if (board.charAt(cell) != '_') {
                conn.rollback();
                resp.sendRedirect("game");
                return;
            }

            StringBuilder newBoard = new StringBuilder(board);
            newBoard.setCharAt(cell, mySymbol.charAt(0));
            String updatedBoard = newBoard.toString();

            String newStatus = "ONGOING";
            String nextTurn = turn.equals("X") ? "O" : "X";
            String winner = determineWinner(updatedBoard);

            if (winner != null) {
                newStatus = "OVER";
            } else if (!updatedBoard.contains("_")) {
                newStatus = "OVER";
                winner = "DRAW";
            }

            PreparedStatement upd = conn.prepareStatement(
                    "UPDATE game_state SET board = ?, turn = ?, status = ? WHERE id = ?");
            upd.setString(1, updatedBoard);
            // If game is over, keep turn = who just moved; otherwise flip it
            upd.setString(2, (newStatus.equals("OVER") ? turn : nextTurn));
            upd.setString(3, newStatus);
            upd.setInt(4, GAME_ID);
            upd.executeUpdate();
            upd.close();

            conn.commit();
        } catch (SQLException e) {
            throw new ServletException(e);
        }

        resp.sendRedirect("game");
    }

    /**
     * Returns "X", "O", or "DRAW" if someone has won or the board is full;
     * returns null if still ongoing.
     */
    private String determineWinner(String b) {
        int[][] lines = {
                { 0, 1, 2 }, { 3, 4, 5 }, { 6, 7, 8 },
                { 0, 3, 6 }, { 1, 4, 7 }, { 2, 5, 8 },
                { 0, 4, 8 }, { 2, 4, 6 }
        };
        for (int[] L : lines) {
            char a = b.charAt(L[0]), c = b.charAt(L[1]), d = b.charAt(L[2]);
            if (a != '_' && a == c && c == d) {
                return String.valueOf(a);
            }
        }
        return null;
    }
}