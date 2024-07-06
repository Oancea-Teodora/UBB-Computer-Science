import random

from src.Board import board


class GameLostException(Exception):
    def __str__(self):
        return "GAME OVER. People starved."


class InvalidGrainsConfigurationError(Exception):
    def __str__(self):
        return "Invalid grains configuration. Not enough grains."


class InsufficientAcresSellError(Exception):
    def __str__(self):
        return "You don't have that many acres to sell, try less."


class InsufficientAcresBuyError(Exception):
    def __str__(self):
        return "You don't have enough grains to buy that many acres."


class OverfeedPopulationError(Exception):
    def __str__(self):
        return "You can only allocate 20 food per 1 person."


class InsufficientGrainsFoodError(Exception):
    def __str__(self):
        return "You don't have that much food to feed the population."


class InsufficientGrainsPlantError(Exception):
    def __str__(self):
        return "You don't have that much grain to plant."


class PlanOverflowError(Exception):
    def __str__(self):
        return "You cannot plant more acres than you have."

class OverworkPeopleError(Exception):
    def __str__(self):
        return "Each person can work at most 10 acres."


class Game:
    def __init__(self, board: board):
        self._board = board

        self._land_price = 20
        self._starved_people = 0  # how many people starved
        self._new_people = 0  # how many new people in city
        self._rat_stolen_food = 0  # how much stolen food, 0 means no rats
        self._yearly_yield = 3  # how much harvested this year per acre

        self._game_year = 0

        self._game_state = 0  # 0 - ongoing, 1 - won, -1 - lost

    def get_current_year(self):
        """
        This method returns the current year of the game.
        :return: the current year of the game
        """
        return self._game_year

    def compute_new_year(self, land_trade: int, population_food: int, planted: int):
        """
        This method computes the new year based on the input parameters.
        :param land_trade: how much land we trade
        :param population_food: how much food we allocate for the population
        :param planted: how much land we plant
        :return:
        """
        self.__validate_input(land_trade, population_food, planted)

        self._game_year += 1

        self.__compute_land_change(land_trade)

        self.__compute_starvation(population_food)

        self.__compute_grain_harvesting(planted)

        self.__compute_rat_infestation()

        self.__compute_new_land_price()

        self.__compute_win_condition()

        self._board.set_planted_acres(planted)

        return (self._game_year, self._starved_people, self._new_people, self._board.get_population(),
                self._board.get_available_acres(), self._yearly_yield, self._rat_stolen_food,
                self._land_price, self._board.get_grains(),
                self._game_state)

    def __compute_land_change(self, land_trade: int):
        """
        This method computes the land change based on the input parameter.
        :param land_trade: the amount of land we trade
        :return: land change
        """
        if land_trade > 0:
            self._board.buy_land(land_trade, self._land_price)
        elif land_trade < 0:
            self._board.sell_land(land_trade, self._land_price)

    def __compute_starvation(self, population_food: int):
        """
        This method computes the starvation based on the input parameter.
        :param population_food:
        :return:
        """
        population = self._board.get_population()

        self._board.remove_grains(population_food)

        potential_fed_population = population_food // 20

        if potential_fed_population < population:
            self._starved_people = population - potential_fed_population

            if self._starved_people >= population // 2:
                self._game_state = -1  # game lost

            self._board.update_population(potential_fed_population)

        else:
            self._starved_people = 0

            if self._game_year != 1:
                self._new_people = random.randint(0, 11)  # between 0 and 10 new people
                new_population = population + self._new_people

                self._board.update_population(new_population)

    def __compute_grain_harvesting(self, planted: int):
        """
        This method computes the grain harvesting based on the input parameter.
        :param planted:
        :return:
        """
        if self._game_year != 1:
            self._yearly_yield = random.randint(1, 7)  # between 1 and 6
        harvest = self._yearly_yield * self._board.get_planted_acres()

        self._board.remove_grains(planted)

        self._board.add_grains(harvest)

    def __compute_rat_infestation(self):
        """
        This method computes the rat infestation.
        :return:
        """
        # rats_infestation_probability = 0
        # if self._game_year == 1:
        #     rats_infestation_probability = 200
        # else:
       # print(self._game_year)
        self._rat_stolen_food = 0
        if self._game_year == 1:
            rats_infestation_probability = 90
        else:
            rats_infestation_probability = random.randint(0, 101)  # get random value [0, 100]
       # print(rats_infestation_probability)
        if rats_infestation_probability >= 80:  # if between 80 and 100 which are 20 values consecutively
            grains = self._board.get_grains()
            if self._game_year == 1:
                self._rat_stolen_food = 200
            else:
               # nr = random.randint(0, 10)
                self._rat_stolen_food = grains * 10 // 100
         #   print(self._rat_stolen_food)
            self._board.remove_grains(self._rat_stolen_food)

    def __compute_new_land_price(self):
        """
        This method computes the new land price and sets it.
        :return:
        """
        if self._game_year == 1:
            self._land_price = 20
        else:
            self._land_price = random.randint(15, 25)

    def __compute_win_condition(self):
        population = self._board.get_population()
        acres = self._board.get_available_acres()

        if population > 100 and acres > 1000:
            self._game_state = 1  # won
        elif self._game_year == 5:
            self._game_state = -1  # lost

    def __validate_input(self, land_change: int, population_food: int, planted: int):
        grains = self._board.get_grains()
        acres = self._board.get_available_acres()
        population = self._board.get_population()

        if land_change > 0:
            if grains < land_change * self._land_price:
                raise InsufficientAcresBuyError()
        elif land_change < 0:
            if land_change > acres:
                raise InsufficientAcresSellError()

        if population_food > population * 20:
            raise OverfeedPopulationError()

        if population_food > grains:
            raise InsufficientGrainsFoodError()

        if planted > grains:
            raise InsufficientGrainsPlantError()

        if planted > acres + land_change:
            raise PlanOverflowError()

        if planted > population * 10:
            raise OverworkPeopleError()

        potential_grains = grains - land_change * self._land_price

        if potential_grains < population_food + planted:
            raise InvalidGrainsConfigurationError()
