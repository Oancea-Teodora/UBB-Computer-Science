from src.services.MovieService import *
from src.services.ClientService import *
from src.services.RentalService import *
from src.services.HistoryService import *
from jproperties import Properties


class UI:
    @staticmethod
    def print_menu():
        print("Menu")
        print("1 - Add a client")
        print("2 - Remove a client")
        print("3 - Update a client's name")
        print("4 - Display the clients")
        print("5 - Add a movie")
        print("6 - Remove a movie")
        print("7 - Update a movie's title")
        print("8 - Update a movie's description")
        print("9 - Update a movie's genre")
        print("10 - Display the movies")
        print("11 - Rent a movie")
        print("12 - Return a movie")
        print("13 - Display rentals")
        print("14 - Search by a movie's title")
        print("15 - Search by a movie's description")
        print("16 - Search by a movie's genre")
        print("17 - Search by a client's name")
        print("18 - Most rented movies")
        print("19 - Most active clients")
        print("20 - Late rentals")
        print("21 - Undo")
        print("22 - Redo")
        print("0 - Exit")

    def __init__(self, client_services: ClientServices, movie_services: MovieService, rental_services: RentalServices, history_service: HistoryService):
        self.__rental_services = rental_services
        self.__client_services = client_services
        self.__movie_services = movie_services
        self.__history_service = history_service

    def start(self):
        self.print_menu()
        while True:
            try:
                opp = int(input(">>"))

                if opp == 1:
                    _id = int(input("Client's ID: "))
                    name = input("Client's name: ")
                    client = Client(_id, name)
                    self.__client_services.add_client(client)
                    self.__history_service.add_to_history(CommandHistory("add client", [_id, name]))

                elif opp == 2:
                    _id = int(input("Client's ID: "))
                    client = self.__client_services.get_client_by_id(_id)
                    self.__client_services.remove(_id)
                    self.__history_service.add_to_history(CommandHistory("remove client", [_id, client.name]))

                elif opp == 3:
                    _id = int(input("Client's ID: "))
                    name1 = input("New name: ")
                    client = self.__client_services.get_client_by_id(_id)
                    initial_name = client.name
                    self.__client_services.update_name(name1, _id)
                    self.__history_service.add_to_history(CommandHistory("update client", [_id, initial_name, name1]))

                elif opp == 4:
                    arr = self.__client_services.display_client()
                    for i in arr:
                        print(i)

                elif opp == 5:
                    _id = int(input("Movie's ID: "))
                    title = input("Movie's title: ")
                    description = input("Movie's description: ")
                    genre = input("Movie's genre: ")
                    movie = Movie(_id, title, description, genre)
                    self.__movie_services.add_movie(movie)
                    self.__history_service.add_to_history(CommandHistory("add movie", [_id, title, description, genre]))

                elif opp == 6:
                    _id = int(input("Movie's ID: "))
                    movie = self.__movie_services.get_movie_by_id(_id)
                    self.__movie_services.remove(_id)
                    self.__history_service.add_to_history(CommandHistory("remove movie", [_id, movie.title, movie.description, movie.genre]))

                elif opp == 7:
                    _id = int(input("Movie's ID: "))
                    title1 = input("New title: ")
                    movie = self.__movie_services.get_movie_by_id(_id)
                    original_title = movie.title
                    self.__movie_services.update_title(title1, _id)
                    self.__history_service.add_to_history(CommandHistory("update title", [_id, original_title, title1]))

                elif opp == 8:
                    _id = int(input("Movie's ID: "))
                    description1 = input("New description: ")
                    movie = self.__movie_services.get_movie_by_id(_id)
                    original_description = movie.description
                    self.__movie_services.update_description(description1, _id)
                    self.__history_service.add_to_history(CommandHistory("update description", [_id, original_description, description1]))

                elif opp == 9:
                    _id = int(input("Movie's ID: "))
                    genre1 = input("New genre: ")
                    movie = self.__movie_services.get_movie_by_id(_id)
                    original_genre = movie.genre
                    self.__movie_services.update_genre(genre1, _id)
                    self.__history_service.add_to_history(CommandHistory("update genre", [_id, original_genre, genre1]))

                elif opp == 10:
                    arr = self.__movie_services.display_movie()
                    for i in arr:
                        print(i)

                elif opp == 11:
                    _id = int(input("Rental's ID: "))
                    movie_id = int(input("Movie's ID: "))
                    client_id = int(input("Client's ID: "))
                    rented_date = input("Rented date is: ")
                    due_date = input("Due date is: ")
                    rental = Rental(_id, movie_id, client_id, rented_date, due_date, None)
                    self.__rental_services.add_rental(rental)
                    self.__history_service.add_to_history(CommandHistory("add rental", [_id, movie_id, client_id, rented_date, due_date, None]))

                elif opp == 12:
                    _id = int(input("Rental's ID: "))
                    new_returned_date = input("Returned date is: ")
                    rental = self.__rental_services.get_rental_by_id(_id)
                    original_returned_date = rental.returned_date
                    self.__rental_services.update_rentals(_id, new_returned_date)
                    self.__history_service.add_to_history(CommandHistory("update rental", [_id, original_returned_date, new_returned_date]))

                elif opp == 13:
                    arr = self.__rental_services.display_rentals()
                    for i in arr:
                        print(i)

                elif opp == 14:
                    title_search = input("Introduce the title for the search: ")
                    arr = self.__movie_services.search_by_title(title_search)
                    for i in arr:
                        print(i)

                elif opp == 15:
                    description_search = input("Introduce the description for the search: ")
                    arr = self.__movie_services.search_by_description(description_search)
                    for i in arr:
                        print(i)

                elif opp == 16:
                    genre_search = input("Introduce the genre for the search: ")
                    arr = self.__movie_services.search_by_genre(genre_search)
                    for i in arr:
                        print(i)

                elif opp == 17:
                    name_search = input("Introduce the name for the: ")
                    arr = self.__client_services.search_by_name(name_search)
                    for i in arr:
                        print(i)

                elif opp == 18:
                    arr = self.__rental_services.most_rented_movies()
                    for i in arr:
                        print("The movie's ID is", i["movie_id"], "and it was rented", i["number"], "day(s)", self.__movie_services.get_movie_by_id(i["movie_id"]))

                elif opp == 19:
                    arr = self.__rental_services.most_active_clients()
                    for i in arr:
                        print("The client's ID is", i["client_id"], "and he rented movies for", i["number"], "days", self.__client_services.get_client_by_id(i["client_id"]))

                elif opp == 20:
                    arr = self.__rental_services.late_rentals()
                    for i in arr:
                        print("Number of days late", i["days_late"], "for Rental ID", i["rental_id"], self.__movie_services.get_movie_by_id(i["movie_id"]))

                elif opp == 21:
                    self.__history_service.undo()

                elif opp == 22:
                    self.__history_service.redo()

                elif opp == 0:
                    print("Bye!")
                    break
                else:
                    print("Invalid option!")
            except Exception as ex:
                print(ex)


