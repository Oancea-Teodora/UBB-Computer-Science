import math

from Domain import *
from random import randint, choice


class PlayerRepo:
    def __init__(self):
        self._list = []

    @staticmethod
    def power_of_2(n):
        copy = n
        while copy % 2 == 0:
            copy = copy / 2
        if copy == 1:
            return 1
        else:
            return 0

    def load_file(self, name_file):
        with open(name_file, "r") as name_file:
            for line in name_file:
                data = line.strip().split(", ")
                player = Player(int(data[0]), data[1], int(data[2]))
                self._list.append(player)

    def display_players(self):
        self._list = sorted(self._list, key=lambda x: x.strength, reverse=True)
        return self._list

    def create_pairings(self, n):
        """
        It creates random pairings in the list of the players
        :param n: the number of players
        :return: the pairs
        """
        arr = self._list
        choice1 = 0
        choice2 = 0
        while choice2 == choice1:
            choice1 = randint(0, n - 1)
            choice2 = randint(0, n - 1)
        return choice1, choice2

    def qualification_round(self, eliminate):
        """
        It creates random pairings in the list of the players
        :param eliminate: the pair that is eliminated
        :return:
        """
        n = len(self._list)
        if self.power_of_2(n) == 0:
            rounds = "Qualifications"
            choice1 = n-2
            choice2 = n-1

            if eliminate == 1:
                self._list[choice2].strength += 1
                self._list.pop(choice1)
            else:
                self._list[choice1].strength += 1
                self._list.pop(choice2)

            return rounds, choice1, choice2, self._list

    def play(self, eliminate, increase):

        self._list[increase].strength += 1
        self._list.pop(eliminate)

