
# python main.py --n 2000 --steps 200 --dt 0.001 --threads 8

import time
import argparse
import numpy as np
from concurrent.futures import ThreadPoolExecutor

def accel_chunk(pos_all, m_all, idx, G, eps):
    pos = pos_all[idx]
    diff = pos_all[None, :, :] - pos[:, None, :]
    r2 = (diff * diff).sum(axis=2) + eps * eps
    invr3 = 1.0 / (np.sqrt(r2) ** 3)
    invr3[np.arange(len(idx)), idx] = 0.0
    return G * (diff * (m_all[None, :, None] * invr3[:, :, None])).sum(axis=1)

def main():
    ap = argparse.ArgumentParser()
    ap.add_argument("--n", type=int, default=2000)
    ap.add_argument("--steps", type=int, default=200)
    ap.add_argument("--dt", type=float, default=1e-3)
    ap.add_argument("--threads", type=int, default=8)
    ap.add_argument("--G", type=float, default=1.0)
    ap.add_argument("--softening", type=float, default=1e-3)
    ap.add_argument("--seed", type=int, default=123)
    args = ap.parse_args()

    rng = np.random.default_rng(args.seed)
    pos = rng.uniform(-1, 1, size=(args.n, 3))
    vel = rng.uniform(-0.1, 0.1, size=(args.n, 3))
    m = rng.uniform(0.5, 2.0, size=(args.n,))

    chunks = np.array_split(np.arange(args.n), args.threads)

    t0 = time.perf_counter()
    with ThreadPoolExecutor(max_workers=args.threads) as ex:
        for _ in range(args.steps):
            futures = [ex.submit(accel_chunk, pos, m, ch, args.G, args.softening) for ch in chunks]
            acc = np.empty_like(pos)
            for ch, futur in zip(chunks, futures):
                acc[ch] = futur.result()

            vel += acc * args.dt
            pos += vel * args.dt
    t1 = time.perf_counter()

    print(f"Done. n={args.n} steps={args.steps} dt={args.dt} threads={args.threads} time={t1-t0:.3f}s")
    for i in range(min(args.n, 5)):
        print(f"Body {i}: pos={pos[i]} vel={vel[i]} m={m[i]:.3f}")

if __name__ == "__main__":
    main()
