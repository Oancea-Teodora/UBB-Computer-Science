import unittest
from src.domain.client import *
from src.repository.client_repo import *


class MyTestCase(unittest.TestCase):
    def setUp(self):
        self.__repo = ClientRepo()

    def test_add_client(self):
        client = Client(client_id=1, name="Michael Smith")
        self.__repo.add_client(client)
        self.assertIn(client, self.__repo.display_clients())

    def test_get_all_clients(self):
        self.assertEqual(self.__repo.display_clients(), [])

        clients = [
            Client(client_id=1, name="Michael Smith"),
            Client(client_id=2, name="Mike Doe"),
            Client(client_id=3, name="Lisa Ann")
        ]
        for client in clients:
            self.__repo.add_client(client)
        self.assertEqual(self.__repo.display_clients(), clients)

    def test_update(self):
        client = Client(client_id=1, name="Michael Smith")
        self.__repo.add_client(client)
        new_name = "Michael Updated"
        self.__repo.update_name(new_name, client.client_id)
        self.assertEqual(client.name, new_name)

    def test_remove(self):
        client = Client(client_id=1, name="Michael Smith")
        self.__repo.add_client(client)
        self.__repo.remove(client.client_id)
        self.assertNotIn(client, self.__repo.display_clients())


if __name__ == '__main__':
    unittest.main()
