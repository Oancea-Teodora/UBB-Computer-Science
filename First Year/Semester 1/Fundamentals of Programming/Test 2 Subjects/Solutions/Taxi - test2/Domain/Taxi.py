
class Taxi:
    def __init__(self, taxi_id, taxi_x, taxi_y, taxi_fare):
        self.__taxi_id = taxi_id
        self.__taxi_x = taxi_x
        self.__taxi_y = taxi_y
        self.__taxi_fare = taxi_fare

    @property
    def taxi_id(self):
        return self.__taxi_id

    @property
    def taxi_x(self):
        return self.__taxi_x

    @property
    def taxi_y(self):
        return self.__taxi_y

    @property
    def taxi_fare(self):
        return self.__taxi_fare

    @taxi_id.setter
    def taxi_id(self, taxi_id):
        self.__taxi_id = taxi_id

    @taxi_x.setter
    def taxi_x(self, taxi_x):
        self.__taxi_x = taxi_x

    @taxi_y.setter
    def taxi_y(self, taxi_y):
        self.__taxi_y = taxi_y

    def __str__(self):
        return "Taxi ID: " + str(self.__taxi_id) + " Coordonate X: " + str(self.__taxi_x) + " Coordonate Y: " + str(self.__taxi_y) + " Taxi fare: " + str(self.taxi_fare)

    @taxi_fare.setter
    def taxi_fare(self, taxi_fare):
        self.__taxi_fare = taxi_fare
