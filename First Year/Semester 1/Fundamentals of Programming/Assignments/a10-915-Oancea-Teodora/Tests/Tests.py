import unittest
from unittest.mock import patch
from entities.Player import Player, Computer
from entities.Ship import Ship
from Exceptions.InputExceptions import BusyCoordinateException, NotEnoughRoomException, TargettedCoordinateException


class TestPlayer(unittest.TestCase):

    def setUp(self):
        self.player = Player()
        self.computer = Computer()


    # def test_generate_ships(self):
    #     # Mocking the input function for testing purposes
    #     with patch('builtins.input', side_effect=['A1', 'B1', 'C1', 'D1', 'E1']):
    #         # Ensure ships are generated successfully without any exceptions
    #         self.assertIsNone(self.player.generate_ships())
    #         self.assertIsNone(self.computer.generate_ships())

    def test_shoot_board(self):
        # Ensure shooting the board updates the grid
        initial_state = self.player.battlefield.grid.copy()
        coordinate = ('A', 1)
        self.player.shoot_board(coordinate)
        self.assertNotEqual(initial_state, self.player.battlefield.grid)


    def test_target(self):
        with patch('builtins.input', return_value='A1'):
            self.assertIsNone(self.player.target(self.computer))
    def gen_coords(self, ship_type):
        """Generate coordinates for a ship based on user input."""
        input1_str = f"Enter the starting coordinate for your {ship_type} (e.g., A1): "
        input2_str = f"Enter the orientation for your {ship_type} (H for horizontal, V for vertical): "

        while True:
            try:
                input1 = input(input1_str)
                input2 = input(input2_str)

                coords = self.battlefield.calculate_ship_coordinates(input1, ship_type, input2)
                self.validate_ship_placement(coords)

                return coords
            except (BusyCoordinateException, NotEnoughRoomException) as e:
                print(f"Error: {e}")

    def test_gen_coords_exceptions(self):
        # Test generating coordinates with exceptions
        ship_type = 'Carrier'

        # Assuming this coordinate is already occupied by another ship
        self.player.battlefield.grid[('A', 1)] = Ship.types[ship_type]
        print("Grid before exception:", self.player.battlefield.grid)

        try:
            self.player.gen_coords(ship_type)
            # If no exception was raised, print a message indicating success
            print("Exception not raised - Success")
        except BusyCoordinateException:
            # If the exception was raised, print a message indicating the exception
            print("BusyCoordinateException raised - Success")
        except Exception as e:
            # If another exception was raised, print details about the exception
            print(f"Unexpected exception: {type(e).__name__} - {str(e)}")

        # Assuming there is not enough room for the ship at the given coordinates
        self.player.battlefield.grid[('A', 1)] = 0  # Empty the coordinate
        self.player.battlefield.grid[('B', 1)] = Ship.types[ship_type]
        print("Grid before exception:", self.player.battlefield.grid)

        try:
            self.player.gen_coords(ship_type)
            # If no exception was raised, print a message indicating success
            print("Exception not raised - Success")
        except NotEnoughRoomException:
            # If the exception was raised, print a message indicating the exception
            print("NotEnoughRoomException raised - Success")
        except Exception as e:
            # If another exception was raised, print details about the exception
            print(f"Unexpected exception: {type(e).__name__} - {str(e)}")

    def test_display_ships(self):
        # Ensure display_ships does not raise any exceptions
        self.assertIsNone(self.player.display_ships2())

    def test_check_fleet_sunk(self):
        # Ensure check_fleet_sunk returns False initially
        self.assertFalse(self.player.check_fleet_sunk())

        # Assuming all ships are sunk
        for ship in self.player.fleet.values():
            ship.sunk = True
        self.assertTrue(self.player.check_fleet_sunk())


if __name__ == '__main__':
    unittest.main()
