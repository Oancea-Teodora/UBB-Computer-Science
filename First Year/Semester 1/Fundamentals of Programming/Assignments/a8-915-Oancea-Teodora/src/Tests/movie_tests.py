import unittest
from src.repository.movie_repo import *
from src.domain.movie import *

class MyTestCase(unittest.TestCase):
    def setUp(self):
        self.__repo = MovieRepo()

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


if __name__ == '__main__':
    unittest.main()
