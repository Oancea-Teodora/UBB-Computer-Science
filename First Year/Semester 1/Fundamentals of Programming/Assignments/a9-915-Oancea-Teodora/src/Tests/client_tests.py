import unittest
import os
from src.domain.client import Client
from src.repository.client_repo import ClientRepo, TextFileRepository1, BinaryFileRepository1

class TestClientRepo(unittest.TestCase):
    def setUp(self):
        self.__repo = ClientRepo()

    def test_add_client(self):
        client = Client(client_id=1, name="Michael Smith")
        self.__repo.add_client(client)
        self.assertIn(client, self.__repo.display_clients())

    def test_update_name(self):
        client = Client(client_id=1, name="Michael Smith")
        self.__repo.add_client(client)
        new_name = "Michael Updated"
        self.__repo.update_name(new_name, client.client_id)
        updated_client = self.__repo.get_client_by_id(client.client_id)
        self.assertEqual(updated_client.name, new_name)

    def test_remove_client(self):
        client = Client(client_id=1, name="Michael Smith")
        self.__repo.add_client(client)
        self.__repo.remove(client.client_id)
        self.assertNotIn(client, self.__repo.display_clients())

    def test_search_by_name(self):
        client1 = Client(client_id=1, name="Michael Smith")
        client2 = Client(client_id=2, name="John Doe")
        self.__repo.add_client(client1)
        self.__repo.add_client(client2)

        result = self.__repo.search_by_name("Michael")
        self.assertIn(client1, result)
        self.assertNotIn(client2, result)

    def test_get_client_by_id(self):
        client = Client(client_id=1, name="Michael Smith")
        self.__repo.add_client(client)
        result = self.__repo.get_client_by_id(client.client_id)
        self.assertEqual(result, client)


class TestTextFileRepository1(TestClientRepo):
    def setUp(self):
        super().setUp()
        self.client_repo = TextFileRepository1("client_repo_test.txt")

    def tearDown(self):
        super().tearDown()
        if os.path.exists("client_repo_test.txt"):
            os.remove("client_repo_test.txt")


class TestBinaryFileRepository1(TestClientRepo):
    def setUp(self):
        super().setUp()
        self.client_repo = BinaryFileRepository1("client_repo_test.bin")

    def tearDown(self):
        super().tearDown()
        if os.path.exists("client_repo_test.bin"):
            os.remove("client_repo_test.bin")


if __name__ == '__main__':
    unittest.main()
