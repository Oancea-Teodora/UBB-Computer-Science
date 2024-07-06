from Repository import *


class PlayerService:
    def __init__(self, repo: PlayerRepo):
        self.__repo = repo

    def load_file(self, name_file):
        self.__repo.load_file(name_file)

    def power_of_2(self, n):
        return self.__repo.power_of_2(n)

    def display_players(self):
        return self.__repo.display_players()

    def qualifications(self, eliminate):
        return self.__repo.qualification_round(eliminate)

    def create_pairings(self, n):
        """
        It gives the pairs
        :param n:number of players
        :return: the pairs
        """
        return self.__repo.create_pairings(n)

    def play(self, eliminate, increase):
        self.__repo.play(eliminate, increase)