package com.example.servlets;

import com.example.util.DBUtil;
import javax.servlet.ServletException;
import javax.servlet.http.*;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Date;

public class ReserveServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Integer userId = (Integer) req.getSession().getAttribute("userId");
        if (userId == null) {
            resp.sendRedirect("login");
            return;
        }

        int roomId = Integer.parseInt(req.getParameter("roomId"));
        Date checkIn = Date.valueOf(req.getParameter("checkIn"));
        Date checkOut = Date.valueOf(req.getParameter("checkOut"));
        int guests = Integer.parseInt(req.getParameter("guests"));

        try {

        } catch (Exception e) {
            throw new ServletException(e);
        }
    }
}