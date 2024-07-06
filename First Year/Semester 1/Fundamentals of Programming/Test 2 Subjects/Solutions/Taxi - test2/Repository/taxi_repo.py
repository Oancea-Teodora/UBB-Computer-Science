from Domain.Taxi import *
from random import randint


class TaxiRepo:
    def __init__(self):
        self._taxi_list = []

    def verify_distance(self, x, y):
        ok = 1
        for taxi in self._taxi_list:
            dist = abs(x - taxi.taxi_x) + abs(y - taxi.taxi_y)
            if dist <= 5:
                ok = 0
        return ok

    def generate(self, number_of_taxis):
        if number_of_taxis < 0 or number_of_taxis > 10:
            raise ValueError("Invalid number of taxis!")
        for i in range(1, number_of_taxis+1):
            taxi_id = i
            taxi_x = -1
            taxi_y = -1
            taxi_fare = 0
            while taxi_x == -1 or taxi_y == -1:
                x = randint(0, 100)
                y = randint(0, 100)
                if self.verify_distance(x, y) == 1:
                    taxi_x = x
                    taxi_y = y
            taxi = Taxi(taxi_id, taxi_x, taxi_y, taxi_fare)
            self._taxi_list.append(taxi)

    def add_taxi(self, taxi):
        self._taxi_list.append(taxi)

    def add_ride(self, start_x, start_y, end_x, end_y):
        if start_x < 0 or start_x > 100 or start_y < 0 or start_y > 100 or end_x < 0 or end_x > 100 or end_y < 0 or end_y > 100:
            raise ValueError("Invalid coordinates!")
        min_dist = 300
        used_taxi = -1
        for taxi in self._taxi_list:
            dist = abs(start_x - taxi.taxi_x) + abs(start_y - taxi.taxi_y)
            if dist < min_dist:
                min_dist = dist
                used_taxi = taxi.taxi_id
        dist = abs(end_x - start_x) + abs(end_y - start_y)
        for taxi in self._taxi_list:
            if taxi.taxi_id == used_taxi:
                taxi.taxi_x = end_x
                taxi.taxi_y = end_y
                taxi.taxi_fare = taxi.taxi_fare + dist

    def simulate_rides(self):
        dist = -1
        start_x = -1
        start_y = -1
        end_x = -1
        end_y = -1
        while dist < 10:
            start_x = randint(0, 100)
            start_y = randint(0, 100)
            end_x = randint(0, 100)
            end_y = randint(0, 100)
            dist = abs(end_x - start_x) + abs(end_y - start_y)
        self.add_ride(start_x, start_y, end_x, end_y)

    def get_all_taxi(self):
        return self._taxi_list

