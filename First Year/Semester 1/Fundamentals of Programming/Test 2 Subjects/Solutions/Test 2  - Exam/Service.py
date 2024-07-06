from Repository import *


class StudentService:

    def __init__(self, student_repo: StudentRepo):
        self.__student_repo = student_repo

    def add_student(self, student):
        self.__student_repo.add_student(student)

    def display_students(self):
        return self.__student_repo.display_students()

    def give_bonuses(self, b, p):
        self.__student_repo.give_bonuses(b, p)

    def display_by_string(self, string):
        return self.__student_repo.display_by_string(string)
