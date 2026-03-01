package org.example.jspspringboot.controller;

import org.example.jspspringboot.entity.HotelRoom;
import org.example.jspspringboot.entity.Reservation;
import org.example.jspspringboot.entity.User;
import org.example.jspspringboot.service.HotelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpSession;
import java.time.LocalDate;
import java.util.List;

@Controller
public class HotelController {

    @Autowired
    private HotelService hotelService;

    @GetMapping("/")
    public String login() {
        return "login";
    }

    @PostMapping("/login")
    public String authenticate(@RequestParam String username,
            @RequestParam Integer password,
            HttpSession session,
            Model model) {
        User user = hotelService.authenticate(username, password);
        if (user != null) {
            session.setAttribute("user", user);
            return "redirect:/home";
        } else {
            model.addAttribute("error", "Invalid credentials");
            return "login";
        }
    }

    @GetMapping("/home")
    public String home(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/";
        }
        model.addAttribute("user", user);
        return "home";
    }

    @GetMapping("/rooms")
    public String viewRooms(@RequestParam(required = false) String checkIn,
            @RequestParam(required = false) String checkOut,
            HttpSession session,
            Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/";
        }

        List<HotelRoom> rooms;
        if (checkIn != null && checkOut != null && !checkIn.isEmpty() && !checkOut.isEmpty()) {
            LocalDate checkInDate = LocalDate.parse(checkIn);
            LocalDate checkOutDate = LocalDate.parse(checkOut);
            rooms = hotelService.getAvailableRooms(checkInDate, checkOutDate);
            model.addAttribute("checkIn", checkIn);
            model.addAttribute("checkOut", checkOut);
        } else {
            rooms = hotelService.getAllRooms();
        }

        model.addAttribute("rooms", rooms);
        return "rooms";
    }

    @PostMapping("/reserve")
    public String makeReservation(@RequestParam Integer roomId,
            @RequestParam String checkIn,
            @RequestParam String checkOut,
            @RequestParam Integer numberOfGuests,
            HttpSession session,
            Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/";
        }

        LocalDate checkInDate = LocalDate.parse(checkIn);
        LocalDate checkOutDate = LocalDate.parse(checkOut);

        String result = hotelService.makeReservation(user.getId(), roomId, checkInDate, checkOutDate, numberOfGuests);
        model.addAttribute("message", result);

        return "redirect:/reservations";
    }

    @GetMapping("/reservations")
    public String viewReservations(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/";
        }

        List<Reservation> reservations = hotelService.getUserReservations(user.getId());
        model.addAttribute("reservations", reservations);
        return "reservations";
    }

    @GetMapping("/guests")
    public String viewGuests(@RequestParam(required = false) String date,
            HttpSession session,
            Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/";
        }

        if (date != null && !date.isEmpty()) {
            LocalDate searchDate = LocalDate.parse(date);
            Integer totalGuests = hotelService.getTotalGuestsForDate(searchDate);
            model.addAttribute("totalGuests", totalGuests);
            model.addAttribute("searchDate", date);
        }

        return "guests";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }
}