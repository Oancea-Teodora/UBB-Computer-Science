
# mpirun -np 4 python main.py --n 2000 --steps 200 --dt 0.001

import argparse
import numpy as np

def block_counts_displs(n, size):
    counts = np.array([((r+1)*n // size) - (r*n // size) for r in range(size)], dtype=np.int32)
    start = np.array([(r*n // size) for r in range(size)], dtype=np.int32)
    return counts, start

def main():
    from mpi4py import MPI
    comm = MPI.COMM_WORLD
    rank = comm.Get_rank()
    size = comm.Get_size()

    ap = argparse.ArgumentParser()
    ap.add_argument("--n", type=int, default=2000)
    ap.add_argument("--steps", type=int, default=200)
    ap.add_argument("--dt", type=float, default=1e-3)
    ap.add_argument("--G", type=float, default=1.0)
    ap.add_argument("--softening", type=float, default=1e-3)
    ap.add_argument("--seed", type=int, default=123)
    args = ap.parse_args()

    n = args.n
    counts, displs = block_counts_displs(n, size)
    local_n = int(counts[rank])
    start = int(displs[rank])
    idx_global = start + np.arange(local_n)

    if rank == 0:
        rng = np.random.default_rng(args.seed)
        pos_all = rng.uniform(-1, 1, size=(n, 3)).astype(np.float64)
        vel_all = rng.uniform(-0.1, 0.1, size=(n, 3)).astype(np.float64)
        m_all = rng.uniform(0.5, 2.0, size=(n,)).astype(np.float64)
    else:
        pos_all = vel_all = m_all = None

    pos_local = np.empty((local_n, 3), dtype=np.float64)
    vel_local = np.empty((local_n, 3), dtype=np.float64)
    m_local = np.empty((local_n,), dtype=np.float64)

    c3 = (counts * 3).astype(np.int32)
    d3 = (displs * 3).astype(np.int32)

    comm.Scatterv([pos_all.reshape(-1) if rank == 0 else None, c3, d3, MPI.DOUBLE], pos_local.reshape(-1), root=0)
    comm.Scatterv([vel_all.reshape(-1) if rank == 0 else None, c3, d3, MPI.DOUBLE], vel_local.reshape(-1), root=0)
    comm.Scatterv([m_all if rank == 0 else None, counts, displs, MPI.DOUBLE], m_local, root=0)

    all_m = np.empty(n, dtype=np.float64)
    comm.Allgatherv(m_local, [all_m, counts, displs, MPI.DOUBLE])

    all_pos = np.empty((n, 3), dtype=np.float64)

    comm.Barrier()
    t0 = MPI.Wtime()

    for _ in range(args.steps):
        comm.Allgatherv(pos_local.reshape(-1), [all_pos.reshape(-1), c3, d3, MPI.DOUBLE])

        diff = all_pos[None, :, :] - pos_local[:, None, :]
        r2 = (diff * diff).sum(axis=2) + args.softening * args.softening
        invr3 = 1.0 / (np.sqrt(r2) ** 3)
        invr3[np.arange(local_n), idx_global] = 0.0
        acc = args.G * (diff * (all_m[None, :, None] * invr3[:, :, None])).sum(axis=1)

        vel_local += acc * args.dt
        pos_local += vel_local * args.dt

    comm.Barrier()
    t1 = MPI.Wtime()
    max_time = comm.reduce(t1 - t0, op=MPI.MAX, root=0)

    if rank == 0:
        pos_final = np.empty((n, 3), dtype=np.float64)
        vel_final = np.empty((n, 3), dtype=np.float64)
    else:
        pos_final = vel_final = None

    comm.Gatherv(pos_local.reshape(-1), [pos_final.reshape(-1) if rank == 0 else None, c3, d3, MPI.DOUBLE], root=0)
    comm.Gatherv(vel_local.reshape(-1), [vel_final.reshape(-1) if rank == 0 else None, c3, d3, MPI.DOUBLE], root=0)

    if rank == 0:
        print(f"Done. n={n} steps={args.steps} dt={args.dt} ranks={size} time(max)={max_time:.3f}s")
        for i in range(min(n, 5)):
            print(f"Body {i}: pos={pos_final[i]} vel={vel_final[i]} m={all_m[i]:.3f}")

if __name__ == "__main__":
    main()
