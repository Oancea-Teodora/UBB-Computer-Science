import random

def bubblesort (n,l,step):
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
                #I verify the number of steps
                if stp % step == 0:
                    print("Step number", stp, ":")
                    for i in ll:
                        print(i, end=" ")
                    print()
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


def heapsort(n, l, step, stp2):
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
        if stp2 % step == 0:
             print("Step number", stp2, ":")
             for j in l3:
                 print(j, end=" ")
             print()
        #print("intrat")
        stp2 = heapify(l3, ii, 0, stp2, step)
        #print("iesit")

    print("The sorted list is:")
    for i in l3:
        print(i, end=" ")


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
                print(i,end=" ")
            print()
            print("Choose an option")
        elif opp==2:
            if(len(l)==0):
                print("The list is empty. Please select option number 1 first.")
            else:
                bubblesort(n, l, step)
            print()
            print("Choose an option")
        elif opp==3:
            if (len(l) == 0):
                print("The list is empty. Please select option number 1 first.")
            else:
                stp2 = 0
                heapsort(n, l, step, stp2)
            print()
            print("Choose an option")
        elif opp==4:
            print("Finish")
            break
        else:
            print("Invalid output!")


#I printed the options of the menu
print("Menu")
print("1 - Create a random list")
print("2 - Bubble Sort Method")
print("3 - Heap Sort Method")
print("4 - Exit")

print("Choose an option")
op=0
menu(op)