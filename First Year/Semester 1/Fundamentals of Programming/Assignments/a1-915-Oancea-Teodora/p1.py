# Solve the problem from the first set here
# Problem 4
def function (nn):
    l=[] # I made a list that will hold the digits of the number
    m=0
    # I put the digits in the list
    while nn!=0:
        cif=nn%10
        l.append(cif)
        nn=nn//10
    # I took all the digits from 9 to 0 and made a new number formed from the highest to the lowest digit
    for i in range(9,-1,-1):
        for j in l:
            if j==i:
                m=m*10+j
    return m  

n=input()
n=int(n)
print("The largest natural number written with the same digits is: ")
print(function(n))
