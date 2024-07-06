
from Service import *


class UI:

    @staticmethod
    def menu():
        print("Menu")
        print("1 - Add a student")
        print("2 - Display students in decreasing order")
        print("3 - Give bonuses")
        print("4 - Display by a giving string")

    def __init__(self, student_serv: StudentService):
        self.__student_serv = student_serv

    def start(self):
        self.menu()
        while True:
            print("Introduce an option: ")
            opp = int(input(">>"))
            if opp == 1:
                try:
                    print("Introduce the ID: ")
                    idd = int(input())
                    print("Introduce the name: ")
                    name = str(input())
                    print("Attendance: ")
                    attendance = int(input())
                    print("Grade: ")
                    grade = int(input())
                    student = Student(idd, name, attendance, grade)
                    self.__student_serv.add_student(student)
                except ValueError as ve:
                    print(ve)
            elif opp == 2:
                arr = self.__student_serv.display_students()
                for i in arr:
                    print(i)
            elif opp == 3:
                print("Introduce p: ")
                p = int(input())
                print("Introduce b: ")
                b = int(input())
                self.__student_serv.give_bonuses(p, b)
            elif opp == 4:
                print("Introduce the string: ")
                string = input()
                arr = self.__student_serv.display_by_string(string)
                for i in arr:
                    print(i)
            elif opp == 5:
                print("Bye!")
                break
            else:
                print("Invalid Option!")
