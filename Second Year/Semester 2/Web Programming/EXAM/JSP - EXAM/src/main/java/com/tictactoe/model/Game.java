package com.tictactoe.model;

public class Game {
    private String[][] board;
    private String player1;
    private String player2;
    private String currentPlayer;
    private boolean gameOver;
    private String winner;
    private int playerCount;

    public Game() {
        this.board = new String[3][3];
        initializeBoard();
        this.gameOver = false;
        this.winner = null;
        this.playerCount = 0;
    }

    private void initializeBoard() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                board[i][j] = "";
            }
        }
    }

    public boolean addPlayer(String username) {
        if (playerCount >= 2) {
            return false; // Game is full
        }

        if (player1 == null) {
            player1 = username;
            currentPlayer = player1;
        } else if (player2 == null) {
            player2 = username;
        }
        playerCount++;
        return true;
    }

    public void removePlayer(String username) {
        if (username.equals(player1)) {
            player1 = player2;
            player2 = null;
        } else if (username.equals(player2)) {
            player2 = null;
        }
        playerCount--;

        if (playerCount == 0) {
            initializeBoard();
            gameOver = false;
            winner = null;
            currentPlayer = null;
        }
    }

    public boolean makeMove(int row, int col, String player) {
        if (gameOver || !board[row][col].isEmpty() || !player.equals(currentPlayer)) {
            return false;
        }

        String symbol = player.equals(player1) ? "X" : "O";
        board[row][col] = symbol;

        if (checkWinner()) {
            gameOver = true;
            winner = currentPlayer;
        } else if (isBoardFull()) {
            gameOver = true;
            winner = "tie";
        } else {
            // Switch turns
            currentPlayer = currentPlayer.equals(player1) ? player2 : player1;
        }

        return true;
    }

    private boolean checkWinner() {
        // Check rows
        for (int i = 0; i < 3; i++) {
            if (!board[i][0].isEmpty() &&
                    board[i][0].equals(board[i][1]) &&
                    board[i][1].equals(board[i][2])) {
                return true;
            }
        }

        // Check columns
        for (int j = 0; j < 3; j++) {
            if (!board[0][j].isEmpty() &&
                    board[0][j].equals(board[1][j]) &&
                    board[1][j].equals(board[2][j])) {
                return true;
            }
        }

        // Check diagonals
        if (!board[0][0].isEmpty() &&
                board[0][0].equals(board[1][1]) &&
                board[1][1].equals(board[2][2])) {
            return true;
        }

        if (!board[0][2].isEmpty() &&
                board[0][2].equals(board[1][1]) &&
                board[1][1].equals(board[2][0])) {
            return true;
        }

        return false;
    }

    private boolean isBoardFull() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[i][j].isEmpty()) {
                    return false;
                }
            }
        }
        return true;
    }

    public void resetGame() {
        initializeBoard();
        gameOver = false;
        winner = null;
        currentPlayer = player1;
    }

    // Getters and setters
    public String[][] getBoard() {
        return board;
    }

    public String getPlayer1() {
        return player1;
    }

    public String getPlayer2() {
        return player2;
    }

    public String getCurrentPlayer() {
        return currentPlayer;
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public String getWinner() {
        return winner;
    }

    public int getPlayerCount() {
        return playerCount;
    }

    public boolean canStart() {
        return playerCount == 2;
    }
}