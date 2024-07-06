from UI import *


if __name__ == "__main__":
    repo = PlayerRepo()
    serv = PlayerService(repo)
    ui = UI(serv)
    ui.start()
