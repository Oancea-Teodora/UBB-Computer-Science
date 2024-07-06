from src.repository.client_repo import *
from src.repository.movie_repo import *
from src.repository.rental_repo import *
from src.domain.commandhistory import *


class HistoryService:
    def __init__(self, client_repo: ClientRepo, movie_repo: MovieRepo, rental_repo: RentalRepo):
        self.__client_repo = client_repo
        self.__movie_repo = movie_repo
        self.__rental_repo = rental_repo
        self._history = []
        self.history_index = -1

    def add_to_history(self, command: CommandHistory):
        while len(self._history) > self.history_index + 1:
            self._history.pop()

        self._history.append(command)
        self.history_index = self.history_index + 1

    def get_command(self):
        return self._history[self.history_index]

    def undo(self):
        try:
            if self.history_index == -1:
                raise ValueError("No more undos")
            last_command = self.get_command()
            if self.history_index > -1:
                self.history_index = self.history_index - 1
            self.map_command_and_execute_undo(last_command)
        except IndexError as er:
            pass

    def redo(self):
        try:
            if self.history_index == len(self._history) - 1:
                raise ValueError("No more redos")
            self.history_index +=1
            last_command = self.get_command()
            self.map_command_and_execute_redo(last_command)
        except IndexError as er:
            self.history_index -=1

    def map_command_and_execute_undo(self, command: CommandHistory):
        if command.command_type == "add client":
            self.__client_repo.remove(command.parameters[0])

        elif command.command_type == "remove client":
            self.__client_repo.add_client(Client(command.parameters[0], command.parameters[1]))
            #for rental in command.parameters[2]

        elif command.command_type == "update client":
            self.__client_repo.update_name(command.parameters[1], command.parameters[0])

        elif command.command_type == "add movie":
            self.__movie_repo.remove(command.parameters[0])

        elif command.command_type == "remove movie":
            self.__movie_repo.add_movie(Movie(command.parameters[0], command.parameters[1], command.parameters[2], command.parameters[3]))

        elif command.command_type == "update title":
            self.__movie_repo.update_title(command.parameters[1], command.parameters[0])

        elif command.command_type == "update description":
            self.__movie_repo.update_description(command.parameters[1], command.parameters[0])

        elif command.command_type == "update genre":
            self.__movie_repo.update_genre(command.parameters[1], command.parameters[0])

        elif command.command_type == "add rental":
            self.__rental_repo.remove_rental(command.parameters[0])

        elif command.command_type == "update rental":
            self.__rental_repo.update_rental(command.parameters[0], command.parameters[1])

    def map_command_and_execute_redo(self, command: CommandHistory):
        if command.command_type == "add client":
            self.__client_repo.add_client(Client(command.parameters[0], command.parameters[1]))

        elif command.command_type == "remove_client":
            self.__client_repo.remove(command.parameters[0])

        elif command.command_type == "update client":
            self.__client_repo.update_name(command.parameters[1], command.parameters[0])

        elif command.command_type == "add movie":
            self.__movie_repo.add_movie(Movie(command.parameters[0], command.parameters[1], command.parameters[2], command.parameters[3]))

        elif command.command_type == "remove movie":
            self.__movie_repo.remove(command.parameters[0])

        elif command.command_type == "update title":
            self.__movie_repo.update_title(command.parameters[2], command.parameters[0])

        elif command.command_type == "update description":
            self.__movie_repo.update_description(command.parameters[2], command.parameters[0])

        elif command.command_type == "update genre":
            self.__movie_repo.update_genre(command.parameters[2], command.parameters[0])

        elif command.command_type == "add rental":
            self.__rental_repo.add_rental(Rental(command.parameters[0], command.parameters[1], command.parameters[2], command.parameters[3], command.parameters[4], command.parameters[5]))

        elif command.command_type == "update rental":
            self.__rental_repo.update_rental(command.parameters[0], command.parameters[2])



