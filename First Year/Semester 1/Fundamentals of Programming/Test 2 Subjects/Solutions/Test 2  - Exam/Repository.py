from Domain import *


class StudentRepo:

    def __init__(self, file_path):
        self.__file_path = file_path
        self._students = self.load_file()

    def load_file(self):
        lists = []
        with open(self.__file_path, "r") as file_name:
            for line in file_name:
                data = line.strip().split(", ")
                student = Student(int(data[0]), data[1], int(data[2]), int(data[3]))
                lists.append(student)
        return lists

    def save_file(self):
        with open(self.__file_path, "w") as file_name:
            for student in self._students:
                file_name.write(str(student) + '\n')

    def add_student(self, student):
        data = student.name.split(" ")
        for students in self._students:
            if student.student_id == students.student_id:
                raise ValueError("The ID already exists!")
        if len(data) != 2 or len(data[0]) < 3 or len(data[1]) <3:
            raise ValueError("Incorrect name!")
        if student.attendance < 0:
            raise ValueError("Incorrect attendance!")
        if student.grade < 0 or student.grade > 10:
            raise ValueError("Incorrect grade!")

        self._students.append(student)
        self.save_file()

    def display_students(self):
        student_list = sorted(self._students, key=lambda x: x.grade, reverse=True)
        for i in range(0, len(student_list)-1):
            for j in range(i+1, len(student_list)):
                if student_list[i].grade == student_list[j].grade and student_list[i].name > student_list[j].name:
                    aux = student_list[i]
                    student_list[i] = student_list[j]
                    student_list[j] = aux
        return student_list

    def give_bonuses(self, p, b):
        for student in self._students:
            if student.attendance >= p and student.grade + b <= 10:
                student.grade += b
        self.save_file()

    def display_by_string(self, string):
        students = []
        for student in self._students:
            copy_name = student.name
            if string.lower() in copy_name.lower():
                students.append(student)
        students = sorted(students, key = lambda x: x.name, reverse=False)
        return students





