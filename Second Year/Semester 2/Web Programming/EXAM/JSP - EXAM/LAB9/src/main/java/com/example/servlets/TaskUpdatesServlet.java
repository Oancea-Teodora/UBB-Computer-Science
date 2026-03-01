package com.example.servlets;

import com.example.model.Task;
import com.example.util.DatabaseUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

public class TaskUpdatesServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        List<Task> tasks = DatabaseUtil.getAllTasks();

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        // Simple JSON response without external library
        StringBuilder json = new StringBuilder();
        json.append("[");
        for (int i = 0; i < tasks.size(); i++) {
            Task task = tasks.get(i);
            if (i > 0)
                json.append(",");
            json.append("{")
                    .append("\"id\":").append(task.getId()).append(",")
                    .append("\"title\":\"").append(escapeJson(task.getTitle())).append("\",")
                    .append("\"status\":\"").append(task.getStatus()).append("\",")
                    .append("\"lastUpdatedByUsername\":\"")
                    .append(escapeJson(task.getLastUpdatedByUsername() != null ? task.getLastUpdatedByUsername() : ""))
                    .append("\"")
                    .append("}");
        }
        json.append("]");

        response.getWriter().write(json.toString());
    }

    private String escapeJson(String str) {
        if (str == null)
            return "";
        return str.replace("\"", "\\\"").replace("\\", "\\\\").replace("\n", "\\n").replace("\r", "\\r");
    }
}