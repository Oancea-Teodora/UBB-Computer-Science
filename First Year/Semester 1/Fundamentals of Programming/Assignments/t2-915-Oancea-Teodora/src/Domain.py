

class Player:
    def __init__(self, player_id, name, strength):
        self.__player_id = player_id
        self.__name = name
        self.__strength = strength

    @property
    def player_id(self):
        return self.__player_id

    @property
    def name(self):
        return self.__name

    @property
    def strength(self):
        return self.__strength

    @player_id.setter
    def player_id(self, player_id):
        self.__player_id = player_id

    @name.setter
    def name(self, name):
        self.__name = name

    @strength.setter
    def strength(self, strength):
        self.__strength = strength

    def __str__(self):
        return "PLayer ID: " + str(self.__player_id) + ", Player name: " + str(self.__name) + ", Player strength: " + str(self.__strength)