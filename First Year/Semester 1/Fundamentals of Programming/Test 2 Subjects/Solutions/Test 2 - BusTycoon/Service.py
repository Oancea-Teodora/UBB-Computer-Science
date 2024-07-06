from Repository import *


class BusService:

    def __init__(self, bus_repo: BusRepo):
        self.__bus_repo = bus_repo

    def load_file(self, route_file, bus_file):
        self.__bus_repo.load_file(route_file, bus_file)

    def display_buses_route_code(self, route_code):
        return self.__bus_repo.display_buses_route_code(route_code)

    def kilometers(self, bus_id):
        return self.__bus_repo.kilometers(bus_id)

    def mileage(self):
        return self.__bus_repo.mileage()

    def get_bus_by_id(self, bus_id):
        return self.__bus_repo.get_bus_by_id(bus_id)

    def get_route_by_id(self, route_code):
        return self.__bus_repo.get_route_by_id(route_code)
