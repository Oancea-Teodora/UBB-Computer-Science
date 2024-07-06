import unittest
from Repository import StudentRepo
from Domain import Student


class MyTestCase(unittest.TestCase):
    def setUp(self):
        self.__repo = StudentRepo(file_path="students.txt")

    def test_add(self):
        student = Student(1, "Michael Smith", 10, 10)
        self.__repo.add_student(student)
        self.assertIn(student, self.__repo.display_students())


if __name__ == '__main__':
    unittest.main()
