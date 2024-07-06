from src.domain.movie import *
from src.repository.movie_repo import *


class MovieService:
    def __init__(self, movie_repo: MovieRepo):
        self.__repo = movie_repo

    def add_movie(self, movie: Movie):
        self.__repo.add_movie(movie)

    def display_movie(self):
        return self.__repo.display_movies()

    def remove(self, movie_id):
        self.__repo.remove(movie_id)

    def update_title(self, new_movie, movie_id):
        self.__repo.update_title(new_movie,movie_id)

    def update_description(self, new_movie, movie_id):
        self.__repo.update_description(new_movie,movie_id)

    def update_genre(self, new_movie, movie_id):
        self.__repo.update_genre(new_movie,movie_id)

    def generate(self):
        self.__repo.generate()

    def search_by_title(self, title_search):
        return self.__repo.search_by_title(title_search)

    def search_by_description(self, description_search):
        return self.__repo.search_by_description(description_search)

    def search_by_genre(self, genre_search):
        return self.__repo.search_by_genre(genre_search)

    def get_movie_by_id(self, id_search):
        return self.__repo.get_movie_by_id(id_search)