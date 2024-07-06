from Service import *


class UI:
    @staticmethod
    def menu():
        print("Menu")
        print("1 - Start")

    def __init__(self, serv: PlayerService):
        self.__serv = serv

    def start(self):
        self.menu()
        self.__serv.load_file("players.txt")
        opp = int(input(">>"))
        while opp == 1:
            arr = self.__serv.display_players()
            n = len(arr)
            while n > 1:
                try:
                    if self.__serv.power_of_2(n) == 0:
                        while self.__serv.power_of_2(n) == 0:
                            for i in arr:
                                print(i)
                            print("Qualifications")
                            print("Choose the winner between: ", arr[n-2].name, "vs", arr[n-1].name)
                            print("Choose 1 or 2")
                            eliminate1 = int(input())
                            eliminate = -1
                            if eliminate1 == 1:
                                eliminate = 2
                            elif eliminate1 == 2:
                                eliminate = 1
                            else:
                                print("Invalid input!")
                            if eliminate != -1:
                                arr2 = self.__serv.qualifications(eliminate)
                                n = n-1
                    else:
                        copy = n
                        while len(arr) >= 1:
                            for i in arr:
                                print(i)
                            print("Last", copy)
                            pair = self.__serv.create_pairings(n)
                            choice1 = pair[0]
                            choice2 = pair[1]
                            print("Choose the winner between: ", arr[choice1].name, "vs", arr[choice2].name)
                            print("Choose 1 or 2")
                            eliminate = int(input())
                            if eliminate == 1:
                                self.__serv.play(choice2, choice1)
                                n = n-1
                            elif eliminate == 2:
                                self.__serv.play(choice1, choice2)
                                n = n-1
                            else:
                                print("Invalid input!")
                            if n == 1:
                                print("The winner is: ")
                                for i in arr:
                                    print(i, "!")
                                break
                            if self.__serv.power_of_2(n) == 1:
                                copy = n
                except ValueError as ve:
                    print(ve)
            if n == 1:
                break



