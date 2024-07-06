from Service import *


class UI:
    @staticmethod
    def menu():
        print("Menu")
        print("1 - Display by a route code")
        print("2 - Display for a bus the length in kilometers")
        print("3 - Display the mileage list")
        print("4 - Exit")

    def __init__(self, bus_service: BusService):
        self.__bus_service = bus_service

    def start(self):
        self.menu()
        self.__bus_service.load_file("bus_routes", "buses")
        while True:

           # try:
            print("Introduce the option: ")
            opp = input(">>")
            opp = int(opp)
            if opp == 1:
                route_code = int(input("Introduce the route code: "))
                arr = self.__bus_service.display_buses_route_code(route_code)
                for i in arr:
                    print(i)
            elif opp == 2:
                bus_id = int(input("Introduce the bus ID: "))
                arr = (self.__bus_service.kilometers(bus_id))
                print("Kilometers: ", arr)
                print(self.__bus_service.get_bus_by_id(bus_id))

            elif opp == 3:
                arr = self.__bus_service.mileage()
                for i in arr:
                    print(i['1'])
                    nr = i['2']
                    #print(nr)
                    print(self.__bus_service.get_route_by_id(nr))
            elif opp == 4:
                print("Bye!")
                break
            else:
                print("Invalid option!")
         #   except Exception as ex:
          #      print(ex)





