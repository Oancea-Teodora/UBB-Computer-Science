import random
import numpy as np
# Write the implementation for A5 in this file

# Write below this comment 
# Functions to deal with complex numbers
def get_the_real_part(i):
   # return list_list[i][0]  #for lists
    return dict_list[i]["real"]   #for dictionaries

def get_the_imag_part(i):
   #  return list_list[i][1]  #for lists
    return dict_list[i]["imag"]  # for dictionaries

def generate():
    lists = []
    list_real = []
    list2 = []
    for i in range(10):
        list2 = []
        a = random.randint(-50,50)
        b = random.randint(0,50)
        c = str(random.choice("+-"))
        list_real.append(a)
        list2.append(a)
        if c == "+":
            list2.append(b)
        else:
            list2.append(-b)
        if c == "+":
            dictionary = {"real": a, "imag": b}
        else:
            dictionary = {"real": a, "imag": -b}
        dict_list.append(dictionary)
        number = str(a)+ str(c) + str(b) + "i"
        lists.append(number)
        list_list.append(list2)
    return [lists, list_list, dict_list]
def get_real (new_number):
    if new_number.find('+') != -1:
        parts = new_number.split('+')
        real2 = int(parts[0])
    elif new_number.find('-') != -1 and new_number[0] != '-':
        parts = new_number.split('-')
        parts[0] = int(parts[0])
        real2 = parts[0]
    elif new_number.find('-') != -1 and new_number[0] == '-' and new_number.count('-') >= 2:
        parts = new_number.rsplit('-', 1)
        parts[0] = int(parts[0])
        real2 = parts[0]
    elif new_number.find('i') == -1:
        real2 = int(new_number)
    else:
        real2 = 0
    return real2
list_list = []
dict_list = []
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
def problem1(lists, list_list, dict_list):
    """
    The function determine the length and elements of the longest subarray of distinct numbers.
    :param lists: The list of the complex numbers
    :param list_real: The list that contains the real part of the complex numbers
    :param list_img: The list that contains the imaginary part of the complex numbers
    :param dict_list: The dictionary that contains the real and imaginary part of the complex numbers
    :return: The maximum length and the numbers that forms the subarray of distinct numbers
    """
    size = len(list_list)
    index = []
    mx = 0
    for i in range(0,size):
        nr_a = []
        nr_b = []
        index = []
        a = get_the_real_part(i)
        b = get_the_imag_part(i)
        nr_a.append(a)
        nr_b.append(b)
        index.append(i)
        length = 1
        for j in range (i+1, size):
            a2 = get_the_real_part(j)
            b2 = get_the_imag_part(j)
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

def problem11(lists, list_list, dict_list):
    """
    The function determine the length and elements of a maximum subarray sum, when considering each number's real part
    :param lists: The list of the complex numbers
    :param list_real: The list that contains the real part of the complex numbers
    :param list_img: The list that contains the imaginary part of the complex numbers
    :param dict_list: The dictionary that contains the real and imaginary part of the complex numbers
    :return: The length, sum and numbers that forms the maximum subarray sum
    """
    size = len(list_list)
    print(size)
    index_fin = []
    nrr = 1
    nr_fin = 1
    index = []
    index.append(0)
    length = 1
    mx = 0
    a = get_the_real_part(0)
    sum = a
    if sum < 0:
        sum = 0
        index.pop()
        nrr = 0
    for i in range(1, size):
        a2 = get_the_real_part(i)
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
    print("3 - Set A (Problem 1) and Set B (Problem 11) results")
    print("4 - Exit")
    final_list = generate()
    lists = final_list[0]
    list_list = final_list[1]
    dict_list = final_list[2]
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
                list2 = []
                list2.append(get_real(new_number))
                list2.append(get_imag(new_number))
                lists.append(new_number)

                list_list.append(list2)
                dict_list.append({"real": get_real(new_number), "imag": get_imag(new_number)})
        elif opp == 2:
            print("The complex numbers are:")
            for i in lists:
                print(i, end='  ')
            print()
        elif opp == 3:
            rez = problem1(lists, list_list, dict_list)
            print("Problem 1:")
            print("The maximum length is", rez[0])
            print("The numbers are:")
            for i in rez[1]:
                print(lists[i], end='  ')
            print()
            rez2 = problem11(lists, list_list, dict_list)
            print()
            print("Problem 11:")
            print("The sum is", rez2[0])
            print("The maximum length is", rez2[2])
            print("The numbers are:")
            for i in rez2[1]:
                print(lists[i], end='  ')
            print()
        elif opp == 4:
            print("Finish!")
        else:
            print("Invalid option")

if __name__ == "__main__":
    start()
