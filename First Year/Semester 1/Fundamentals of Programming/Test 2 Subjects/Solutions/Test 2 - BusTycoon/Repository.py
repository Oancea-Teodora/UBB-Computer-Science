from Domain import *


class BusRepo:
    def __init__(self):
        self._bus_list = []
        self._route_list = []

    def load_file(self, route_file, bus_file):
        with open(route_file, "r") as route_file:
            for line in route_file:
                route_data = line.strip().split(", ")
                route = BusRoute(int(route_data[0]), int(route_data[1]))
                self._route_list.append(route)

        with open(bus_file, "r") as bus_file:
            for line in bus_file:
                bus_data = line.strip().split(", ")
                bus = Buses(int(bus_data[0]), int(bus_data[1]), bus_data[2], int(bus_data[3]))
                self._bus_list.append(bus)

    def display_buses_route_code(self, route_code):
        buses_route = []
        for bus in self._bus_list:
            if route_code == bus.bus_code:
                buses_route.append(bus)
        return buses_route

    def kilometers(self, bus_id):
        km = 0
        id = 0
        for bus in self._bus_list:
            if bus.bus_id == bus_id:
                for routes in self._route_list:
                    if bus.bus_code == routes.route_code:
                        km = routes.route_length * bus.bus_times
        return km

    def mileage(self):
        mileage_list = []
        mileage_list.clear()
      #  print(self._bus_list)
        for routes in self._route_list:
            kilometers = 0
            for bus in self._bus_list:
                if bus.bus_code == routes.route_code:
                    kilometers = kilometers + routes.route_length * bus.bus_times
            mileage = {"1": kilometers, "2": routes.route_code}
            mileage_list.append(mileage)
        mileage_list = sorted(mileage_list, key=lambda x: x["1"], reverse=True)
        return mileage_list

    def get_bus_by_id(self, bus_id):
        for i in self._bus_list:
            if bus_id == i.bus_id:
                return i

    def get_route_by_id(self, route_id):
        for i in self._route_list:
            if route_id == i.route_code:
                return i
