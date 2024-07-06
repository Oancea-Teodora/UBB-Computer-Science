#
# This is the program's UI module. The user interface and all interaction with the user (print and input statements) are found here
#

from functions import *

def menu():
    print("(A) Add a new expense")
    print("add < sum > < category >")
    print("insert < day > < sum > < category >")
    print()

    print("(B) Modify expenses")
    print("remove < day >")
    print("remove < start day > to < end day >")
    print("remove < category >")
    print()

    print("(C) Display numbers having different properties")
    print("list")
    print("list <category>")
    print("list <category> [ < | = | > ] <value>")
    print()

    print("(D) Filter expenses")
    print("filter < category >")
    print("filter < category > [ < | = | >] < value >")
    print()

    print("(E) Undo")
    print("undo")
    print()

    print("(F) Exit")
    print("exit")
    print()

def introduce():
    option = input(">")
    return option

def errors(ve):
    print("There was an error!")
    print(str(ve))

def display(lists):
    for i in lists:
        print(i)

def display_category(lists, option):
    string = ["housekeeping", "food", "transport", "clothing", "internet", "others"]
    if option[1] in string:
        for i in lists:
            if i["type"] == option[1]:
                print(i)
    else:
        raise ValueError("Invalid output!")


def display_amount_of_money(lists, option):
    string = ["housekeeping", "food", "transport", "clothing", "internet", "others"]
    string2 = ["<", "=", ">"]
    if (option[1] in string) and (option[2] in string2) and (option[3].isnumeric() == True) and len(option) == 4:
        option[3] = int(option[3])
        if option[2] == "<":
            for i in lists:
                if i["type"] == option[1] and i["amount"] < option[3]:
                    print(i)
        elif option[2] == "=":
            for i in lists:
                if i["type"] == option[1] and i["amount"] == option[3]:
                    print(i)
        else:
            for i in lists:
                if i["type"] == option[1] and i["amount"] > option[3]:
                    print(i)
    else:
        raise ValueError("Invalid output!")

def afis():
    print("You reach the initial list!")
    
def invalid():
    print("There was an error!")
    print("Invalid option!")