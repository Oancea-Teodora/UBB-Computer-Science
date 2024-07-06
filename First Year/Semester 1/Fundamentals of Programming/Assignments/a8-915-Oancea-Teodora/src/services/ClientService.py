from src.domain.client import *
from src.repository.client_repo import *

class ClientServices:
    def __init__(self, client_repo: ClientRepo):
        self.__repo = client_repo

    def add_client(self, client: Client):
        self.__repo.add_client(client)

    def generate(self):
        self.__repo.generate()

    def display_client(self):
        return self.__repo.display_clients()

    def remove(self, client: int):
        self.__repo.remove(client)

    def update_name(self, new_client, client_id):
        self.__repo.update_name(new_client, client_id)

    def search_by_name(self, search_name):
        return self.__repo.search_by_name(search_name)

    def get_client_by_id(self, id_search):
        return self.__repo.get_client_by_id(id_search)


