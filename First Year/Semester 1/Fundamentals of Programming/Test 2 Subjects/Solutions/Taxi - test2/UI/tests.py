import unittest
from Repository.taxi_repo import *
from Domain.Taxi import *


class MyTestCase(unittest.TestCase):
    def setUp(self):
        self.__repo = TaxiRepo()

    def test_add_ride(self):
        taxi = Taxi(12, 1, 5, 12)
        self.__repo.add_taxi(taxi)
        self.assertIn(taxi, self.__repo.get_all_taxi())


if __name__ == '__main__':
    unittest.main()
