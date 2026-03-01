package org.example.jspspringboot.service;

import org.example.jspspringboot.entity.HotelRoom;
import org.example.jspspringboot.entity.Reservation;
import org.example.jspspringboot.entity.User;
import org.example.jspspringboot.repository.RoomRepository;
import org.example.jspspringboot.repository.ReservationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

import static java.time.temporal.ChronoUnit.DAYS;

@Service
public class BookingService {
    private final RoomRepository rooms;
    private final ReservationRepository reservations;

    public BookingService(RoomRepository rooms, ReservationRepository reservations) {
        this.rooms = rooms;
        this.reservations = reservations;
    }

    public List<HotelRoom> listAvailable(LocalDate in, LocalDate out) {
        return rooms.findAvailable(in, out);
    }

    public int calculatePrice(HotelRoom room, LocalDate in, LocalDate out) {
        long totalRooms = rooms.count();
        long booked = reservations.countOverlapping(room, in, out);
        double pct = booked / (double) totalRooms;

        int base = room.getBasePrice();
        if (pct > 0.8)
            return (int) (base * 1.5);
        else if (pct > 0.5)
            return (int) (base * 1.2);
        else
            return base;
    }

    @Transactional
    public Reservation book(User user, HotelRoom room, LocalDate in, LocalDate out, int guests) {
        // 1) Prevent overlapping for same user:
        boolean userOverlap = reservations.findByUser(user).stream()
                .anyMatch(r -> in.isBefore(r.getCheckOutDate()) && out.isAfter(r.getCheckInDate()));
        if (userOverlap)
            throw new IllegalStateException("Overlapping reservation");

        // 2) Compute price/day and sum:
        int days = (int) DAYS.between(in, out);
        int daily = calculatePrice(room, in, out);
        Reservation res = new Reservation();
        res.setUser(user);
        res.setRoom(room);
        res.setCheckInDate(in);
        res.setCheckOutDate(out);
        res.setNumberOfGuests(guests);
        res.setTotalPrice(daily * days);
        return reservations.save(res);
    }

    public long guestsOn(LocalDate date) {
        Long result = reservations.countGuestsOn(date);
        return result != null ? result : 0;
    }
}