package org.example.jspspringboot.entity;

import jakarta.persistence.*;

@Entity
public class HotelRoom {
    @Id
    @GeneratedValue
    private Integer id;

    private String roomNumber;
    private Integer capacity;
    private Integer basePrice;

    // Default constructor
    public HotelRoom() {
    }

    public HotelRoom(String roomNumber, Integer capacity, Integer basePrice) {
        this.roomNumber = roomNumber;
        this.capacity = capacity;
        this.basePrice = basePrice;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    public Integer getBasePrice() {
        return basePrice;
    }

    public void setBasePrice(Integer basePrice) {
        this.basePrice = basePrice;
    }
}