import random
import numpy as np
# Write the implementation for A5 in this file

# Write below this comment 
# Functions to deal with complex numbers

def get_the_real_part(list_real, list_img, dict_list, i):
    #return list_real[i]  #for lists
    return dict_list[i]["real"]   #for dictionaries

def get_the_imag_part(list_real, list_img, dict_list, i):
    # return list_img[i]  #for lists
    return dict_list[i]["imag"]  # for dictionaries
def generate():
    lists = []
    list_real = []
    list_img = []
    dict_list = []
    for i in range(10):
        a = random.randint(-50,50)
        b = random.randint(0,50)
        c = str(random.choice("+-"))
        list_real.append(a)
        if c == "+":
            list_img.append(b)
        else:
            list_img.append(-b)
        if c == "+":
            dictionary = {"real": a, "imag": b}
        else:
            dictionary = {"real": a, "imag": -b}
        dict_list.append(dictionary)
        number = str(a)+ str(c) + str(b) + "i"
        lists.append(number)
    return [lists, list_real, list_img, dict_list]

def get_real (new_number):
    if new_number.find('+') != -1:
        parts = new_number.split('+')
        real2 = int(parts[0])
    elif new_number.find('-') != -1 and new_number[0] != '-':
        parts = new_number.split('-')
        parts[0] = int(parts[0])
        real2 = parts[0]
    elif new_number.find('-') != -1 and new_number[0] == '-':
        parts = new_number.rsplit('-', 1)
        parts[0] = int(parts[0])
        real2 = parts[0]
    elif new_number.find('i') == -1:
        real2 = int(new_number)
    else:
        real2 = 0
    return real2

def get_imag (new_number):
    if new_number.find('i') != -1:
        if new_number.find('+i') != -1:
            imag = 1
        elif new_number.find('-i') != -1:
            imag = -1
        elif new_number.find('+') != -1:
            parts = new_number.split('+')
            imag = int(parts[1].replace('i',''))
        elif new_number.find('-') != -1 and new_number[0] != '-':
            parts = new_number.split('-')
            imag = int(parts[1].replace('i', ''))
            imag = -imag
        elif new_number.find('-') != -1 and new_number[0] == '-':
            parts = new_number.rsplit('-', 1)
            imag = int(parts[1].replace('i', ''))
            imag = -imag
        else:
            imag = int(new_number.replace('i', ''))
    else:
        imag = 0
    return imag

# Write below this comment 
# Functions that deal with subarray/subsequence properties
# -> There should be no print or input statements in this section 
# -> Each function should do one thing only
# -> Functions communicate using input parameters and their return values
def problem1(lists, list_real, list_img, dict_list):
    size = len(list_real)
    index = []
    mx = 0
    for i in range(0,size):
        nr_a = []
        nr_b = []
        index = []
        a = get_the_real_part(list_real, list_img, dict_list, i)
        b = get_the_imag_part(list_real, list_img, dict_list, i)
        nr_a.append(a)
        nr_b.append(b)
        index.append(i)
        length = 1
        for j in range (i+1, size):
            a2 = get_the_real_part(list_real, list_img, dict_list, j)
            b2 = get_the_imag_part(list_real, list_img, dict_list, j)
            sem = 1
            for k in range(len(nr_a)):
                if nr_a[k] == a2 and nr_b[k] == b2:
                    sem = 0
            if sem == 1:
                length = length + 1
                nr_a.append(a2)
                nr_b.append(b2)
                index.append(j)
            if length > mx:
                mx = length
                index_fin = index.copy()
            if sem == 0:
                break
    return [mx, index_fin]

def problem11(lists, list_real, list_img, dict_list):
    size = len(list_real)
    index_fin = []
    nrr = 1
    nr_fin = 1
    index = []
    index.append(0)
    length = 1
    mx = 0
    a = get_the_real_part(list_real, list_img, dict_list, 0)
    sum = a
    if sum < 0:
        sum = 0
        index.pop()
        nrr = 0
    for i in range(1, size):
        a2 = get_the_real_part(list_real, list_img, dict_list, i)
        if sum > mx:
            mx = sum
            index_fin = index.copy()
            nr_fin = nrr
        if sum + a2 > 0:
            sum = sum + a2
            index.append(i)
            nrr = nrr + 1
        else:
            sum = 0
            index = []
            nrr = 0
    if sum > mx:
        mx = sum
        index_fin = index.copy()
        nr_fin = nrr
    return [mx, index_fin, nr_fin]

# Write below this comment 
# UI section
def start():
    print("Menu")
    print("1 - Read a list of complex numbers (in z = a+bi form) from the console.")
    print("2 - Display the entire list of numbers on the console.")
    print("3 - Set A and Set B problems")
    print("4 - Exit")
    final_list = generate()
    lists = final_list[0]
    list_real = final_list[1]
    list_img = final_list[2]
    dict_list = final_list[3]
    opp = -1
    while(opp != 4):
        print("Choose an option:")
        opp = input()
        opp = int(opp)
        if opp == 1:
            print("How many numbers do you want to add?")
            n = input()
            n = int(n)
            for i in range(n):
                new_number = input()
                list_real.append(get_real(new_number))
                list_img.append(get_imag(new_number))
                lists.append(new_number)
                dict_list.append({"real": get_real(new_number), "imag": get_imag(new_number)})

        elif opp == 2:
            for i in lists:
                print(i, end=', ')
            print()
            for i in dict_list:
                print(i)
        elif opp == 3:
            rez = problem1(lists, list_real, list_img, dict_list)
            print("Problem 1:")
            print("The maximum length is", rez[0])
            print("The numbers are:")
            for i in rez[1]:
                print(lists[i], end=', ')
            print()
            rez2 = problem11(lists, list_real, list_img, dict_list)
            print("Problem 11:")
            print("The sum is", rez2[0])
            print("The maximum length is", rez2[2])
            print("The numbers are:")
            for i in rez2[1]:
                print(lists[i], end=', ')
            print()
        else:
            print("Invalid option")

if __name__ == "__main__":
    start()



