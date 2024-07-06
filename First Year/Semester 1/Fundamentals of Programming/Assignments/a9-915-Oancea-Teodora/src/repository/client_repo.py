from src.domain.client import *
from src.domain import *
from random import randint, choice
import copy
import pickle
import jsonpickle


class NotUniqueClientIdError(Exception):
    def __init__(self, client_id):
        self.__client_id = client_id

    def __str__(self):
        return str(self.__client_id) + " is not an unique client id "


class NotFoundID(Exception):
    def __init__(self, entity_id):
        self.__entity_id = entity_id

    def __str__(self):
        return str(self.__entity_id) + " this id is not found"


class ClientRepo:
    def __init__(self):
        self._client_list = []

    def generate(self):
        name_list = ["Tudor", "John", "Mary", "Bob", "Alice", "Mark", "Megan", "James", "Lily", "Michael", "Emma",
                     "David", "Olivia", "Richard", "Sophia", "Joseph", "Elizabeth", "Thomas", "Grace", "Charles",
                     "Sarah"]
        family_name_list = ["Smith", "Johnson", "Williams", "Brown", "Jones", "Miller", "Davis", "Garcia", "Rodriguez",
                            "Wilson", "Martinez", "Anderson", "Taylor", "Thomas", "Hernandez", "Moore", "Martin",
                            "Jackson", "Thompson", "White"]
        for i in range(1, 21):
            _id = i
            name = choice(name_list) + " " + choice(family_name_list)
            client = Client(_id, name)
            self._client_list.append(client)

    def add_client(self, client):
        """
        Adds a new client to the repository
        :param client:  new client to be added
        :return:
        """
        if self.already_exists_client(client) is False:
            raise NotUniqueClientIdError(client.client_id)
        self._client_list.append(client)

    def display_clients(self):
        """
        Returns all the clients in the repository
        :return:
        """
        return self._client_list

    def update_name(self, new_client, client_id):
        """
        Updates the name of a client
        :param new_client:
        :param client_id:
        :return:
        """
        found = 0
        for client in self._client_list:
            if int(client_id) == int(client.client_id):
                found = 1
                client.name = str(new_client)
                break
        if found == 0:
            raise NotFoundID(client_id)

    def remove(self, client_id):
        """
        Removes a client from the repository
        :param client_id:
        :return:
        """
        found = 0
        for client in self._client_list:
            if int(client_id) == int(client.client_id):
                self._client_list.remove(client)
                found = 1
        if found == 0:
            raise NotFoundID(client_id)

    def search_by_name(self, name_search):
        client_name_list = []
        for client in self._client_list:
            copy_name = client.name
            if name_search.lower() in copy_name.lower():
                client_name_list.append(client)
        return client_name_list

    def get_client_by_id(self, id_search):
        for client in self._client_list:
            if client.client_id == id_search:
                return client

    def already_exists_client(self, client):
        for client1 in self._client_list:
            if client1.client_id == client.client_id:
                return False
        return True


class TextFileRepository1(ClientRepo):
    def __init__(self, file_name):
        super().__init__()
        self._file_name = file_name
        self._load_file()

    def _load_file(self):
        """
        Loads the file
        :return:
        """
        try:
            f = open(self._file_name, "r")
            for line in f:
                line = line.strip()
                valori = line.split(',')
                client = Client(int(valori[0]), valori[1])
                super().add_client(client)
        except FileNotFoundError:
            fisier_nou = open(self._file_name, "w")
            fisier_nou.close()

        except:
            self.generate()
            self._save_file()
            pass

    def _save_file(self):
        """
        Saves the file
        :return:
        """
        try:
            f = open(self._file_name, "w")
            for line in self.display_clients():
                f.write(f"{line.client_id},{line.name}\n")
            f.close()

        except FileNotFoundError:
            fisier_nou = open(self._file_name, "w")
            fisier_nou.close()

        except IOError as e:

            raise e

    def add_client(self, client: Client):
        """
        Adds a new client to the repository
        :param client:  new client to be added
        :return:
        """
        super().add_client(client)
        self._save_file()

    def update_name(self, new_client, client_id):
        """
        Updates the name of a client
        :param new_client:
        :param client_id:
        :return:
        """
        super().update_name(new_client, client_id)
        self._save_file()

    def remove(self, client_id):
        """
        Removes a client from the repository
        :param client_id:
        :return:
        """
        super().remove(client_id)
        self._save_file()


class BinaryFileRepository1(ClientRepo):
    def __init__(self, file_name):
        super().__init__()
        self._file_name = file_name
        self._load_file()

    def _load_file(self):
        """
        Loads the file
        :return:
        """
        #self.generate()
        #self._save_file()
       # try:
        file = open(self._file_name, "rb")
        self._client_list = pickle.load(file)
        file.close()

    def _save_file(self):
        """
        Saves the file
        :return:
        """
        try:
            f = open(self._file_name, "wb")
            pickle.dump(self._client_list, f)
        except IOError as e:
            raise e
        finally:
            f.close()

    def add_client(self, client):
        """
        Adds a new client to the repository
        :param client:  new client to be added
        :return:
        """
        super().add_client(client)
        self._save_file()

    def update_name(self, new_client, client_id):
        """
        Updates the name of a client
        :param new_client:
        :param client_id:
        :return:
        """
        super().update_name(new_client, client_id)
        self._save_file()

    def remove(self, client_id):
        """
        Removes a client from the repository
        :param client_id:
        :return:
        """
        super().remove(client_id)
        self._save_file()


class MemoryClientRepo(ClientRepo):
    def __init__(self):
        super().__init__()
        self.generate()
