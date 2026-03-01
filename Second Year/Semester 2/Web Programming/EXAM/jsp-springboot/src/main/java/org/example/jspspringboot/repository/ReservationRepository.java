package org.example.jspspringboot.repository;

import org.example.jspspringboot.entity.Reservation;
import org.example.jspspringboot.entity.HotelRoom;
import org.example.jspspringboot.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Integer> {
    @Query("SELECT COUNT(res) FROM Reservation res WHERE res.room = :room "
            + " AND :checkIn < res.checkOutDate AND :checkOut > res.checkInDate")
    long countOverlapping(@Param("room") HotelRoom room,
            @Param("checkIn") LocalDate checkIn,
            @Param("checkOut") LocalDate checkOut);

    @Query("SELECT SUM(res.numberOfGuests) FROM Reservation res WHERE :date BETWEEN res.checkInDate AND res.checkOutDate")
    Long countGuestsOn(@Param("date") LocalDate date);

    List<Reservation> findByUser(User user);

    @Query("SELECT r FROM Reservation r WHERE r.roomId = :roomId AND ((r.checkInDate <= :checkOut AND r.checkOutDate >= :checkIn))")
    List<Reservation> findOverlappingReservations(@Param("roomId") Integer roomId,
            @Param("checkIn") LocalDate checkIn,
            @Param("checkOut") LocalDate checkOut);

    @Query("SELECT r FROM Reservation r WHERE r.userId = :userId AND ((r.checkInDate <= :checkOut AND r.checkOutDate >= :checkIn))")
    List<Reservation> findUserOverlappingReservations(@Param("userId") Integer userId,
            @Param("checkIn") LocalDate checkIn,
            @Param("checkOut") LocalDate checkOut);

    List<Reservation> findByUserId(Integer userId);

    @Query("SELECT r FROM Reservation r WHERE r.checkInDate <= :date AND r.checkOutDate >= :date")
    List<Reservation> findReservationsForDate(@Param("date") LocalDate date);

    @Query("SELECT r FROM Reservation r WHERE ((r.checkInDate <= :checkOut AND r.checkOutDate >= :checkIn))")
    List<Reservation> findReservationsInDateRange(@Param("checkIn") LocalDate checkIn,
            @Param("checkOut") LocalDate checkOut);
}