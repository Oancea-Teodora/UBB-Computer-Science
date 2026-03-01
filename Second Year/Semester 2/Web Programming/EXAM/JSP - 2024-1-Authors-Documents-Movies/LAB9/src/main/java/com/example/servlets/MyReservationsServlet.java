package com.example.servlets;

import com.example.util.DBUtil;
import javax.servlet.ServletException;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class MyReservationsServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Integer userId = (Integer) req.getSession().getAttribute("userId");
        if (userId == null) {
            resp.sendRedirect("login");
            return;
        }

        try {
            List<Map<String, Object>> myList = DBUtil.findReservationsByUser(userId);
            req.setAttribute("myRes", myList);
        } catch (Exception e) {
            throw new ServletException(e);
        }

        req.getRequestDispatcher("/myreservations.jsp").forward(req, resp);
    }
}