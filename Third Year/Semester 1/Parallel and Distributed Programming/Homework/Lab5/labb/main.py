from concurrent.futures import ThreadPoolExecutor
import random, time

def add(x, y):
    n = max(len(x), len(y))
    return [(x[i] if i < len(x) else 0) + (y[i] if i < len(y) else 0) for i in range(n)]

def sub(x, y):
    n = max(len(x), len(y))
    return [(x[i] if i < len(x) else 0) - (y[i] if i < len(y) else 0) for i in range(n)]

def naive(a, b):
    c = [0] * (len(a) + len(b) - 1)
    for i in range(len(a)):
        for j in range(len(b)):
            c[i + j] += a[i] * b[j]
    return c

def naive_par(a, b):
    n = len(a) + len(b) - 1

    def coef(k):
        s = 0
        for i in range(len(a)):
            j = k - i
            if 0 <= j < len(b):
                s += a[i] * b[j]
        return s

    with ThreadPoolExecutor() as ex:
        return list(ex.map(coef, range(n)))

def kcore(a, b, par):
    n = max(len(a), len(b))

    if n <= 32:
        return naive(a, b)

    a = a + [0] * (n - len(a))
    b = b + [0] * (n - len(b))

    m = n // 2
    a0, a1 = a[:m], a[m:]
    b0, b1 = b[:m], b[m:]

    if par:
        with ThreadPoolExecutor() as ex:
            f0 = ex.submit(kcore, a0, b0, True)
            f2 = ex.submit(kcore, a1, b1, True)
            f1 = ex.submit(kcore, add(a0, a1), add(b0, b1), True)
            z0, z2, z1 = f0.result(), f2.result(), f1.result()
    else:
        z0 = kcore(a0, b0, False)
        z2 = kcore(a1, b1, False)
        z1 = kcore(add(a0, a1), add(b0, b1), False)

    z1 = sub(sub(z1, z0), z2)

    res = [0] * (2 * n)
    for i, v in enumerate(z0):
        res[i] += v
    for i, v in enumerate(z1):
        res[i + m] += v
    for i, v in enumerate(z2):
        res[i + 2 * m] += v

    return res

def karatsuba_seq(a, b):
    c = kcore(a, b, False)
    return c[:len(a) + len(b) - 1]

def karatsuba_par(a, b):
    c = kcore(a, b, True)
    return c[:len(a) + len(b) - 1]

def to_digits(s):
    return [int(ch) for ch in s.strip()[::-1]]

def from_digits(d):
    carry = 0
    for i in range(len(d)):
        total = d[i] + carry
        d[i] = total % 10
        carry = total // 10
    while carry:
        d.append(carry % 10)
        carry //= 10
    while len(d) > 1 and d[-1] == 0:
        d.pop()
    return ''.join(str(x) for x in d[::-1])

def big_naive(x, y):
    a = to_digits(x)
    b = to_digits(y)
    c = naive(a, b)
    return from_digits(c)

def big_naive_par(x, y):
    a = to_digits(x)
    b = to_digits(y)
    c = naive_par(a, b)
    return from_digits(c)

def big_karatsuba_seq(x, y):
    a = to_digits(x)
    b = to_digits(y)
    c = karatsuba_seq(a, b)
    return from_digits(c)

def big_karatsuba_par(x, y):
    a = to_digits(x)
    b = to_digits(y)
    c = karatsuba_par(a, b)
    return from_digits(c)

if __name__ == "__main__":
    n = 100
    a = [random.randint(0, 9) for _ in range(n)]
    b = [random.randint(0, 9) for _ in range(n)]

    print("polynomial multiplication")
    for name, f in [
        ("naive", naive),
        ("naive_par", naive_par),
        ("karatsuba_seq", karatsuba_seq),
        ("karatsuba_par", karatsuba_par),
    ]:
        t0 = time.time()
        c = f(a, b)
        print(name, "time:", time.time() - t0)

    print("same poly:",
          naive(a, b) == naive_par(a, b) == karatsuba_seq(a, b) == karatsuba_par(a, b))

    print("\nbig integer multiplication (base 10)")
    digits = 200
    x = ''.join(str(random.randint(0, 9)) for _ in range(digits))
    y = ''.join(str(random.randint(0, 9)) for _ in range(digits))

    for name, f in [
        ("big_naive", big_naive),
        ("big_naive_par", big_naive_par),
        ("big_karatsuba_seq", big_karatsuba_seq),
        ("big_karatsuba_par", big_karatsuba_par),
    ]:
        t0 = time.time()
        r = f(x, y)
        print(name, "time:", time.time() - t0)

    print("same big:",
          big_naive(x, y) == big_naive_par(x, y) ==
          big_karatsuba_seq(x, y) == big_karatsuba_par(x, y))
