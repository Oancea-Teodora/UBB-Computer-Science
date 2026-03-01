
package template.controller;

import com.google.gson.Gson;
import jakarta.servlet.RequestDispatcher;
import template.domain.Game;
import template.model.DBManager;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Objects;


public class GameController extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {


        String action = request.getParameter("action");

        if (action == null) return;
        if (action.equals("getGames")) {
            DBManager dbmanager = new DBManager();
            ArrayList<Game> cars = dbmanager.getGames();
            String json = new Gson().toJson(cars);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(json);
        }

    }


    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        if(Objects.equals(action, "AddGame")){
            addGame(request,response);
        }
    }

    private void addGame(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String title = request.getParameter("title");
        String genre = request.getParameter("genre");
        String releaseDateString = request.getParameter("releaseDate");
        String platform = request.getParameter("platform");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate releaseDate = LocalDate.parse(releaseDateString, formatter);

        DBManager dbManager = new DBManager();
        boolean success = dbManager.createGame(title, genre, releaseDate, platform);

        if (success) {
            response.sendRedirect(request.getContextPath() + "/main.jsp"); // Redirect to a success page or list view
        } else {
            request.setAttribute("error", "Failed to create game");
            RequestDispatcher rd = request.getRequestDispatcher("error.jsp"); // Redirect to an error page
            rd.forward(request, response);
        }
    }
}