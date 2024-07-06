from UI import *


if __name__ == "__main__":
    bus_repo = BusRepo()
    bus_service = BusService(bus_repo)
    ui = UI(bus_service)
    ui.start()
