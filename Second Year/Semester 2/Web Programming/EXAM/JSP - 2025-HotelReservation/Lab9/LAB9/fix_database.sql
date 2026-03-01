-- Fix the board field to use underscores instead of spaces
USE xogame;

-- Update the existing game state to have a proper board with underscores
UPDATE game_state SET board = '_________' WHERE id = 1;

-- Show the updated state
SELECT id, player1, player2, board, LENGTH(board) as board_length, turn, status FROM game_state; 