
#def verifdigits()

x = []

def verif_digits(a,b):
    while a!=0:
        cif_a = a%10
        cop = b
        while cop!=0:
            cif_b=cop%10
            if cif_a == cif_b:
                return 1
            cop=cop//10
        a=a//10
    return 0

def backtracking_iterative(vect):
    n = len(vect)
    st = []
    i = 0
    st.append(-1)

    while i >= 0:
        while st[i] < n - 1:
            st[i] = st[i] + 1
            sem = 1
            if i > 0:
                sem = verif_digits(vect[st[i]], vect[st[i - 1]])
                if  vect[st[i]] < vect[st[i - 1]] and st[i] > st[i - 1]:
                    sem = 0
            if sem == 1 and (i == 0 or vect[st[i]] > vect[st[i - 1]]):
                if i >= 1:
                    for j in range(0, i + 1):
                        print(vect[st[j]], end=" ")
                    print()
                i = i + 1
                st.append(st[i - 1])
        st.pop()
        i = i - 1


def backtracking_recursive(k, vect, x):
    if len(x) >= 2:
        for i in x:
            print(i, end=" ")
        print()
    for i in range(k, len(vect)):
        if len(x) == 0 or (verif_digits(x[len(x)-1], vect[i]) == 1 and x[len(x)-1] < vect[i]):
            x.append(vect[i])
            backtracking_recursive(i+1, vect, x)
            x.pop()

vect = [5, 3, 92, 1, 345, 9, 4, 24522, 92, 54, 888882]
print("Recursive backtracking: ")
backtracking_recursive(0, vect, x)
print()
print("Iterative backtracking: ")
backtracking_iterative(vect)

