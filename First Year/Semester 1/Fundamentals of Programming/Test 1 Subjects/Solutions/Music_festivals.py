def add(name, month, cost, artists, list):
    dict = {"1": name, "2": month, "3": cost, "4": artists}
    sem = 1
    for i in list:
        if i["1"] == name:
            sem = 0
    if 1 <= month <= 12 and sem == 1:
        list.append(dict)
    else:
        raise ValueError("The input is incorrect")


def display(season, list):
    if season == "spring":
        poz = []
        m = "april"
        for i in list:
            if i["2"] == 4:
                poz.append(i)
        sorted(poz, key=lambda x: x["1"])
        afis(poz, m)

        poz = []
        m = "mai"
        for i in list:
            if i["2"] == 5:
                poz.append(i)
        sorted(poz, key=lambda x: x["1"])
        afis(poz, m)

        poz = []
        m = "march"
        for i in list:
            if i["2"] == 3:
                poz.append(i)
        sorted(poz, key=lambda x: x["1"])

    elif season == "summer":
        poz = []
        m = "august"
        for i in list:
            if i["2"] == 8:

                poz.append(i)
        sorted(poz, key=lambda x: x["1"])
        afis(poz, m)

        poz = []
        m = "july"
        for i in list:
            if i["2"] == 7:
                poz.append(i)
        sorted(poz, key=lambda x: x["1"])
        afis(poz, m)

        poz = []
        m = "june"
        for i in list:
            if i["2"] == 6:
                poz.append(i)
        sorted(poz, key=lambda x: x["1"])
    elif season == "autumn":
        m = "november"
        poz = []
        for i in list:
            if i["2"] == 11:
                poz.append(i)
        sorted(poz, key=lambda x: x["1"])
        afis(poz,m)

        poz = []
        m = "october"
        for i in list:
            if i["2"] == 10:
                poz.append(i)
        sorted(poz, key=lambda x: x["1"])
        afis(poz,m)

        poz = []
        m = "september"
        for i in list:
            if i["2"] == 9:

                poz.append(i)
        sorted(poz, key=lambda x: x["1"])
        afis(poz, m)
    else:
        m = "december"
        poz = []
        for i in list:
            if i["2"] == 12:
                poz.append(i)
        sorted(poz, key=lambda x: x["1"])
        afis(poz, m)

        poz = []
        m = "february"
        for i in list:
            if i["2"] == 2:
                poz.append(i)
        sorted(poz, key=lambda x: x["1"])
        afis(poz, m)

        poz = []
        m = "january"
        for i in list:
            if i["2"] == 1:
                poz.append(i)
        sorted(poz, key=lambda x: x["1"])
        afis(poz, m)

def display_all (art, list):
    poz = []
    for i in list:
        sem = 0
        for j in i["4"]:
            if j == art:
                sem = 1
        if sem == 1:
            poz.append(i)
    sorted(poz, key=lambda x: x["2"])
    afis2(poz)


def add_artists(n, artists):
    for i in range(n):
        art = input()
        artists.append(art)


def afis(poz,m):
    for i in poz:
        print(i["1"], m, i["3"], i["4"])


def afis2(poz):
    for i in poz:
        print(i)


def menu():
    print("1 - Add a music festival")
    print("2 - Show all the festivals taking place in a given season")
    print("3 - Show all the festivals performed by a given artist")
    print("4 - Exit")


def start():
    menu()
    opp = 1
    list = []
    while True:
        print("Introduce option")
        opp = int(input())
        if opp == 1:
            try:
                print("The name of the festival is")
                name = input()
                print("The month during it takes place is")
                month = int(input())
                print("The ticket cost is")
                cost = int(input())
                print("The number of artists that will perform")
                n = int(input())
                artists = []
                add_artists(n, artists)
                add(name, month, cost, artists, list)
            except ValueError as ve:
                print("There was an error!")
                print(str(ve))
        elif opp == 2:
            print("The season is:")
            season = input()
            display(season, list)
        elif opp == 3:
            print("The artist is:")
            art = input()
            display_all(art, list)
        elif opp == 4:
            print("Bye!")
            break
        else:
            print("Invalid option!")
                

if __name__ == "__main__":
    start()
