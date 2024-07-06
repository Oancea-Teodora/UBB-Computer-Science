from UI import *


if __name__ == "__main__":
    file_path = "students.txt"
    repo = StudentRepo(file_path)
    serv = StudentService(repo)
    ui = UI(serv)
    ui.start()