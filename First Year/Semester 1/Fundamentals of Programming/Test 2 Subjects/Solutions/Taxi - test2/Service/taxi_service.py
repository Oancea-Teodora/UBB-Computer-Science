from Repository.taxi_repo import *


class TaxiService:
    def __init__(self, taxi_repo: TaxiRepo):
        self.__taxi_repo = taxi_repo

    def add_taxi(self, taxi: Taxi):
        self.__taxi_repo.add_taxi(taxi)

    def generate(self, number_of_taxis):
        self.__taxi_repo.generate(number_of_taxis)

    def add_ride(self, start_x, start_y, end_x, end_y):
        self.__taxi_repo.add_ride(start_x, start_y, end_x, end_y)

    def simulate_rides(self):
        self.__taxi_repo.simulate_rides()

    def get_all_taxis(self):
        return self.__taxi_repo.get_all_taxi()