package com.tictactoe.util;

import com.tictactoe.model.User;
import com.tictactoe.model.Game;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class DatabaseUtil {
    private static final String USERS_FILE = "users.txt";
    private static Map<String, User> users = new HashMap<>();
    private static Game currentGame = new Game();

    static {
        loadUsers();
        // Add default users for testing
        if (users.isEmpty()) {
            users.put("player1", new User("player1", "pass1"));
            users.put("player2", new User("player2", "pass2"));
            users.put("admin", new User("admin", "admin"));
            saveUsers();
        }
    }

    public static boolean validateUser(String username, String password) {
        User user = users.get(username);
        return user != null && user.getPassword().equals(password);
    }

    public static User getUser(String username) {
        return users.get(username);
    }

    public static void addUser(String username, String password) {
        users.put(username, new User(username, password));
        saveUsers();
    }

    public static Game getCurrentGame() {
        return currentGame;
    }

    public static void resetGame() {
        currentGame = new Game();
    }

    private static void loadUsers() {
        try (BufferedReader reader = new BufferedReader(new FileReader(USERS_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(":");
                if (parts.length == 2) {
                    users.put(parts[0], new User(parts[0], parts[1]));
                }
            }
        } catch (IOException e) {
            // File doesn't exist or can't be read, start with empty users
            System.out.println("Users file not found, creating with default users");
        }
    }

    private static void saveUsers() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(USERS_FILE))) {
            for (User user : users.values()) {
                writer.println(user.getUsername() + ":" + user.getPassword());
            }
        } catch (IOException e) {
            System.err.println("Error saving users: " + e.getMessage());
        }
    }
}