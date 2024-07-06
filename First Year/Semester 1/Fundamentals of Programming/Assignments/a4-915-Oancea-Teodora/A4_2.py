def dynamicprogramming(dp, s, k, n):
    for i in range (n+1):
        dp[i][0] = 1
    for i in range (1, n+1):
        for j in range(1, k+1):
            if j < s[i-1]:
                dp[i][j] = dp[i-1][j]
            else:
                dp[i][j] = (dp[i-1][j] or dp[i-1][j-s[i-1]])
    for i in range (n+1):
        for j in range (k+1):
            print(dp[i][j],end=" ")
        print()
    return dp[n][k]

s = [2, 3, 5, 7, 8]
k = 14
n = len(s)
dp = [[0] * (k + 1) for _ in range(n + 1)]
if dynamicprogramming(dp, s, k, n) == 1:
    print("The sum", k, "can be formed.")
    print("The numbers are:")
    i = n
    j = k
    while i>0 and j>0:
        if dp[i][j] == 1 and dp[i-1][j] == 0:
            print(s[i-1],end=" ")
            j=j-s[i-1]
        i=i-1
else:
    print("The sum", k, "can't be formed.")
print()
print()
print("Naive version")

def naive(set, n, sum, subset=[]):
    if sum == 0:
        print("The numbers are:")
        for i in subset:
            print(i, end=" ")
        print()
        return 1
    if n == 0:
        return 0
    if set[n - 1] > sum:
        return naive(set, n - 1, sum, subset)
    else:
        return naive(set, n - 1, sum, subset) or naive(set, n - 1, sum - set[n - 1], subset + [set[n - 1]])

set = [2, 3, 5, 7, 8]
sum = 14
n = len(set)
if naive(set, n, sum):
    print("The sum", sum, "can be formed.")
else:
    print("The sum", sum, "can't be formed.")