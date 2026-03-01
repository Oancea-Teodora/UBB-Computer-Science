-- Create the database
CREATE DATABASE IF NOT EXISTS xogame;
USE xogame;

-- Create users table
CREATE TABLE IF NOT EXISTS users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(100) NOT NULL
);

-- Create game_state table
CREATE TABLE IF NOT EXISTS game_state (
    id INT PRIMARY KEY,
    player1 VARCHAR(50),
    player2 VARCHAR(50),
    board VARCHAR(9) DEFAULT '_________',  -- 9 spaces
    turn CHAR(1) DEFAULT 'X',
    status VARCHAR(20) DEFAULT 'WAITING'
);

-- Insert test users
INSERT IGNORE INTO users (username, password) VALUES 
('alice', 'alice123'),
('bob', 'bob123'),
('charlie', 'charlie123'),
('diana', 'diana123');

-- Initialize game state
INSERT IGNORE INTO game_state (id, player1, player2, board, turn, status) VALUES 
(1, NULL, NULL, '         ', 'X', 'WAITING');

-- Show the setup
SELECT 'Users table:' as Info;
SELECT * FROM users;
SELECT 'Game state table:' as Info;
SELECT * FROM game_state; 