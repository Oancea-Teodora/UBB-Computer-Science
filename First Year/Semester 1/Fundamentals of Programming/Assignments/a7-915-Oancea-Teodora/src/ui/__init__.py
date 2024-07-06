from src.services import *

from jproperties import Properties

class UI:
    def __init__(self, serv: Services):
        self.__serv = serv
    def menu(self):
        print("Menu")
        print("1 - Add a student")
        print("2 - Display the list of students")
        print("3 - Filter the list so that students in a given group are deleted from the list")
        print("4 - Undo")
        print("0 - Exit")

    def start(self):
        while True:
            try:
                self.menu()
                opp = int(input(">>"))
                if opp == 1:
                    print("Introduce the ID of the student")
                    id = int(input())
                    print("Introduce the name of the student")
                    name = input()
                    print("Introduce the group")
                    group = int(input())
                    self.__serv.add(id, name, group)
                elif opp == 2:
                    for x in self.__serv.get_all():
                        print(x)
                elif opp == 3:
                    print("Introduce the group that will be removed")
                    gr = int(input())
                    self.__serv.filter(gr)
                elif opp == 4:
                    self.__serv.undo()
                elif opp == 0:
                    print("Bye!")
                    break
                else:
                    print("Invalid option!")
            except Exception as ex:
                print(ex)


if __name__ == "__main__":
    repo = Repository()

    config = Properties()

    config_file = open("settings.properties", "rb")
    config.load(config_file)

    repo_type = config.get("repo_type").data

    if repo_type == "memory":
        repo = MemoryRepository()
    elif repo_type == "txt":
        file_name = config.get("text_file_name").data
        repo = TextFileRepository(file_name)
    elif repo_type == "bin":
        file_name = config.get("binary_name").data
        repo = BinaryRepository(file_name)
    elif repo_type == "json":
        file_name = config.get("json_name").data
        repo = JsonRepository(file_name)

    serv = Services(repo)
    ui = UI(serv)
    ui.start()
