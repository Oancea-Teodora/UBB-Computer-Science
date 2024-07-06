class Board:
    def __init__(self, acres: int, population: int):
        self._available_acres = acres
        self._planted_acres = self._available_acres

        self._population = population
        self._grains = 2800

    def get_population(self):
        return self._population

    def get_available_acres(self):
        return self._available_acres

    def get_planted_acres(self):
        return self._planted_acres

    def set_planted_acres(self, quantity: int):
        self._planted_acres = quantity

    def get_grains(self):
        return self._grains

    def add_grains(self, quantity: int):
        self._grains += quantity

    def remove_grains(self, quantity: int):
        self._grains -= quantity

    def buy_land(self, quantity: int, price: int):
        self._available_acres += quantity
        self._grains -= quantity * price

    def sell_land(self, quantity: int, price: int):
        self._available_acres -= -quantity
        self._grains += -quantity * price

    def update_population(self, quantity: int):
        self._population = quantity

