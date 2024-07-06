from UI.ui import *

if __name__ == '__main__':
    taxi_repository = TaxiRepo()
    taxi_service = TaxiService(taxi_repository)
    ui = UI(taxi_service)
    ui.start()