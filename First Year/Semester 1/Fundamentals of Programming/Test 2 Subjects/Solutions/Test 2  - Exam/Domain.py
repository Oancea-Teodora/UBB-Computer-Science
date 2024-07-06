

class Student:
    def __init__(self, student_id, name, attendance, grade):
        self.__student_id = student_id
        self.__name = name
        self.__attendance = attendance
        self.__grade = grade

    @property
    def student_id(self):
        return self.__student_id

    @property
    def name(self):
        return self.__name

    @property
    def attendance(self):
        return self.__attendance

    @property
    def grade(self):
        return self.__grade

    @student_id.setter
    def student_id(self, student_id):
        self.__student_id = student_id

    @name.setter
    def name(self, name):
        self.__name = name

    @attendance.setter
    def attendance(self, attendance):
        self.__attendance = attendance

    @grade.setter
    def grade(self, grade):
        self.__grade = grade

    def __str__(self):
        return str(self.__student_id) + ", " + self.__name + ", " + str(self.__attendance) + ", " + str(self.__grade)
