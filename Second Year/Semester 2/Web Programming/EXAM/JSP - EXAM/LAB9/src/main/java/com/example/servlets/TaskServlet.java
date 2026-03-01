package com.example.servlets;

import com.example.model.Task;
import com.example.model.User;
import com.example.util.DatabaseUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

public class TaskServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        List<Task> tasks = DatabaseUtil.getAllTasks();
        request.setAttribute("tasks", tasks);

        Integer tasksMovedCount = (Integer) session.getAttribute("tasksMovedCount");
        if (tasksMovedCount == null) {
            tasksMovedCount = 0;
            session.setAttribute("tasksMovedCount", tasksMovedCount);
        }

        request.getRequestDispatcher("/task-board.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        User user = (User) session.getAttribute("user");
        String taskIdStr = request.getParameter("taskId");
        String newStatus = request.getParameter("newStatus");

        if (taskIdStr == null || newStatus == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        try {
            int taskId = Integer.parseInt(taskIdStr);
            boolean success = DatabaseUtil.updateTaskStatus(taskId, newStatus, user.getId());

            if (success) {
                // Increment tasks moved count
                Integer tasksMovedCount = (Integer) session.getAttribute("tasksMovedCount");
                tasksMovedCount = (tasksMovedCount == null) ? 1 : tasksMovedCount + 1;
                session.setAttribute("tasksMovedCount", tasksMovedCount);

                response.setStatus(HttpServletResponse.SC_OK);
                response.getWriter().write("Success");
            } else {
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            }
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
        }
    }
}