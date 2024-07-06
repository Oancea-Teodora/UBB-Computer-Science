class Movie:
    def __init__(self,movie_id, title, description, genre):
        self.__movie_id = movie_id
        self.__title = title
        self.__description = description
        self.__genre = genre


    @property
    def movie_id(self):
        return self.__movie_id

    @property
    def title(self):
        return self.__title

    @property
    def description(self):
        return self.__description

    @property
    def genre(self):
        return self.__genre

    @movie_id.setter
    def movie_id(self, movie_id):
        self.__movie_id = movie_id

    @title.setter
    def title(self, title):
        self.__title = title

    @description.setter
    def description(self, description):
        self.__description = description

    @genre.setter
    def genre(self, genre):
        self.__genre = genre

    def __str__(self):
        return " Movie ID: " + str(self.__movie_id) + ", Title: " + str(self.__title) + ", Description: " + str(self.__description) + ", Genre: " + str(self.__genre)