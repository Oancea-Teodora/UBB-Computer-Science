from mpi4py import MPI
from array import array
import random, sys, time

comm = MPI.COMM_WORLD
rank = comm.Get_rank()
size = comm.Get_size()

def add(x, y):
    n = max(len(x), len(y))
    return [(x[i] if i < len(x) else 0) + (y[i] if i < len(y) else 0) for i in range(n)]

def sub(x, y):
    n = max(len(x), len(y))
    return [(x[i] if i < len(x) else 0) - (y[i] if i < len(y) else 0) for i in range(n)]

def naive(a, b):
    c = [0] * (len(a) + len(b) - 1)
    for i in range(len(a)):
        ai = a[i]
        for j in range(len(b)):
            c[i + j] += ai * b[j]
    return c


def next_pow2(n):
    p = 1
    while p < n:
        p *= 2
    return p

def kcore(a, b):
    n = len(a)
    if n <= 32:
        return naive(a, b)

    m = n // 2
    a0, a1 = a[:m], a[m:]
    b0, b1 = b[:m], b[m:]

    z0 = kcore(a0, b0)
    z2 = kcore(a1, b1)
    z1 = kcore(add(a0, a1), add(b0, b1))
    mid = sub(sub(z1, z0), z2)

    res = [0] * (2 * n - 1)
    for i, v in enumerate(z0): res[i] += v
    for i, v in enumerate(mid): res[i + m] += v
    for i, v in enumerate(z2): res[i + 2*m] += v
    return res

def karatsuba_seq(a, b):
    la, lb = len(a), len(b)
    n = next_pow2(max(la, lb))
    aa = a + [0] * (n - la)
    bb = b + [0] * (n - lb)
    c = kcore(aa, bb)
    return c[:la + lb - 1]


def naive_mpi(a, b):
    n = len(a)
    m = len(a) + len(b) - 1

    start = (rank * n) // size
    end = ((rank + 1) * n) // size

    local = array('q', [0]) * m
    for i in range(start, end):
        ai = a[i]
        for j in range(len(b)):
            local[i + j] += ai * b[j]

    if rank == 0:
        out = array('q', [0]) * m
        comm.Reduce([local, MPI.LONG_LONG], [out, MPI.LONG_LONG], op=MPI.SUM, root=0)
        return list(out)
    else:
        comm.Reduce([local, MPI.LONG_LONG], None, op=MPI.SUM, root=0)
        return None


def karatsuba_mpi_top(a, b):
    if rank == 0:
        la, lb = len(a), len(b)
        n = next_pow2(max(la, lb))
        aa = a + [0] * (n - la)
        bb = b + [0] * (n - lb)
        m = n // 2

        a0, a1 = aa[:m], aa[m:]
        b0, b1 = bb[:m], bb[m:]
        sA = add(a0, a1)
        sB = add(b0, b1)

        z0 = karatsuba_seq(a0, b0)

        if size > 1:
            comm.send((a1, b1), dest=1, tag=1)
        else:
            z2 = karatsuba_seq(a1, b1)

        if size > 2:
            comm.send((sA, sB), dest=2, tag=2)
        else:
            z1 = karatsuba_seq(sA, sB)

        if size > 1: z2 = comm.recv(source=1, tag=10)
        if size > 2: z1 = comm.recv(source=2, tag=20)

        mid = sub(sub(z1, z0), z2)

        res = [0] * (2 * n - 1)
        for i, v in enumerate(z0): res[i] += v
        for i, v in enumerate(mid): res[i + m] += v
        for i, v in enumerate(z2): res[i + 2*m] += v

        return res[:la + lb - 1]

    if rank == 1 and size > 1:
        x, y = comm.recv(source=0, tag=1)
        comm.send(karatsuba_seq(x, y), dest=0, tag=10)
    if rank == 2 and size > 2:
        x, y = comm.recv(source=0, tag=2)
        comm.send(karatsuba_seq(x, y), dest=0, tag=20)
    return None

#big numbers
def to_digits(s):
    return [int(ch) for ch in s.strip()[::-1]]

def from_digits(d):
    carry = 0
    for i in range(len(d)):
        t = d[i] + carry
        d[i] = t % 10
        carry = t // 10
    while carry:
        d.append(carry % 10)
        carry //= 10
    while len(d) > 1 and d[-1] == 0:
        d.pop()
    return ''.join(str(x) for x in d[::-1])

def big_naive_mpi(x, y):
    a = to_digits(x) if rank == 0 else None
    b = to_digits(y) if rank == 0 else None
    a = comm.bcast(a, root=0)
    b = comm.bcast(b, root=0)
    c = naive_mpi(a, b)
    return from_digits(c) if rank == 0 else None

def big_karatsuba_mpi(x, y):
    a = to_digits(x) if rank == 0 else None
    b = to_digits(y) if rank == 0 else None
    a = comm.bcast(a, root=0)
    b = comm.bcast(b, root=0)
    c = karatsuba_mpi_top(a, b)
    return from_digits(c) if rank == 0 else None

if __name__ == "__main__":
    N = 1000

    if rank == 0:
        a = [random.randint(0, 9) for _ in range(N)]
        b = [random.randint(0, 9) for _ in range(N)]
    else:
        a = b = None

    a = comm.bcast(a, root=0)
    b = comm.bcast(b, root=0)

    if rank == 0:
        t = time.time(); cpu_na = naive(a, b); print("CPU naive:", time.time() - t)
        t = time.time(); cpu_ka = karatsuba_seq(a, b);print("CPU kara :", time.time() - t, "ok:", cpu_ka == cpu_na)

    comm.Barrier()
    t0 = MPI.Wtime()
    mpi_na = naive_mpi(a, b)
    comm.Barrier()
    time1 = MPI.Wtime() - t0
    if rank == 0:
        print("MPI naive:", time1, "ok:", mpi_na == cpu_na)

    comm.Barrier()
    t0 = MPI.Wtime()
    mpi_ka = karatsuba_mpi_top(a, b)
    comm.Barrier()
    time2 = MPI.Wtime() - t0
    if rank == 0:
        print("MPI kara :", time2, "ok:", mpi_ka == cpu_na)

    #big numbers
    if rank == 0:
        x = ''.join(str(random.randint(0, 9)) for _ in range(200))
        y = ''.join(str(random.randint(0, 9)) for _ in range(200))
    else:
        x = y = None
    x = comm.bcast(x, root=0)
    y = comm.bcast(y, root=0)

    comm.Barrier()
    t = time.time()
    r1 = big_naive_mpi(x, y)
    comm.Barrier()
    if rank == 0:
        print("BIG MPI naive:", time.time() - t)

    comm.Barrier()
    t = time.time()
    r2 = big_karatsuba_mpi(x, y)
    comm.Barrier()
    if rank == 0:
        print("BIG MPI kara :", time.time() - t, "same:", r1 == r2)
