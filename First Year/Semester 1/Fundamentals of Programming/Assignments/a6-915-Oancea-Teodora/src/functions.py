#
# The program's functions are implemented here. There is no user interaction in this file, therefore no input/print statements. Functions here
# communicate via function parameters, the return statement and raising of exceptions. 
#
import copy
from random import randint
from datetime import date

def add(option, lists):
    """
    This function adds a new expense.
    :param option: string that tells the option
    :param lists: the list of the expenses
    :return: -
    """
    string = ["housekeeping", "food", "transport", "clothing", "internet", "others"]
    cdt = date.today()
    today_fin = cdt.day
    if option[1].isnumeric() == True and option[2] in string and len(option) == 3:
        option[1] = int(option[1])
        dict = {"day": today_fin, "amount": option[1], "type": option[2]}
        lists.append(dict)
        list2 = lists.copy()
       # history.append(list2)
    else:
        raise ValueError("Invalid output!")


def insert(option, lists, history):
    """
    This function adds a new expense.
    :param option: string that tells the option
    :param lists: the list of the expenses
    :param history: the list that has all the commands used for undo
    :return: -
    """
    string = ["housekeeping", "food", "transport", "clothing", "internet", "others"]
    if option[1].isnumeric() == True and option[2].isnumeric() == True and option[3] in string and len(option) == 4 and 1 <= int(option[1]) <= 31:
        option[1] = int(option[1])
        option[2] = int(option[2])
        dict = {"day": option[1], "amount": option[2], "type": option[3]}
        lists.append(dict)
        history.append(copy.deepcopy(lists))
    else:
        raise ValueError("Invalid output!")


def remove_one_day(option, lists, history):
    """
    This function removes an expense
    :param option: string that tells the option
    :param lists: the list of the expenses
    :param history: the list that has all the commands used for undo
    :return: -
    """
    if option[1].isnumeric() == True and 1 <= int(option[1]) <= 31:
        option[1] = int(option[1])
        sem = 0
        while sem == 0:
            sem = 1
            for i in lists:
                if i["day"] == option[1]:
                    lists.remove(i)
                    sem = 0
        history.append(copy.deepcopy(lists))
    else:
        raise  ValueError("Invalid output!")


def remove_from_to_day(option, lists, history):
    """
    This function removes an expense
    :param option: string that tells the option
    :param lists: the list of the expenses
    :param history: the list that has all the commands used for undo
    :return: -
    """
    if option[1].isnumeric() == True and option[2] == "to" and option[3].isnumeric() == True and 1 <= int(option[1]) <= 31 and 1 <= int(option[3]) <= 31:
        option[1] = int(option[1])
        option[3] = int(option[3])
        sem = 0
        while sem == 0:
            sem = 1
            for i in range(option[1], option[3] + 1):
                for j in lists:
                    if j["day"] == i:
                        lists.remove(j)
                        sem = 0
        history.append(copy.deepcopy(lists))
    else:
        raise ValueError("Invalid output!")


def remove_category(option, lists, history):
    """
    This function removes an expense
    :param option: string that tells the option
    :param lists: the list of the expenses
    :param history: the list that has all the commands used for undo
    :return: -
    """
    string = ["housekeeping", "food", "transport", "clothing", "internet", "others"]
    if option[1] in string and len(option) == 2:
        sem = 0
        while sem == 0:
            sem = 1
            for i in lists:
                if i["type"] == option[1]:
                    lists.remove(i)
                    sem = 0
        history.append(copy.deepcopy(lists))
    else:
        raise ValueError("Invalid output!")


def filter_1(option, lists, history):
    """
    This function filters an expense and it will remain in the list just what it should
    :param option: string that tells the option
    :param lists: the list of the expenses
    :param history: the list that has all the commands used for undo
    :return: -
    """
    string = ["housekeeping", "food", "transport", "clothing", "internet", "others"]
    if option[1] in string:
        sem = 0
        while sem == 0:
            sem = 1
            for i in lists:
                if i["type"] != option[1]:
                    lists.remove(i)
                    sem = 0
        history.append(copy.deepcopy(lists))
    else:
        raise ValueError("Invalid option!")


def filter_2(option, lists, history):
    """
    This function filters an expense and it will remain in the list just what it should
    :param option: string that tells the option
    :param lists: the list of the expenses
    :param history: the list that has all the commands used for undo
    :return: -
    """
    string = ["housekeeping", "food", "transport", "clothing", "internet", "others"]
    string2 = ["<", "=", ">"]
    if option[1] in string and option[2] in string2 and option[3].isnumeric() == True and len(option) == 4:
        if option[2] == "<":
            sem = 0
            while sem == 0:
                sem = 1
                for i in lists:
                    if i["type"] != option[1]:
                        lists.remove(i)
                        sem = 0
                    elif i["amount"] >= int(option[3]):
                        lists.remove(i)
                        sem = 0
        elif option[2] == "=":
            sem = 0
            while sem == 0:
                sem = 1
                for i in lists:
                    if i["type"] != option[1]:
                        lists.remove(i)
                        sem = 0
                    elif i["amount"] != int(option[3]):
                        lists.remove(i)
                        sem = 0
        else:
            sem = 0
            while sem == 0:
                sem = 1
                for i in lists:
                    if i["type"] != option[1]:
                        lists.remove(i)
                        sem = 0
                    elif i["amount"] <= int(option[3]):
                        lists.remove(i)
                        sem = 0
        history.append(copy.deepcopy(lists))
    else:
        raise ValueError("Invalid output!")


def generate(lists):
    """
    This function generates 10 random expenses
    :param lists: the list of expenses
    :return:
    """
    for i in range(10):
        day = randint(1, 30)
        amount = randint(1, 200)
        string = ["housekeeping", "food", "transport", "clothing", "internet", "others"]
        types = string[randint(0,5)]
        dict = {"day": day, "amount": amount, "type": types}
        lists.append(dict)


def test_add():
    option2 = "add 20 internet"
    option2 = option2.split(" ")
    test_list = []
    add(option2, test_list)
    dict = {"day": 28, "amount": 20, "type": "internet"}
    result = []
    result.append(dict)
    assert test_list == result


def test_insert():
    test_list = []
    result = []
    dict = {"day": 28, "amount": 20, "type": "internet"}
    result.append(dict)
    his = []
    option = "insert 28 20 internet"
    option = option.split(" ")
    insert(option, test_list, his)
    assert result == test_list


def test_remove_one_day():
    test_list = []
    result = []
    dict = {"day": 28, "amount": 20, "type": "internet"}
    result.append(dict)
    test_list.append(dict)
    his = []
    dict = {"day": 22, "amount": 10, "type": "others"}
    test_list.append(dict)
    option = "remove 22"
    option = option.split(" ")
    remove_one_day(option, test_list, his)
    assert  test_list == result


def test_remove_from_to_day():
    test_list = []
    result = []
    dict = {"day": 28, "amount": 20, "type": "internet"}
    result.append(dict)
    test_list.append(dict)
    his = []
    dict = {"day": 22, "amount": 10, "type": "others"}
    test_list.append(dict)
    dict = {"day": 19, "amount": 60, "type": "transportation"}
    test_list.append(dict)
    option = "remove 18 to 23"
    option = option.split(" ")
    remove_from_to_day(option, test_list, his)
    assert test_list == result


def test_remove_category():
    test_list = []
    result = []
    dict = {"day": 28, "amount": 20, "type": "internet"}
    result.append(dict)
    test_list.append(dict)
    his = []
    dict = {"day": 22, "amount": 10, "type": "others"}
    test_list.append(dict)
    option = "remove others"
    option = option.split(" ")
    remove_category(option, test_list, his)
    assert test_list == result


def tests():
    test_add()
    test_insert()
    test_remove_one_day()
    test_remove_from_to_day()
    test_remove_category()
