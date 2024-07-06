from random import randint, choice
import dateutil
from src.domain.rental import *
from datetime import date
from datetime import datetime
from src.repository.client_repo import NotUniqueClientIdError, NotFoundID


class RentalRepo:

    def __init__(self):
        self._rental_list = []

    def generate(self):
        rented_date_list = ["2023-01-02", "2023-01-03", "2023-01-04", "2023-01-05", "2023-01-06", "2023-01-07",
                            "2023-01-08", "2023-01-09", "2023-01-10", "2023-01-11", "2023-01-12", "2023-01-13",
                            "2023-01-14", "2023-01-15", "2023-01-16", "2023-01-17", "2023-01-18", "2023-01-19",
                            "2023-01-20", "2023-01-21"]
        due_date_list = ["2023-01-22", "2023-01-23", "2023-01-24", "2023-01-25", "2023-01-26", "2023-01-27",
                         "2023-01-28", "2023-01-29", "2023-01-30", "2023-01-31", "2023-02-01", "2023-02-02",
                         "2023-02-03", "2023-02-04", "2023-02-05", "2023-02-06", "2023-02-07", "2023-02-08",
                         "2023-02-09", "2023-02-10"]
        returned_date_list = ["2023-01-30", "2023-02-12", "2023-01-24", "2023-01-31", "2023-01-23", "2023-02-16",
                              "2023-02-17", "2023-01-25", "2023-02-19", "2023-01-26", "2023-02-21", "2023-02-22",
                              "2023-01-23", "2023-02-24", "2023-02-25", "2023-02-26", "2023-01-27", "2023-02-28",
                              "2023-03-01", "2023-03-02"]
        for i in range(1, 21):
            _id = i
            id_movie = randint(1, 20)
            id_client = randint(1, 20)
            rented_date = choice(rented_date_list)
            due_date = choice(due_date_list)
            returned_date = choice(returned_date_list)
            returned_date = choice(returned_date_list)
            rental = Rental(_id, id_movie, id_client, rented_date, due_date, returned_date)
            self._rental_list.append(rental)

    def add_rental(self, rental):
        if self.already_exists_rental(rental) is False:
            raise NotUniqueClientIdError(rental.rental_id)
        self._rental_list.append(rental)

    def display_rentals(self):
        return self._rental_list

    def update_rental(self, rental_id, new_returned_date):
        found = 0
        for rental in self._rental_list:
            if int(rental.rental_id) == int(rental_id):
                rental.returned_date = new_returned_date
                found = 1
        if found == 0:
            raise NotFoundID(rental_id)

    def get_number_of_days(self, rented_date, returned_date):
        if returned_date is None:
            rented_date1 = rented_date.split("-")
            current_date = datetime.now()
            f_date = date(int(rented_date1[0]), int(rented_date1[1]), int(rented_date1[2]))
            l_date = date(int(current_date.year), int(current_date.month), int(current_date.day))
            delta = l_date - f_date
        else:
            rented_date1 = rented_date.split("-")
            returned_date1 = returned_date.split("-")
            f_date = date(int(rented_date1[0]), int(rented_date1[1]), int(rented_date1[2]))
            l_date = date(int(returned_date1[0]), int(returned_date1[1]), int(returned_date1[2]))
            delta = l_date - f_date
        return delta.days

    def compare_dates(self, due_date, returned_date):
        ok = 0
        if returned_date is None:
            due_date1 = due_date.split("-")
            returned_date1 = datetime.now()
            if int(due_date1[0]) < int(returned_date1.year):
                ok = 1
            elif int(due_date1[0]) == int(returned_date1.year):
                if int(due_date1[1]) < int(returned_date1.month):
                    ok = 1
                elif int(due_date1[1]) == int(returned_date1.month):
                    if int(due_date1[2]) < int(returned_date1.day):
                        ok = 1
        else:
            due_date1 = due_date.split("-")
            returned_date1 = returned_date.split("-")
            if int(due_date1[0]) < int(returned_date1[0]):
                ok = 1
            elif int(due_date1[0]) == int(returned_date1[0]):
                if int(due_date1[1]) < int(returned_date1[1]):
                    ok = 1
                elif int(due_date1[1]) == int(returned_date1[1]):
                    if int(due_date1[2]) < int(returned_date1[2]):
                        ok = 1
        return ok

    def already_exists_rental(self, rental):
        for rental1 in self._rental_list:
            if rental1.rental_id == rental.rental_id:
                return False
        return True

