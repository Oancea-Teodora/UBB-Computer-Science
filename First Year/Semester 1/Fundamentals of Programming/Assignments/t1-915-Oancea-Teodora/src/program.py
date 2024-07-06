# Functions section
def add(name, country, price, list):
    """
    The function adds a new coffee to the list
    :param name: string that tells the name of the coffee
    :param country: string that tells the country of origin
    :param price: float that represents the price of the coffee
    :param list: the list of dictionaries that includes the coffees
    :return: -
    """
    dict = {"1": name, "2": country, "3":price}
    if price>0:
        list.append(dict)
    else:
        raise ValueError("The price is negative!")


def sort_list(list):

    list_sorted = list.copy()
    list_sorted = sorted(list, key=lambda x: x["2"])
    n = len(list_sorted)
    for i in range(0,n-1):
        for j in range (i+1,n):
            if list_sorted[i]["3"] > list_sorted[j]["3"] and list_sorted[i]["2"] == list_sorted[j]["2"]:
                aux = list_sorted[i]
                list_sorted[i] = list_sorted[j]
                list_sorted[j] = aux
    afis(list_sorted)


def filter_coffe(country, price, list, sem_price):

    if len(country) > 0 and sem_price == 1:
        price = float(price)
        sem = 0
        poz = []
        for i in list:
            if i["2"] == country and i["3"] <= price:
                poz.append(i)
                sem = 1
    elif len(country) > 0:
        sem = 0
        poz = []
        for i in list:
            if i["2"] == country:
                poz.append(i)
                sem = 1
    else:
        price = float(price)
        sem = 0
        poz = []
        for i in list:
            if i["3"] <= price:
                poz.append(i)
                sem = 1
    afis2(poz, sem)

def check_price(price):

    sem_price = 0
    if len(price) > 0:
        price = float(price)
        sem_price = 1
    return sem_price


def add_five(list):
    dict = {"1": "Latte", "2": "France", "3": 5.5}
    list.append(dict)
    dict = {"1": "Cappuccino", "2": "France", "3": 7.5}
    list.append(dict)
    dict = {"1": "Long coffee", "2": "France", "3": 3.5}
    list.append(dict)
    dict = {"1": "Short coffee", "2": "Italy", "3": 2.5}
    list.append(dict)
    dict = {"1": "Frappuccino", "2": "Turkey", "3": 9.5}
    list.append(dict)

# User interface section

def afis(list_sorted):
    for i in list_sorted:
        print(i)

def afis2(poz, sem):
    if sem == 1 and len(poz)>0:
        for i in poz:
            print(i)
    else:
        print("No such coffees")


def menu():
    print("Menu")
    print("1 - Add a coffee")
    print("2 - Display all coffees sorted")
    print("3 - Filter coffees")
    print("4 - Exit")

def start():
    menu()
    list = []
    add_five(list)
    opp = 0
    while True:
        print("Choose option")
        opp = input()
        opp = int(opp)
        if opp == 1:
            try:
                print("Introduce coffe name")
                name = input()
                print("Introduce the country of origin")
                country = input()
                print("Introduce price")
                price = input()
                price = float(price)
                add(name, country, price, list)
            except ValueError as ve:
                print("There was an error!")
                print(str(ve))
        elif opp == 2:
            sort_list(list)
        elif opp == 3:
            print("Introduce the country of origin")
            country = input()
            print("Enter the price")
            price = input()
            sem_price = check_price(price)
            filter_coffe(country, price, list, sem_price)
        elif opp == 4:
            print("Bye!")
            break
        else:
            print("Invalid option!")


if __name__ == "__main__":
    start()
