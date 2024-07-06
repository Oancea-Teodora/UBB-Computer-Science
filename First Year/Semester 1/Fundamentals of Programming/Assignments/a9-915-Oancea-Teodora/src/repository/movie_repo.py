from src.domain.movie import *
from random import randint, choice
from src.repository.client_repo import NotUniqueClientIdError, NotFoundID
import copy
import pickle
import jsonpickle
import os

class MovieRepo:
    def __init__(self):
        self._movie_list = []

    def generate(self):
        title_list = ["Clover field", "Clown", "The Clowns", "ClownTown", "Club Paradise", "Clue", "Cobra Verde", "Harry Potter", "Home alone", "Karate kid", "Lion king", "Cinderella", "The Godfather", "The Godfather: Part II", "The Godfather: Part III", "The Good, the Bad and the Ugly", "Good Bye, Lenin!", "Good Morning", "Vietnam", "Good Will Hunting", "Goodfellas", "The Goonies", "The Graduate", "Gran Torino", "The Grand Budapest Hotel", "The Great Dictator", "The Great Escape", "The Great Gatsby", "The Green Mile", "Groundhog Day", "Guardians of the Galaxy", "Hachi: A Dog's Tale", "Hacksaw Ridge"]
        description_list = ["A very good movie", "A very bad movie", "A very long movie", "A very short movie", "A very funny movie", "A very sad movie", "A very happy movie", "A very boring movie", "A very interesting movie", "A very scary movie"]
        genre_list = ["Action", "Adventure", "Comedy", "Crime", "Drama", "Fantasy", "Historical", "Historical fiction", "Horror", "Magical realism", "Mystery", "Paranoid Fiction", "Philosophical", "Political", "Romance", "Saga", "Satire", "Science fiction", "Social", "Speculative", "Thriller", "Urban", "Western"]
        for i in range(1,21):
            _id = i
            title = choice(title_list)
            description = choice(description_list)
            genre = choice(genre_list)
            movie = Movie(_id, title, description, genre)
            self._movie_list.append(movie)

    def add_movie(self, movie):
        """
        Adds a new movie to the repository
        :param movie: the movie to be added
        :return: -
        """
        if self.already_exists_movie(movie) is False:
            raise NotUniqueClientIdError(movie.movie_id)
        self._movie_list.append(movie)

    def display_movies(self):
        """
        Returns all the movies in the repository
        :return:
        """
        return self._movie_list

    def update_title(self, new_movie, movie_id):
        """
        Updates the title of a movie
        :param new_movie:
        :param movie_id:
        :return:
        """
        found = 0
        for movie in self._movie_list:
            if int(movie_id) == int(movie.movie_id):
                movie.title = str(new_movie)
                found = 1
                break
        if found == 0:
            raise NotFoundID(movie_id)

    def update_description(self, new_movie, movie_id):
        """
        Updates the description of a movie
        :param new_movie:
        :param movie_id:
        :return:
        """
        for movie in self._movie_list:
            if int(movie_id) == int(movie.movie_id):
                movie.description= str(new_movie)
                break

    def update_genre(self, new_movie, movie_id):
        """
        Updates the genre of a movie
        :param new_movie:
        :param movie_id:
        :return:
        """
        for movie in self._movie_list:
            if int(movie_id) == int(movie.movie_id):
                movie.genre = str(new_movie)
                break

    def remove(self, movie_id):
        """
        Removes a movie from the repository
        :param movie_id:
        :return:
        """
        for movie in self._movie_list:
            if int(movie_id) == int(movie.movie_id):
                self._movie_list.remove(movie)

    def search_by_title(self, title_search):
        movies_title_list = []
        for movie in self._movie_list:
            copy_title = movie.title
            if title_search.lower() in copy_title.lower():
                movies_title_list.append(movie)
        return movies_title_list

    def search_by_description(self, description_search):
        movies_description_list = []
        for movie in self._movie_list:
            copy_description = movie.description
            if description_search.lower() in copy_description.lower():
                movies_description_list.append(movie)
        return movies_description_list

    def search_by_genre(self, genre_search):
        movies_genre_list = []
        for movie in self._movie_list:
            copy_genre = movie.genre
            if genre_search.lower() in copy_genre.lower():
                movies_genre_list.append(movie)
        return movies_genre_list

    def get_movie_by_id(self, id_find):
        for movie in self._movie_list:
            if int(movie.movie_id) == int(id_find):
                return movie

    def already_exists_movie(self, movie):
        for i in self._movie_list:
            if i.movie_id == movie.movie_id:
                return False
        return True


class TextFileRepository2(MovieRepo):
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
                movie = Movie(int(valori[0]), valori[1], valori[2], valori[3])
                super().add_movie(movie)
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
            for line in self.display_movies():
                f.write(f"{line.movie_id},Title: {line.title}, Description: {line.description}, Genre: {line.genre}\n")
            f.close()

        except FileNotFoundError:
            fisier_nou = open(self._file_name, "w")
            fisier_nou.close()

        except IOError as e:

            raise e

    def add_movie(self, movie):
        """
        Adds a new client to the repository
        :param client:  new client to be added
        :return:
        """
        super().add_movie(movie)
        self._save_file()

    def update_title(self, new_movie, movie_id):
        """
        Updates the title of a movie
        :param new_movie:
        :param movie_id:
        :return:
        """
        super().update_title(new_movie, movie_id)
        self._save_file()

    def update_description(self, new_movie, movie_id):
        """
        Updates the description of a movie
        :param new_movie:
        :param movie_id:
        :return:
        """
        super().update_description(new_movie, movie_id)
        self._save_file()

    def update_genre(self, new_movie, movie_id):
        """
        Updates the genre of a movie
        :param new_movie:
        :param movie_id:
        :return:
        """
        super().update_genre(new_movie, movie_id)
        self._save_file()

    def remove(self, movie_id):
        super().remove(movie_id)
        self._save_file()


class BinaryFileRepository2(MovieRepo):
    def __init__(self, file_name):
        super().__init__()
        self._file_name = file_name
        self._load_file()

    def _load_file(self):
        """
        Loads the file
        :return:
        """
        #try:

        file = open(self._file_name, "rb")
        self._client_list = pickle.load(file)
        file.close()
        #if len(self._client_list) == 0:
        self.generate()
           # self._client_list = pickle.load(file)
        self._save_file()

    def _save_file(self):
        """
        Saves the file
        :return:
        """
        try:
            f = open(self._file_name, "wb")
            pickle.dump(self._movie_list, f)
        except IOError as e:
            raise e
        finally:
            f.close()

    def add_movie(self, movie):
        """
        Adds a new client to the repository
        :param client:  new client to be added
        :return:
        """
        super().add_movie(movie)
        self._save_file()

    def update_title(self, new_movie, movie_id):
        """
        Updates the title of a movie
        :param new_movie:
        :param movie_id:
        :return:
        """
        super().update_title(new_movie, movie_id)
        self._save_file()

    def update_description(self, new_movie, movie_id):
        """
        Updates the description of a movie
        :param new_movie:
        :param movie_id:
        :return:
        """
        super().update_description(new_movie, movie_id)
        self._save_file()

    def update_genre(self, new_movie, movie_id):
        """
        Updates the genre of a movie
        :param new_movie:
        :param movie_id:
        :return:
        """
        super().update_genre(new_movie, movie_id)
        self._save_file()

    def remove(self, movie_id):
        super().remove(movie_id)
        self._save_file()


class MemoryMovieRepo(MovieRepo):
    def __init__(self):
        super().__init__()
        self.generate()
