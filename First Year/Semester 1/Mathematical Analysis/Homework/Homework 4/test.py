import math

def summ(n, p, q):
    s = 0
    i = 1
    while i < n:
        for j in range(1, p+1):
            s = s + 1/i
            i = i+1
        for k in range(1, q+1):
            s = s - 1/i
            i = i+1
    print("The sum is:", s)


def rearranged(n, p, q):
    result = 0
    for i in range(1, p + 1):
        result = result + 1 / (2 * i - 1)
    for i in range(1, q + 1):
        result = result - 1 / (2 * i)
    return result


print("Introduce n:")
n = input()
n = int(n)
print("Introduce p:")
p = input()
p = int(p)
print("Introduce q:")
q = input()
q = int(q)
if p == 1 and q == 1:
    summ(n, p, q)
else:
    print(rearranged(n, p, q))
ln2 = math.log(2)
print("ln(2):", ln2)