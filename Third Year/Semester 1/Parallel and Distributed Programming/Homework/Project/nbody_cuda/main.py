
# python main.py --n 4096 --steps 200 --dt 0.001
import argparse, time
import numpy as np
import pyopencl as cl

KERNEL = r"""
__kernel void step(
    __global const float4* pos,
    __global const float4* vel,
    __global const float*  m,
    __global float4* pos2,
    __global float4* vel2,
    const int n,
    const float G,
    const float eps,
    const float dt
){
    int i = get_global_id(0);
    if (i >= n) return;

    float xi = pos[i].x, yi = pos[i].y, zi = pos[i].z;
    float vxi = vel[i].x, vyi = vel[i].y, vzi = vel[i].z;

    float ax = 0.0f, ay = 0.0f, az = 0.0f;
    float eps2 = eps * eps;

    for (int j = 0; j < n; j++){
        if (j == i) continue;

        float dx = pos[j].x - xi;
        float dy = pos[j].y - yi;
        float dz = pos[j].z - zi;

        float r2 = dx*dx + dy*dy + dz*dz + eps2;
        float invr = rsqrt(r2);
        float invr3 = invr * invr * invr;

        float s = G * m[j] * invr3;
        ax += dx * s;
        ay += dy * s;
        az += dz * s;
    }

    float vxo = vxi + ax * dt;
    float vyo = vyi + ay * dt;
    float vzo = vzi + az * dt;

    vel2[i] = (float4)(vxo, vyo, vzo, 0.0f);
    pos2[i] = (float4)(xi + vxo*dt, yi + vyo*dt, zi + vzo*dt, 0.0f);
}
"""

def pick_device():
    for plat in cl.get_platforms():
        gpus = plat.get_devices(device_type=cl.device_type.GPU)
        if gpus:
            return gpus[0]
    return cl.get_platforms()[0].get_devices()[0]

def main():
    ap = argparse.ArgumentParser()
    ap.add_argument("--n", type=int, default=4096)
    ap.add_argument("--steps", type=int, default=200)
    ap.add_argument("--dt", type=float, default=1e-3)
    ap.add_argument("--G", type=float, default=1.0)
    ap.add_argument("--softening", type=float, default=1e-3)
    ap.add_argument("--seed", type=int, default=123)
    args = ap.parse_args()

    dev = pick_device()
    ctx = cl.Context([dev])
    queue = cl.CommandQueue(ctx)

    print("OpenCL device:", dev.name)

    rng = np.random.default_rng(args.seed)
    pos = rng.uniform(-1, 1, size=(args.n, 4)).astype(np.float32)
    vel = rng.uniform(-0.1, 0.1, size=(args.n, 4)).astype(np.float32)
    pos[:, 3] = 0.0
    vel[:, 3] = 0.0
    m = rng.uniform(0.5, 2.0, size=(args.n,)).astype(np.float32)

    mf = cl.mem_flags
    pos_d = cl.Buffer(ctx, mf.READ_ONLY | mf.COPY_HOST_PTR, hostbuf=pos)
    vel_d = cl.Buffer(ctx, mf.READ_ONLY | mf.COPY_HOST_PTR, hostbuf=vel)
    m_d = cl.Buffer(ctx, mf.READ_ONLY | mf.COPY_HOST_PTR, hostbuf=m)
    pos2_d = cl.Buffer(ctx, mf.READ_WRITE, pos.nbytes)
    vel2_d = cl.Buffer(ctx, mf.READ_WRITE, vel.nbytes)

    prg = cl.Program(ctx, KERNEL).build()
    step = prg.step

    n = np.int32(args.n)
    G = np.float32(args.G)
    eps = np.float32(args.softening)
    dt = np.float32(args.dt)

    step(queue, (args.n,), None, pos_d, vel_d, m_d, pos2_d, vel2_d, n, G, eps, dt)
    queue.finish()

    t0 = time.perf_counter()
    for _ in range(args.steps):
        step(queue, (args.n,), None, pos_d, vel_d, m_d, pos2_d, vel2_d, n, G, eps, dt)
        pos_d, pos2_d = pos2_d, pos_d
        vel_d, vel2_d = vel2_d, vel_d
    queue.finish()
    t1 = time.perf_counter()

    pos_out = np.empty_like(pos)
    vel_out = np.empty_like(vel)
    cl.enqueue_copy(queue, pos_out, pos_d).wait()
    cl.enqueue_copy(queue, vel_out, vel_d).wait()

    print(f"Done (OpenCL). n={args.n} steps={args.steps} dt={args.dt} time={t1-t0:.3f}s")
    for i in range(min(args.n, 5)):
        print(f"Body {i}: pos={pos_out[i,:3]} vel={vel_out[i,:3]} m={m[i]:.3f}")

if __name__ == "__main__":
    main()
