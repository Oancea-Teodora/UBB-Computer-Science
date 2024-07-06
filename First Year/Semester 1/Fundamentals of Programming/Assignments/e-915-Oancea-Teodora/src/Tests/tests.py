import unittest
from unittest.mock import patch

import src
from src.Board.board import Board
from src.Game.game import Game
import src

class TestGameYearProcessing(unittest.TestCase):

    def setUp(self):
        # Set up a board with known values for acres, population, and grains
        self.board = Board(acres=1000, population=100)
        self.game = Game(self.board)

    def test_game_initial_state(self):
        # Test that the game starts with the correct initial state
        self.assertEqual(self.game.get_current_year(), 0)

    def test_compute_new_year(self):
        # Test compute_new_year with valid inputs and no exceptions
        year, starved, new_people, population, acres, harvest, rats, land_price, grains, game_state = \
            self.game.compute_new_year(0, 2000, 1000)

        self.assertEqual(year, 1)
        self.assertEqual(starved, 0)
        self.assertEqual(new_people, self.game._new_people)
        self.assertEqual(population, self.board.get_population())
        self.assertEqual(acres, self.board.get_available_acres())
        self.assertTrue(0 <= harvest <= 6)  # Assuming harvest per acre is random between 1 and 6
        self.assertTrue(0 <= rats <= grains * 0.1)  # Assuming rats can eat up to 10% of grains
        self.assertTrue(15 <= land_price <= 25)  # Land price is random between 15 and 25
        self.assertEqual(grains, self.board.get_grains())
        self.assertEqual(game_state, self.game._game_state)

    def test_rat_infestation(self):
        # Mock randint to simulate rat infestation
        src.Game.game.random.randint = lambda x, y: 80 # Ensure that rat infestation occurs 80% of the time
        self.game.compute_new_year(0, 2000, 1000)
        self.assertTrue(self.game._rat_stolen_food > 0)

    def test_no_starvation(self):
        # Mock randint to control population growth and harvest
        src.Game.game.random.randint = lambda x, y: 10 # No new people, moderate harvest
        _, starved, _, _, _, _, _, _, _, _ = self.game.compute_new_year(0, 2000, 1000)
        self.assertEqual(starved, 0)

    def test_starvation(self):
        # Mock randint to control population growth and harvest
        src.Game.game.random.randint = lambda x, y: 10 # No new people, moderate harvest
        # Intentionally not providing enough food for population
        _, starved, _, _, _, _, _, _, _, _ = self.game.compute_new_year(0, 100, 1000)
        self.assertTrue(starved > 0)



if __name__ == '__main__':
    unittest.main()
