from src.domain.rental import *
from src.repository.rental_repo import *


class RentalServices:

    def __init__(self, rental_repo: RentalRepo):
        self.__repo = rental_repo

    def generate(self):
        self.__repo.generate()

    def add_rental(self, rental):
        self.__repo.add_rental(rental)

    def display_rentals(self):
        return self.__repo.display_rentals()

    def update_rentals(self, rental_id, new_returned_date):
        self.__repo.update_rental(rental_id, new_returned_date)

    def get_rental_by_id(self, rental_id):
        return self.__repo.get_rental_by_id(rental_id)

    def most_rented_movies(self):
        rental_list = self.__repo.display_rentals()
        most_active = []
        for rental in rental_list:
            ok = 0
            for i in most_active:
                if rental.movie_id == i["movie_id"]:
                    i["number"] = i["number"] + self.__repo.get_number_of_days(rental.rented_date, rental.returned_date)
                    ok = 1
                    break
            if ok == 0:
                dict = {"movie_id": rental.movie_id, "number": self.__repo.get_number_of_days(rental.rented_date, rental.returned_date)}
                most_active.append(dict)
        most_active.sort(key=lambda i: i["number"], reverse=True)
        return most_active

    def most_active_clients(self):
        rental_list = self.__repo.display_rentals()
        most_active = []
        for rental in rental_list:
            ok = 0
            for i in most_active:
                if rental.client_id == i["client_id"]:
                    i["number"] = i["number"] + self.__repo.get_number_of_days(rental.rented_date, rental.returned_date)
                    ok = 1
                    break
            if ok == 0:

                dict = {"client_id": rental.client_id, "number": self.__repo.get_number_of_days(rental.rented_date, rental.returned_date)}
                most_active.append(dict)
        most_active.sort(key=lambda i: i["number"], reverse=True)
        return most_active

    def late_rentals(self):
        rental_list = self.__repo.display_rentals()
        late_rental_list = []
        for rental in rental_list:
            ok = self.__repo.compare_dates(rental.due_date, rental.returned_date)
            if ok == 1:
                dict = {"rental_id": rental.rental_id, "movie_id": rental.movie_id, "days_late": self.__repo.get_number_of_days(rental.due_date, rental.returned_date)}
                late_rental_list.append(dict)
        late_rental_list.sort(key=lambda i: i["days_late"], reverse=True)
        return late_rental_list
