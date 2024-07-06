from src.services.MovieService import *
from src.services.ClientService import *
from src.repository.client_repo import *
from src.repository.movie_repo import *
from jproperties import Properties
from src.ui.ui import *

if __name__ == "__main__":
    config = Properties()

    configfile = open("settings.properties", "rb")

    config.load(configfile)
    if config.get("repository").data == "binary":
        client_repository = BinaryFileRepository1("client_repo.bin")
        movie_repository = BinaryFileRepository2("movie_repo.bin")
        rental_repository = BinaryFileRepository3("rental_repo.bin")
    elif config.get("repository").data == "text":
        client_repository = TextFileRepository1("client_repo.txt")
        movie_repository = TextFileRepository2("movie_repo.txt")
        rental_repository = TextFileRepository3("rental_repo.txt")
    elif config.get("repository").data == "inmemory":
        client_repository = MemoryClientRepo()
        movie_repository = MemoryMovieRepo()
        rental_repository = MemoryRentalRepo()
  #  repo_type = config.get("repository").data

   # client_repository = TextFileRepository1(repo_type)
   # movie_repository = TextFileRepository2(repo_type)
   # rental_repository = TextFileRepository3(repo_type)
    #aaa = MemoryMovieRepo()
    client_services = ClientServices(client_repository)
    movie_services = MovieService(movie_repository)
    rental_services = RentalServices(rental_repository)
    history_services = HistoryService(client_repository, movie_repository, rental_repository)
    ui = UI(client_services, movie_services, rental_services, history_services)
    ui.start()
