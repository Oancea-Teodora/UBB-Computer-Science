import unittest
from src.repository import *
from src.domain import *


class MyTestCase(unittest.TestCase):

    def setUp(self):
        self.__repo = Repository
        self.__repo2 = TextFileRepository("data2.txt")
        self.__repo3 = BinaryRepository("data.bin")

    def test_add(self):
        student = Student(149389, "Marin", 912)
        rezultat = self.__repo.get_all_students() + [student]

        self.__repo.add_student(student)

        self.assertEqual(self.__repo.get_all_students(), rezultat)

    def test_add_2(self):
        student = Student(149389, "Marin", 912)
        rezultat = self.__repo2.get_all_students() + [student]

        self.__repo2.add_student(student)

        self.assertEqual(self.__repo2.get_all_students(), rezultat)

    def test_add_3(self):
        student = Student(149389, "Marin", 912)
        rezultat = self.__repo3.get_all_students() + [student]

        self.__repo3.add_student(student)

        self.assertEqual(self.__repo3.get_all_students(), rezultat)


if __name__ == '__main__':
    unittest.main()
