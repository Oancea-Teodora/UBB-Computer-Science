import copy
import pickle
from src.domain import *
import jsonpickle
import json


class Repository:
    def __init__(self):
        self._list = []
        self._history = []

    def generate(self):
        """
        Generates ten students and puts them in the list
        :return:
        """
        for i in range(10):
            id = i
            names = ["Cosmin", "Eliza", "Teo", "Mihai", "Andrei", "Otilia", "Daria", "Ioana", "Bianca", "Miruna",
                     "Ilinca", "Andreea"]
            name = names[randint(0, 11)]
            group = randint(910, 917)
            self._list.append(Student(id, name, group))

    def add_student(self, student: Student):
        """
        Adds a student to the list of students and maintains a history of the student list.
        :param student: A Student object representing the student to be added.
        :type student: Student
        """
        self._history.append(copy.deepcopy(self._list))

        self._list.append(student)

    def get_all_students(self):
        return self._list

    def filter(self, gr):
        self._history.append(copy.deepcopy(self._list))

        ok = 1
        while ok == 1:
            ok = 0
            for i in self._list:
                if i.group == gr:
                    self._list.remove(i)
                    ok = 1

    def undo(self):
        self._list = self._history[-1]
        self._history.pop()


class BinaryRepository(Repository):
    def __init__(self, file_name: str):
        super().__init__()
        self.__file_name = file_name
        self.load()

    def load(self):
        file = open(self.__file_name, "rb")
        self._list = pickle.load(file)
        file.close()

    def save(self):
        file = open(self.__file_name, "wb")
        pickle.dump(self._list, file)
        file.close()

    def filter(self, group: int):
        super().filter(group)
        self.save()

    def add_student(self, student: Student):
        super().add_student(student)
        self.save()

    def undo(self):
        super().undo()
        self.save()


class TextFileRepository(Repository):
    def __init__(self, file_name: str):
        super().__init__()
        self.__file_name = file_name
        self.load()

    def load(self):
        file = open(self.__file_name, "r")
        values = file.readlines()
        for value in values:
            student = value.split("|")
            id = int(student[0])
            name = student[1]
            group = int(student[2])
            self._list.append(Student(id, name, group))

    def save(self):
        values = []
        for student in self._list:
            values.append(str(student.id) + "|" + student.name + "|" + str(student.group) + '\n')

        file = open(self.__file_name, "w")
        file.writelines(values)
        file.flush()
        file.close()

    def filter(self, gr: int):
        super().filter(gr)
        self.save()

    def add_student(self, value: Student):
        super().add_student(value)
        self.save()

    def undo(self):
        super().undo()
        self.save()


class JsonRepository(Repository):
    def __init__(self, file_name: str):
        super().__init__()
        self.__file_name = file_name
        self.load()

    def load(self):
        file = open(self.__file_name, "r")
        self._list = jsonpickle.decode(json.load(file))
        file.close()

    def save(self):
        file = open(self.__file_name, "w")
        json.dump(jsonpickle.encode(self._list), file)
        file.close()

    def filter(self, group: int):
        super().filter(group)
        self.save()

    def add_student(self, student: Student):
        """

        :param student:
        :return:
        """
        super().add_student(student)
        self.save()

    def undo(self):
        super().undo()
        self.save()


class MemoryRepository(Repository):
    def __init__(self):
        super().__init__()
        self.generate()
