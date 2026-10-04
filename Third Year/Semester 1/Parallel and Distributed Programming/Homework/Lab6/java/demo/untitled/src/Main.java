import java.util.*;
import java.util.concurrent.*;

public class Main {

    static int n;
    static boolean[][] g;
    static int start = 0;
    static volatile boolean found;
    static int[] result;

    static void buildGraph() {
        n = 5;
        g = new boolean[n][n];

        addEdge(0, 1);
        addEdge(1, 2);
        addEdge(2, 3);
        addEdge(3, 4);
        addEdge(4, 0);

        addEdge(0, 2);
        addEdge(1, 3);
        addEdge(2, 4);
        addEdge(3, 0);
        addEdge(4, 1);
    }

    static void addEdge(int u, int v) {
        g[u][v] = true;
    }

    static synchronized void saveResult(int[] path) {
        if (!found) {
            found = true;
            result = path.clone();
        }
    }
    static void printResult(String name) {
        System.out.println(name);
        if (!found || result == null) {
            System.out.println("No Hamiltonian cycle.");
            return;
        }
        System.out.print("Hamiltonian cycle: ");
        for (int i = 0; i < n; i++) {
            System.out.print(result[i] + " ");
        }
        System.out.println(result[0]);
        System.out.println();
    }

    static void dfsThreads(int node, int depth, boolean[] used, int[] path, int threads) {
        if (found) return;

        if (depth == n) {
            if (g[node][start]) {
                saveResult(path);
            }
            return;
        }

        List<Integer> neigh = new ArrayList<>();
        for (int v = 0; v < n; v++) {
            if (!used[v] && g[node][v]) {
                neigh.add(v);
            }
        }
        if (neigh.isEmpty()) return;

        if (threads <= 1) {
            for (int v : neigh) {
                if (found) return;
                boolean[] u2 = used.clone();
                int[] p2 = path.clone();
                u2[v] = true;
                p2[depth] = v;
                dfsThreads(v, depth + 1, u2, p2, 1);
            }
        } else {
            int k = neigh.size();
            int base = threads / k;
            int extra = threads % k;

            List<Thread> extraThreads = new ArrayList<>();

            for (int i = 0; i < k; i++) {
                int v = neigh.get(i);
                int childThreads = base + (i < extra ? 1 : 0);
                final int childThreadsFinal = childThreads;

                Runnable r = () -> {
                    boolean[] u2 = used.clone();
                    int[] p2 = path.clone();
                    u2[v] = true;
                    p2[depth] = v;
                    dfsThreads(v, depth + 1, u2, p2, childThreadsFinal);
                };

                if (i == 0) {
                    r.run();
                } else {
                    Thread t = new Thread(r);
                    extraThreads.add(t);
                    t.start();
                }
            }

            for (Thread t : extraThreads) {
                try {
                    t.join();
                } catch (InterruptedException e) {
                }
            }
        }
    }
    static void runThreadsVersion(int maxThreads) {
        found = false;
        result = null;
        boolean[] used = new boolean[n];
        int[] path = new int[n];
        used[start] = true;
        path[0] = start;

        dfsThreads(start, 1, used, path, maxThreads);
        printResult("Threads version");
    }

    static class HamiTask extends RecursiveTask<Boolean> {
        int node;
        int depth;
        boolean[] used;
        int[] path;

        HamiTask(int node, int depth, boolean[] used, int[] path) {
            this.node = node;
            this.depth = depth;
            this.used = used;
            this.path = path;
        }

        @Override
        protected Boolean compute() {
            if (found) return false;

            if (depth == n) {
                if (g[node][start]) {
                    saveResult(path);
                }
                return found;
            }

            List<Integer> neigh = new ArrayList<>();
            for (int v = 0; v < n; v++) {
                if (!used[v] && g[node][v]) {
                    neigh.add(v);
                }
            }
            if (neigh.isEmpty()) return false;

            if (neigh.size() == 1) {
                int v = neigh.get(0);
                boolean[] u2 = used.clone();
                int[] p2 = path.clone();
                u2[v] = true;
                p2[depth] = v;
                return new HamiTask(v, depth + 1, u2, p2).compute();
            }

            List<HamiTask> tasks = new ArrayList<>();
            for (int i = 1; i < neigh.size(); i++) {
                int v = neigh.get(i);
                boolean[] u2 = used.clone();
                int[] p2 = path.clone();
                u2[v] = true;
                p2[depth] = v;
                HamiTask t = new HamiTask(v, depth + 1, u2, p2);
                tasks.add(t);
                t.fork();
            }

            int v0 = neigh.get(0);
            boolean[] u2 = used.clone();
            int[] p2 = path.clone();
            u2[v0] = true;
            p2[depth] = v0;
            if (new HamiTask(v0, depth + 1, u2, p2).compute()) {
                return true;
            }

            for (HamiTask t : tasks) {
                if (t.join()) return true;
            }
            return found;
        }
    }

    static void runForkJoinVersion(int maxThreads) {
        found = false;
        result = null;
        boolean[] used = new boolean[n];
        int[] path = new int[n];
        used[start] = true;
        path[0] = start;

        ForkJoinPool pool = new ForkJoinPool(maxThreads);
        HamiTask root = new HamiTask(start, 1, used, path);
        pool.invoke(root);
        pool.shutdown();

        printResult("ForkJoin version");
    }

    public static void main(String[] args) {
        buildGraph();
        int maxThreads = 8;

        long t1 = System.nanoTime();
        runThreadsVersion(maxThreads);
        long t2 = System.nanoTime();
        System.out.println("Threads time: " + (t2 - t1) / 1_000_000.0 + " ms\n");

        long t3 = System.nanoTime();
        runForkJoinVersion(maxThreads);
        long t4 = System.nanoTime();
        System.out.println("ForkJoin time: " + (t4 - t3) / 1_000_000.0 + " ms");
    }
}
