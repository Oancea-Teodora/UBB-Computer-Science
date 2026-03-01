package org.example.jspspringboot.config;

import org.example.jspspringboot.entity.HotelRoom;
import org.example.jspspringboot.entity.User;
import org.example.jspspringboot.repository.RoomRepository;
import org.example.jspspringboot.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoomRepository roomRepository;

    @Override
    public void run(String... args) throws Exception {
        // Create sample users
        if (userRepository.count() == 0) {
            userRepository.save(new User("john", "123"));
            userRepository.save(new User("jane", "456"));
            userRepository.save(new User("admin", "999"));
        }

        // Create sample rooms
        if (roomRepository.count() == 0) {
            roomRepository.save(new HotelRoom("101", 2, 100));
            roomRepository.save(new HotelRoom("102", 2, 120));
            roomRepository.save(new HotelRoom("103", 4, 200));
            roomRepository.save(new HotelRoom("201", 2, 150));
            roomRepository.save(new HotelRoom("202", 3, 180));
        }
    }
}