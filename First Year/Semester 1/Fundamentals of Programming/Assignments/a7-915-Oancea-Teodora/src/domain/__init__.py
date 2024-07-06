from random import randint, choice

class Student:
    def __init__(self, id, name, group):
        self.__id = id

        self.__name = name

        self.__group = group

    #getters

    @property
    def id(self):
        return self.__id

    @property
    def name(self):
        return self.__name

    @property
    def group(self):
        return self.__group



    def __str__(self):
        return "Id: " + str(self.id) + " Name: " + str(self.name) + " Group: " + str(self.group)


