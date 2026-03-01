package com.example.servlets;

import com.example.util.DBUtil;
import javax.servlet.ServletException;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.Date;
import java.util.List;
import java.util.Map;

public class AvailabilityServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/availability.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Integer userId = (Integer) req.getSession().getAttribute("userId");
        if (userId == null) {
            resp.sendRedirect("login");
            return;
        }

        Date checkIn = Date.valueOf(req.getParameter("checkIn"));
        Date checkOut = Date.valueOf(req.getParameter("checkOut"));

        try {
            List<Map<String, Object>> freeRooms = DBUtil.findAvailableRooms(checkIn, checkOut);
            int guestCount = DBUtil.countGuestsOn(checkIn);

            req.setAttribute("freeRooms", freeRooms);
            req.setAttribute("guestCount", guestCount);
            req.setAttribute("checkIn", req.getParameter("checkIn"));
            req.setAttribute("checkOut", req.getParameter("checkOut"));
        } catch (Exception e) {
            throw new ServletException(e);
        }

        req.getRequestDispatcher("/availability.jsp").forward(req, resp);
    }
}