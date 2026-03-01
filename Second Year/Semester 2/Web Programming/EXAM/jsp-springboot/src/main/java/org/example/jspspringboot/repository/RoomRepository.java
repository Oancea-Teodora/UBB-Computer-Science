package org.example.jspspringboot.repository;

import org.example.jspspringboot.entity.HotelRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface RoomRepository extends JpaRepository<HotelRoom, Integer> {
    @Query("SELECT r FROM HotelRoom r WHERE r.id NOT IN (" +
            " SELECT res.room.id FROM Reservation res " +
            "  WHERE :checkIn < res.checkOutDate AND :checkOut > res.checkInDate )")
    List<HotelRoom> findAvailable(@Param("checkIn") LocalDate checkIn, @Param("checkOut") LocalDate checkOut);
}