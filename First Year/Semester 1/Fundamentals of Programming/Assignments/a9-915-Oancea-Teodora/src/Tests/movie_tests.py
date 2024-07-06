import unittest
import os
from src.domain.movie import Movie
from src.repository.movie_repo import TextFileRepository2, BinaryFileRepository2, NotFoundID, NotUniqueClientIdError


class TestTextFileRepository2(unittest.TestCase):
    def setUp(self):
        self.file_name = "movieteststext.txt"
        self.file_name2 = "movieteststext.bin"
        self.__repo = TextFileRepository2(self.file_name)

    def tearDown(self):
        if os.path.exists(self.file_name):
            os.remove(self.file_name)

    def test_add_movie(self):
        movie = Movie(1, "The Test Movie", "A good movie", "Action")
        self.__repo.add_movie(movie)
        self.assertIn(movie, self.__repo.display_movies())

    def test_get_all_movies(self):
        self.assertEqual(self.__repo.display_movies(), [])

        movies = [
            Movie(1, "The Test Movie", "A good movie", "Action"),
            Movie(2, "The Test Movie 2", "A good movie", "Action"),
            Movie(3, "The Test Movie 3", "A good movie", "Action")
        ]
        for movie in movies:
            self.__repo.add_movie(movie)
        self.assertEqual(self.__repo.display_movies(), movies)

    def test_update_title(self):
        movie = Movie(1, "The Test Movie", "A good movie", "Action")
        self.__repo.add_movie(movie)
        new_title = "The Updated Test Movie"
        self.__repo.update_title(new_title, movie.movie_id)
        self.assertEqual(movie.title, new_title)

    def test_update_description(self):
        movie = Movie(1, "The Test Movie", "A good movie", "Action")
        self.__repo.add_movie(movie)
        new_description = "A better movie"
        self.__repo.update_description(new_description, movie.movie_id)
        self.assertEqual(movie.description, new_description)

    def test_remove_movie(self):
        movie = Movie(1, "The Test Movie", "A good movie", "Action")
        self.__repo.add_movie(movie)
        self.__repo.remove(movie.movie_id)
        self.assertNotIn(movie, self.__repo.display_movies())

    def test_search_by_title(self):
        movie = Movie(1, "The Test Movie", "A good movie", "Action")
        self.__repo.add_movie(movie)
        search_result = self.__repo.search_by_title("Test")
        self.assertIn(movie, search_result)

    def test_search_by_description(self):
        movie = Movie(1, "The Test Movie", "A good movie", "Action")
        self.__repo.add_movie(movie)
        search_result = self.__repo.search_by_description("good")
        self.assertIn(movie, search_result)

    def test_search_by_genre(self):
        movie = Movie(1, "The Test Movie", "A good movie", "Action")
        self.__repo.add_movie(movie)
        search_result = self.__repo.search_by_genre("Action")
        self.assertIn(movie, search_result)

    def test_get_movie_by_id(self):
        movie = Movie(1, "The Test Movie", "A good movie", "Action")
        self.__repo.add_movie(movie)
        retrieved_movie = self.__repo.get_movie_by_id(movie.movie_id)
        self.assertEqual(movie, retrieved_movie)

    def test_already_exists_movie(self):
        movie = Movie(1, "The Test Movie", "A good movie", "Action")
        self.__repo.add_movie(movie)
        duplicate_movie = Movie(1, "Duplicate Movie", "A different movie", "Adventure")
        with self.assertRaises(NotUniqueClientIdError):
            self.__repo.add_movie(duplicate_movie)

    def test_not_found_id_exception(self):
        with self.assertRaises(NotFoundID):
            self.__repo.update_title("New Title", 999)

if __name__ == '__main__':
    unittest.main()

