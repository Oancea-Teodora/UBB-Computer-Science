
from src.Board.board import *
from src.Game.game import *
from src.UI.UI import UI


def main():
    board = Board(1000, 100)
    game = Game(board)
    ui = UI(game)

    ui.start()

main()