
class Rental:
    def __init__(self, rental_id, movie_id, client_id, rented_date, due_date, returned_date):
        self.__rental_id = rental_id
        self.__movie_id = movie_id
        self.__client_id = client_id
        self.__rented_date = rented_date
        self.__due_date = due_date
        self.__returned_date = returned_date

    @property
    def rental_id(self):
        return self.__rental_id

    @property
    def movie_id(self):
        return self.__movie_id

    @property
    def client_id(self):
        return self.__client_id

    @property
    def rented_date(self):
        return self.__rented_date

    @property
    def due_date(self):
        return self.__due_date

    @property
    def returned_date(self):
        return self.__returned_date

    @rental_id.setter
    def rental_id(self, rental_id):
        self.__rental_id = rental_id

    @movie_id.setter
    def movie_id(self, movie_id):
        self.__movie_id = movie_id

    @client_id.setter
    def client_id(self, client_id):
        self.__client_id = client_id

    @rented_date.setter
    def rented_date(self, rented_date):
        self.__rented_date = rented_date

    @due_date.setter
    def due_date(self, due_date):
        self.__due_date = due_date

    @returned_date.setter
    def returned_date(self, returned_date):
        self.__returned_date = returned_date


    def __str__(self):
        return " Rental ID: " + str(self.__rental_id) + ", Movie ID: " + str(self.__movie_id) + ", Client ID: " + str(self.__client_id) + ", Rented date: " + str(self.__rented_date) + ", Due date: " + str(self.__due_date) + ", Returned date: " + str(self.__returned_date)