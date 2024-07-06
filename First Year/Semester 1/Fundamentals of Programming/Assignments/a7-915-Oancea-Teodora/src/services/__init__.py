from src.repository import *
from src.domain import *

class Services:
    def __init__(self, repo: Repository):
        self.__repo = repo

    def add(self, id, name, group):
        """
        Adds a student with the given ID, name, and group to the repository.
        :param id: The ID of the student
        :param name: The name of the student
        :param group: The group to which the student belongs
        :return:
        """
        self.__repo.add_student(Student(id, name, group))

    def get_all(self):
        return self.__repo.get_all_students()

    def filter(self, gr):
        self.__repo.filter(gr)

    def undo(self):
        self.__repo.undo()