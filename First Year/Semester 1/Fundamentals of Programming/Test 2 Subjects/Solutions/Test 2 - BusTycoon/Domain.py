

class BusRoute:
    def __init__(self, route_code, route_length):
        self.__route_code = route_code
        self.__route_length = route_length

    @property
    def route_code(self):
        return self.__route_code

    @property
    def route_length(self):
        return self.__route_length

    @route_code.setter
    def route_code(self, route_code):
        self.__route_code = route_code

    @route_length.setter
    def route_length(self, route_length):
        self.__route_length = route_length

    def __str__(self):
        return "Route code: " + str(self.__route_code) + ", Route length: " + str(self.__route_length)


class Buses:
    def __init__(self, bus_id, bus_code, bus_model, bus_times):
        self.__bus_id = bus_id
        self.__bus_code = bus_code
        self.__bus_model = bus_model
        self.__bus_times = bus_times

    @property
    def bus_id(self):
        return self.__bus_id

    @property
    def bus_code(self):
        return self.__bus_code

    @property
    def bus_model(self):
        return self.__bus_model

    @property
    def bus_times(self):
        return self.__bus_times

    @bus_id.setter
    def bus_id(self, bus_id):
        self.__bus_id = bus_id

    @bus_code.setter
    def bus_code(self, bus_code):
        self.__bus_code = bus_code

    @bus_model.setter
    def bus_model(self, bus_model):
        self.__bus_model = bus_model

    @bus_times.setter
    def bus_times(self, bus_times):
        self.__bus_times = bus_times

    def __str__(self):
        return "Bus ID: " + str(self.__bus_id) + ", Bus code: " + str(self.__bus_code) + ", Bus model: " + str(self.__bus_model) + ", Number of times: " + str(self.__bus_times)