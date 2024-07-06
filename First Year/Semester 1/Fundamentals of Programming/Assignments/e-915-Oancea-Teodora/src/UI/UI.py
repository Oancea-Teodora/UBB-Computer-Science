from src import Game
from src.Game import *
from src.Board import *
from src.Game.game import GameLostException


class UI:
    def __init__(self, game: Game):
        self._game = game
        self._land_change = 0
        self._population_food = 0
        self._planted = 0
        self._game_state = 0

    def start(self):
        while True:
            try:
                if self._game.get_current_year() == 0:
                    self._land_change = 0
                    self._population_food = 2000
                    self._planted = 800

                (year, starved, new_people, population,
                 acres, harvest, rats,
                 land_price, grains,
                 self._game_state) = self._game.compute_new_year(self._land_change, self._population_food, self._planted)

                print('\n\n')
                print(f'In year {year}.')
                print(f'{starved} people starved. {new_people} people came to the city. ')
                print(f'City population is {population}.')
                print(f'City owns {acres} acres of land. Harvest was {harvest} units per acre.')

                if rats >= 0:
                    print(f'Rats ate {rats} units.')

                print(f'Land price is {land_price} units per acre.')
                print(f'Grains stocks are {grains} units.')

                if self._game_state == 1:
                    print("GAME OVER. You won.")
                    return
                elif self._game_state == -1:
                    print("GAME OVER. You lost.")
                    return

            except GameLostException as ex:
                print(ex)

            except Exception as ex:
                print(ex)

            while True:
                try:
                    self._land_change = int(input("Acres to buy/sell(+/-) -> "))
                    self._population_food = int(input("Units to feed the population -> "))
                    self._planted = int(input("Acres to plant -> "))
                    break
                except ValueError:
                    print("Invalid input. Please insert valid numbers.")

