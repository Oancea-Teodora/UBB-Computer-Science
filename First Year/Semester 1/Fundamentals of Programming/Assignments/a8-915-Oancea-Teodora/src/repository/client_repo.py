from src.domain.client import *
from random import randint, choice


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
        if client.name.isnumeric() is True:
            raise ValueError("Name must be a string")
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
