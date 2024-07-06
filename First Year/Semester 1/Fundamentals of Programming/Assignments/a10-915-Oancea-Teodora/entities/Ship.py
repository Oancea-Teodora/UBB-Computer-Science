from entities.Board import Battlefield
from Board_Strings.board_strings import new_line, Formatting


class Ship:
    types = {"Carrier": 5, "Battleship": 4, "Destroyer": 3, "Submarine": 3, "Patrol Boat": 2}

    def __init__(self, coordinates, type, battlefield):
        self.coordinates = coordinates
        self.type = type
        self.battlefield = battlefield
        self.size = Ship.types[self.type]
        self.sunk = False
        for coordinate in self.coordinates:
            self.battlefield.grid[coordinate] = Battlefield.states[5]

    def __repr__(self):
        return "Type " + str(self.type) + ". Coordinates: " + str(self.coordinates)

    def check_sunk(self):
        """Check if the ship has sunk (all ship coordinates hit).
            If so, update the sunk attribute to True, and print a stetement identifyuing the sunk ship"""
        hit_coordinates = 0
        battlefield = self.battlefield
        for coordinate in self.coordinates:
            if battlefield.grid[coordinate] == battlefield.states[7]:
                hit_coordinates += 1
        if hit_coordinates == self.size:
            self.sunk = True
            for coordinate in self.coordinates:
                battlefield.grid[coordinate] = Battlefield.states[9]  # replace ship coords with #
            print(new_line * 2 + Formatting.line_wrap3(self.type + " HAS BEEN SUNK!!!!") + new_line * 2)
