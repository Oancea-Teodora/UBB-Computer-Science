#
# This module is used to invoke the program's UI and start it. It should not contain a lot of code.
#
from functions import *
from ui import *

def start():
    tests()
    lists = []
    history = []
    generate(lists)
    history.append(copy.deepcopy(lists))
    string = ["housekeeping", "food", "transport", "clothing", "internet", "others"]
    menu()
    while True:
        option = introduce()
        option = option.split(" ")
        if option[0] == "add":
            try:
                add(option, lists)
                history.append(copy.deepcopy(lists))
            except ValueError as ve:
                errors(ve)
        elif option[0] == "insert":
            try:
                insert(option, lists, history)
            except ValueError as ve:
                errors(ve)
        elif option[0] == "remove":
            if len(option) == 2 and option[1].isnumeric() == True:
                try:
                    remove_one_day(option, lists, history)
                except ValueError as ve:
                    errors(ve)
            elif len(option) == 4:
                try:
                    remove_from_to_day(option, lists, history)
                except ValueError as ve:
                    errors(ve)
            else:
                try:
                    remove_category(option, lists, history)
                except ValueError as ve:
                    errors(ve)
        elif option[0] == "list":
            if len(option) == 1:
                display(lists)
            elif len(option) == 2:
                try:
                    display_category(lists, option)
                except ValueError as ve:
                    errors(ve)
            else:
                try:
                    display_amount_of_money(lists, option)
                except ValueError as ve:
                    errors(ve)
        elif option[0] == "filter":
            if len(option) == 2:
                try:
                    filter_1(option, lists, history)
                except ValueError as ve:
                    errors(ve)
            else:
                try:
                    filter_2(option, lists, history)
                except ValueError as ve:
                    errors(ve)
        elif option[0] == "undo":
            if len(history) <= 1:
                afis()
            else:
                history.pop()
                lists = history[len(history)-1].copy()
        elif option[0] == "exit":
            print("Bye!")
            break
        else:
            invalid()

if __name__ == "__main__":
    start()