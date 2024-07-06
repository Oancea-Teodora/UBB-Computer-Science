from Service.taxi_service import *


class UI:
    @staticmethod
    def menu():
        print("Menu")
     #   print("1 -Generate taxis")
        print("2 - Add a ride")
        print("3 - Simulate a ride")
        print("4 - Exit")

    def __init__(self, taxi_service: TaxiService):
        self.__taxi_service = taxi_service

    def start(self):
        self.menu()
        try:
            number_of_taxis = int(input("Introduce the number of taxis: "))
        except ValueError as ve:
            print(ve)
        try:
            self.__taxi_service.generate(number_of_taxis)
        except ValueError as ve:
            print(ve)
        while True:
            try:
                opp = input(">>")
                opp = int(opp)
                if opp == 1:
                    pass
                elif opp == 2:
                    try:
                        start_x = int(input("Introduce the starting point x: "))
                        start_y = int(input("Introduce the starting point y: "))
                        end_x = int(input("Introduce the final point x: "))
                        end_y = int(input("Introduce the final point y: "))
                        self.__taxi_service.add_ride(start_x, start_y, end_x, end_y)
                        arr = self.__taxi_service.get_all_taxis()
                        for i in arr:
                            print(i)
                    except ValueError as ve:
                        print(ve)
                elif opp == 3:
                    self.__taxi_service.simulate_rides()
                    arr = self.__taxi_service.get_all_taxis()
                    arr.sort(key=lambda i: i.taxi_fare, reverse=True)
                    for i in arr:
                        print(i)
                elif opp == 4:
                    print("Bye!")
                    break
                else:
                    print("Invalid option!")
            except ValueError as ve:
                print(ve)
