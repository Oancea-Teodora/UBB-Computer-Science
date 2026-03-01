package com.tictactoe.servlet;

import com.tictactoe.util.DatabaseUtil;
import com.tictactoe.model.Game;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class GameServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("username") == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        String username = (String) session.getAttribute("username");
        String action = request.getParameter("action");
        Game game = DatabaseUtil.getCurrentGame();

        if ("join".equals(action)) {
            handleJoinGame(request, response, username, game);
        } else if ("move".equals(action)) {
            handleMove(request, response, username, game);
        } else if ("reset".equals(action)) {
            handleReset(request, response, username, game);
        } else {
            response.sendRedirect("game.jsp");
        }
    }

    private void handleJoinGame(HttpServletRequest request, HttpServletResponse response,
            String username, Game game) throws ServletException, IOException {

        if (!game.addPlayer(username)) {
            request.setAttribute("error", "Game is full! Only 2 players can play at a time.");
        } else {
            request.setAttribute("message", "Successfully joined the game!");
        }

        request.getRequestDispatcher("game.jsp").forward(request, response);
    }

    private void handleMove(HttpServletRequest request, HttpServletResponse response,
            String username, Game game) throws ServletException, IOException {

        try {
            int row = Integer.parseInt(request.getParameter("row"));
            int col = Integer.parseInt(request.getParameter("col"));

            if (!game.makeMove(row, col, username)) {
                request.setAttribute("error", "Invalid move! Please try again.");
            }
        } catch (NumberFormatException e) {
            request.setAttribute("error", "Invalid move coordinates!");
        }

        request.getRequestDispatcher("game.jsp").forward(request, response);
    }

    private void handleReset(HttpServletRequest request, HttpServletResponse response,
            String username, Game game) throws ServletException, IOException {

        game.resetGame();
        request.setAttribute("message", "Game has been reset!");
        request.getRequestDispatcher("game.jsp").forward(request, response);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("username") == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        response.sendRedirect("game.jsp");
    }
}