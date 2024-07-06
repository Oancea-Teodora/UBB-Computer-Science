import random
import timeit
import time

"""
Bubble sort

-Best case: O(n)
-Worst case: O(n^2)
-Average case: O(n^2)

The best case occurs when the array is already sorted. So the number of comparisons required is N-1.

The worst-case condition for bubble sort occurs when elements of the array are arranged in decreasing order.
In the worst case, the total number of swaps is N(N-1)/2

In the case of the standard version of the bubble sort, we need to do N iterations. In each iteration, we do the comparison and we perform swapping if required.
In this way, the total number of comparison will be:
(N-1)+(N-2)+...+2+1=N*(N-1)/2

Heap sort

-Best case: O(n*log n)
-Worst case: O(n*log n)
-Average case: O(n*log n)

The best case for heapsort would happen when all elements in the list are sorted.

In the heapify() function, we walk through the tree from top to bottom. The height of a binary tree (the root not being counted) of size n is log2 n.
The heapify() method is called n-1 times. So the total complexity for repairing the heap is also O(n log n).

"""


def bubblesort (n,l,step, opp):
    ll = l.copy()
    sort = 0
    stp = 0
    while sort==0:
        sort=1
        for i in range(0,n-1):
            #I compare the numbers in the list two by two and swap the if they are descending
            if ll[i] > ll[i+1]:
                stp = stp+1
                aux = ll[i]
                ll[i] = ll[i+1]
                ll[i+1] = aux
                sort = 0
                #I verify the number of steps
                if stp % step == 0 and opp == 2:
                    print("Step number", stp, ":")
                    for i in ll:
                        print(i, end=" ")
                    print()
    print("The sorted list is:")
    for i in ll:
        print(i, end=" ")

def bubblesort1 (n,l):
    ll=l.copy()
    sort=0
    stp=0
    while sort==0:
        sort=1
        for i in range(0,n-1):
            #I compare the numbers in the list two by two and swap the if they are descending
            if ll[i]>ll[i+1]:
                stp=stp+1
                aux=ll[i]
                ll[i]=ll[i+1]
                ll[i+1]=aux
                sort=0
    print("The sorted list is:")
    for i in ll:
        print(i, end=" ")

def heapify(l3, n, i, stp2, step):
    #I determine the biggest number between the father and the sons
    big = i
    left = 2*i+1
    right = 2*i+2
    if left < n and l3[left] > l3[big]:
        big = left
    if right < n and l3[right] > l3[big]:
        big = right
    #I swap if the father with one of the son if the father is not the biggest one
    if big != i:
        aux = l3[i]
        l3[i] = l3[big]
        l3[big] = aux
        stp2 = stp2 + 1
        # I verify the number of steps
        if stp2 % step == 0:
             print("Step number", stp2, ":")
             for j in l3:
                 print(j, end=" ")
             print()
        stp2 = heapify(l3, n, big, stp2, step)
    return stp2


def heapsort(n, l, step, stp2, opp):
    #I copied the list
    l3 = l.copy()
    n = len(l3)
    #I applied for the first time heapify and the root will be the biggest number
    for i in range(n//2-1, -1, -1):
        stp2 = heapify(l3, n, i, stp2, step)
    #I change the root of the tree (the bigest number) with the last number in the list
    for ii in range(n-1, 0, -1):
        aux = l3[0]
        l3[0] = l3[ii]
        l3[ii] = aux
        stp2 = stp2 + 1
        if stp2 % step == 0 and opp == 3:
             print("Step number", stp2, ":")
             for j in l3:
                 print(j, end=" ")
             print()
        stp2 = heapify(l3, ii, 0, stp2, step)

    print("The sorted list is:")
    for i in l3:
        print(i, end=" ")

def heapify1(l3, n, i):
    #I determine the biggest number between the father and the sons
    big = i
    left = 2*i+1
    right = 2*i+2
    if left < n and l3[left] > l3[big]:
        big = left
    if right < n and l3[right] > l3[big]:
        big = right
    #I swap if the father with one of the son if the father is not the biggest one
    if big != i:
        aux = l3[i]
        l3[i] = l3[big]
        l3[big] = aux
        heapify1(l3, n, big)


def heapsort1(n, l):
    #I copied the list
    l3 = l.copy()
    n = len(l3)
    #I applied for the first time heapify and the root will be the biggest number
    for i in range(n//2-1, -1, -1):
        stp2 = heapify1(l3, n, i)
    #I change the root of the tree (the bigest number) with the last number in the list
    for ii in range(n-1, 0, -1):
        aux = l3[0]
        l3[0] = l3[ii]
        l3[ii] = aux
        heapify1(l3, ii, 0)

    print("The sorted list is:")
    for i in l3:
        print(i, end=" ")

list1 = []
list2 = []
list3 = []
list4 = []
list5 = []
def createlists(n):
    list1.clear()
    list2.clear()
    list3.clear()
    list4.clear()
    list5.clear()
    lung2 = 2*n
    lung3 = 2*lung2
    lung4 = 2*lung3
    lung5 = 2*lung4
    for i in range(n):
        list1.append(random.randint(0,100))
    for i in range(lung2):
        list2.append(random.randint(0,100))
    for i in range(lung3):
        list3.append(random.randint(0,100))
    for i in range(lung4):
        list4.append(random.randint(0,100))
    for i in range(lung5):
        list5.append(random.randint(0,100))
    lung = n

def bestcase(n,opp):
    print("The best case for bubble sort is when the lists are already sorted.")
    createlists(n)
    list11 = []
    list22 = []
    list33 = []
    list44 = []
    list55 = []
    """
    list11.append(list1[0])
    list22.append(list2[0])
    list33.append(list3[0])
    list44.append(list4[0])
    list55.append(list5[0])
    """
    list1.sort()
    list2.sort()
    list3.sort()
    list4.sort()
    list5.sort()
    #bubble sort
    print("The lists for the best case for the bubble sort algorithm.")
    print()
    print("The first list is:")
    for i in list1:
        print(i, end=" ")
    print()
    default_time = timeit.default_timer()
    bubblesort1(n,list1)
    end_time = timeit.default_timer()
    total_time = end_time - default_time
    print()
    print("The time taken for the first list is:", total_time)
    print()

    print("The second list is:")
    for i in list2:
        print(i, end=" ")
    print()
    default_time = timeit.default_timer()
    bubblesort1(n,list2)
    end_time = timeit.default_timer()
    total_time = end_time - default_time
    print()
    print("The time taken for the second list is: ",total_time)
    print()

    print("The third list is:")
    for i in list3:
        print(i, end=" ")
    print()
    default_time = timeit.default_timer()
    bubblesort1(n,list3)
    end_time = timeit.default_timer()
    total_time = end_time - default_time
    print()
    print("The time taken for the third list is: ",total_time)
    print()

    print("The fourth list is:")
    for i in list4:
        print(i, end=" ")
    print()
    default_time = timeit.default_timer()
    bubblesort1(n,list4)
    end_time = timeit.default_timer()
    total_time = end_time - default_time
    print()
    print("The time taken for the fourth list is: ",total_time)
    print()

    print("The fifth list is:")
    for i in list5:
        print(i, end=" ")
    print()
    default_time = timeit.default_timer()
    bubblesort1(n, list5)
    end_time = timeit.default_timer()
    total_time = end_time - default_time
    print()
    print("The time taken for the fifth list is: ",total_time)
    print()

    """for i in range(1,n):
        list11.append(list11[0])
        list22.append(list22[0])
        list33.append(list33[0])
        list44.append(list44[0])
        list55.append(list55[0])"""
    list1.sort(reverse=True)
    list2.sort(reverse=True)
    list3.sort(reverse=True)
    list4.sort(reverse=True)
    list5.sort(reverse=True)

    #heap sort

    print("The best case for heap sort is when the elements are sorted in reverse order.")
    print()
    print("The first list is:")
    for i in list1:
        print(i, end=" ")
    print()
    default_time = timeit.default_timer()
    heapsort1(n, list1)
    end_time = timeit.default_timer()
    total_time = end_time - default_time
    print()
    print("The time taken for the first list is: ",total_time)
    print()

    print("The second list is:")
    for i in list2:
        print(i, end=" ")
    print()
    default_time = timeit.default_timer()
    heapsort1(n, list2)
    end_time = timeit.default_timer()
    total_time = end_time - default_time
    print()
    print("The time taken for the first list is: ", total_time)
    print()

    print("The third list is:")
    for i in list3:
        print(i, end=" ")
    print()
    default_time = timeit.default_timer()
    heapsort1(n, list3)
    end_time = timeit.default_timer()
    total_time = end_time - default_time
    print()
    print("The time taken for the first list is: ", total_time)
    print()

    print("The fourth list is:")
    for i in list4:
        print(i, end=" ")
    print()
    default_time = timeit.default_timer()
    heapsort1(n, list4)
    end_time = timeit.default_timer()
    total_time = end_time - default_time
    print()
    print("The time taken for the first list is: ", total_time)
    print()

    print("The fifth list is:")
    for i in list5:
        print(i, end=" ")
    print()
    default_time = timeit.default_timer()
    heapsort1(n, list5)
    end_time = timeit.default_timer()
    total_time = end_time - default_time
    print()
    print("The time taken for the first list is: ", total_time)
    print()

def averagecase(n,opp):
    # bubble sort
    print("The lists for the average case for bubble sort.")
    print()
    print("The first list is:")
    for i in list1:
        print(i, end=" ")
    print()
    default_time = timeit.default_timer()
    bubblesort1(n, list1)
    end_time = timeit.default_timer()
    total_time = end_time - default_time
    print()
    print("The time taken for the first list is:", total_time)
    print()

    print("The second list is:")
    for i in list2:
        print(i, end=" ")
    print()
    default_time = timeit.default_timer()
    bubblesort1(n, list2)
    end_time = timeit.default_timer()
    total_time = end_time - default_time
    print()
    print("The time taken for the second list is:", total_time)
    print()

    print("The third list is:")
    for i in list3:
        print(i, end=" ")
    print()
    default_time = timeit.default_timer()
    bubblesort1(n, list3)
    end_time = timeit.default_timer()
    total_time = end_time - default_time
    print()
    print("The time taken for the third list is:", total_time)
    print()

    print("The fourth list is:")
    for i in list4:
        print(i, end=" ")
    print()
    default_time = timeit.default_timer()
    bubblesort1(n, list4)
    end_time = timeit.default_timer()
    total_time = end_time - default_time
    print()
    print("The time taken for the fourth list is:", total_time)
    print()

    print("The fifth list is:")
    for i in list5:
        print(i, end=" ")
    print()
    default_time = timeit.default_timer()
    bubblesort1(n, list5)
    end_time = timeit.default_timer()
    total_time = end_time - default_time
    print()
    print("The time taken for the fifth list is:", total_time)
    print()

    # heap sort

    print("The average case for heap sort.")
    print()
    print("The first list is:")
    for i in list1:
        print(i, end=" ")
    print()
    default_time = timeit.default_timer()
    heapsort1(n, list1)
    end_time = timeit.default_timer()
    total_time = end_time - default_time
    print()
    print("The time taken for the first list is: ",total_time)
    print()

    print("The second list is:")
    for i in list2:
        print(i, end=" ")
    print()
    default_time = timeit.default_timer()
    heapsort1(n, list2)
    end_time = timeit.default_timer()
    total_time = end_time - default_time
    print()
    print("The time taken for the second list is: ",total_time)
    print()

    print("The third list is:")
    for i in list3:
        print(i, end=" ")
    print()
    default_time = timeit.default_timer()
    heapsort1(n, list3)
    end_time = timeit.default_timer()
    total_time = end_time - default_time
    print()
    print("The time taken for the third list is: ",total_time)
    print()

    print("The fourth list is:")
    for i in list4:
        print(i, end=" ")
    print()
    default_time = timeit.default_timer()
    heapsort1(n, list4)
    end_time = timeit.default_timer()
    total_time = end_time - default_time
    print()
    print("The time taken for the fourth list is: ",total_time)
    print()

    print("The fifth list is:")
    for i in list5:
        print(i, end=" ")
    print()
    default_time = timeit.default_timer()
    heapsort1(n, list5)
    end_time = timeit.default_timer()
    total_time = end_time - default_time
    print()
    print("The time taken for the fifth list is: ",total_time)
    print()

def worstcase(n,opp):
    print("The worst case for bubble sort is when the lists are already sorted in descending order.")
    createlists(n)
    list1.sort(reverse=True)
    list2.sort(reverse=True)
    list3.sort(reverse=True)
    list4.sort(reverse=True)
    list5.sort(reverse=True)
    # bubble sort
    print("The lists for the best case for the bubble sort algorithm.")
    print()
    print("The first list is:")
    for i in list1:
        print(i, end=" ")
    print()
    default_time = timeit.default_timer()
    bubblesort1(n, list1)
    end_time = timeit.default_timer()
    total_time = end_time - default_time
    print()
    print("The time taken for the first list is: ",total_time)
    print()

    print("The second list is:")
    for i in list2:
        print(i, end=" ")
    print()
    default_time = timeit.default_timer()
    bubblesort1(n, list2)
    end_time = timeit.default_timer()
    total_time = end_time - default_time
    print()
    print("The time taken for the second list is: ", total_time)
    print()

    print("The third list is:")
    for i in list3:
        print(i, end=" ")
    print()
    default_time = timeit.default_timer()
    bubblesort1(n, list3)
    end_time = timeit.default_timer()
    total_time = end_time - default_time
    print()
    print("The time taken for the third list is: ",total_time)
    print()

    print("The fourth list is:")
    for i in list4:
        print(i, end=" ")
    print()
    default_time = timeit.default_timer()
    bubblesort1(n, list4)
    end_time = timeit.default_timer()
    total_time = end_time - default_time
    print()
    print("The time taken for the fourth list is: ",total_time)
    print()

    print("The fifth list is:")
    for i in list5:
        print(i, end=" ")
    print()
    default_time = timeit.default_timer()
    bubblesort1(n, list5)
    end_time = timeit.default_timer()
    total_time = end_time - default_time
    print()
    print("The time taken for the fifth list is: ",total_time)
    print()

    list1.sort()
    list2.sort()
    list3.sort()
    list4.sort()
    list5.sort()
    # heap sort
    print("The worst case for heap sort is when the lists are already sorted in descending order.")
    print()
    print("The first list is:")
    for i in list1:
        print(i, end=" ")
    print()
    default_time = timeit.default_timer()
    heapsort1(n, list1)
    end_time = timeit.default_timer()
    total_time = end_time - default_time
    print()
    print("The time taken for the first list is: ",total_time)
    print()

    print("The second list is:")
    for i in list2:
        print(i, end=" ")
    print()
    default_time = timeit.default_timer()
    heapsort1(n, list2)
    end_time = timeit.default_timer()
    total_time = end_time - default_time
    print()
    print("The time taken for the second list is: ",total_time)
    print()

    print("The third list is:")
    for i in list3:
        print(i, end=" ")
    print()
    default_time = timeit.default_timer()
    heapsort1(n, list3)
    end_time = timeit.default_timer()
    total_time = end_time - default_time
    print()
    print("The time taken for the third list is: ",total_time)
    print()

    print("The fourth list is:")
    for i in list4:
        print(i, end=" ")
    print()
    default_time = timeit.default_timer()
    heapsort1(n, list4)
    end_time = timeit.default_timer()
    total_time = end_time - default_time
    print()
    print("The time taken for the fourth list is: ",total_time)
    print()

    print("The fifth list is:")
    for i in list5:
        print(i, end=" ")
    print()
    default_time = timeit.default_timer()
    heapsort1(n, list5)
    end_time = timeit.default_timer()
    total_time = end_time - default_time
    print()
    print("The time taken for the fifth list is: ", total_time)
    print()

#While opp isn't four, the program will work
def menu(opp):
    l=[]
    while opp != 4:
        opp = input()
        opp = int(opp)
        #For option number 1, we will introduce the length of the list and number of steps
        if opp == 1:
            l = []
            print("Introduce the length of the list: ")
            n = input()
            n = int(n)
            print("Introduce number of steps: ")
            step = input()
            step = int(step)
            for i in range(n):
                l.append(random.randint(0,100))
            print("The list is: ")
            for i in l:
                print(i, end=" ")
            print()
            print("Choose an option")

        elif opp == 2:
            if(len(l)==0):
                print("The list is empty. Please select option number 1 first.")
            else:
                bubblesort(n, l, step, opp)
            print()
            print("Choose an option")
        elif opp==3:
            if (len(l) == 0):
                print("The list is empty. Please select option number 1 first.")
            else:
                stp2 = 0
                heapsort(n, l, step, stp2, opp)
            print()
            print("Choose an option")
        elif opp == 4:
            print("Finish")
            break
        elif opp == 5:
            print("Introduce the length of the first list:")
            n = input()
            n = int(n)
            bestcase(n,opp)
            print()
            print("Choose an option")
        elif opp == 6:
            print("Introduce the length of the first list:")
            n = input()
            n = int(n)
            createlists(n)
            averagecase(n,opp)
            print()
            print("Choose an option")
        elif opp == 7:
            print("Introduce the length of the first list:")
            n = input()
            n = int(n)
            createlists(n)
            worstcase(n,opp)
            print()
            print("Choose an option")
        else:
            print("Invalid output!")


#I printed the option of the menu
print("Menu")
print("1 - Create a random list")
print("2 - Bubble Sort Method")
print("3 - Heap Sort Method")
print("4 - Exit")
print("5 - The best case for the sorting algorithm")
print("6 - The average case for the sorting algorithm")
print("7 - The worst case for the sorting algorithm")
print("Choose an option")
op=0
menu(op)