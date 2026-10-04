package ro.alexpopa.mpi;

import mpi.MPI;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MpiPrimes {

    private static final int DEFAULT_TAG = 0;

    public static List<Integer> primesInInterval(int id, int nrProcs, int maxN, List<Integer> primesToSqrt) {
        List<Integer> result = new ArrayList<>();
        int begin = id * (maxN - primesToSqrt.get(primesToSqrt.size() - 1) + 1) / nrProcs;
        int end = (id + 1) * (maxN - primesToSqrt.get(primesToSqrt.size() - 1) + 1) / nrProcs;
        for (int i = begin; i < end; i++) {
            int k = 0;
            while (k < primesToSqrt.size() && i % primesToSqrt.get(k) != 0) {
                k++;
            }
            if (k == primesToSqrt.size() && i != 1) {
                result.add(i);
            }
        }
        return result;
    }

    private static void master(int n, int nrProcs, List<Integer> primesToSqrt) {
        int[] metadata = new int[2];
        metadata[0] = n;
        metadata[1] = primesToSqrt.size();
        for (int i = 1; i < nrProcs; i++) {
            MPI.COMM_WORLD.Send(metadata, 0, 2, MPI.INT, i, DEFAULT_TAG);
            MPI.COMM_WORLD.Send(new Object[]{primesToSqrt}, 0, 1, MPI.OBJECT, i, DEFAULT_TAG);
        }

        List<Integer> result = primesInInterval(0, nrProcs, n, primesToSqrt);
        for (int i = 1; i < nrProcs; i++) {
            Object[] obj = new Object[1];
            MPI.COMM_WORLD.Recv(obj, 0, 1, MPI.OBJECT, i, DEFAULT_TAG);
            result.addAll((List<Integer>) obj[0]);
        }
        System.out.println(result);
    }

    private static void worker(int id, int nrProcs) {
        int[] metadata = new int[2];
        MPI.COMM_WORLD.Recv(metadata, 0, 2, MPI.INT, 0, DEFAULT_TAG);
        int maxN = metadata[0];
        int size = metadata[1];
        System.out.println(maxN);
        System.out.println(size);
        Object[] primesToSqrt = new Object[1];
        MPI.COMM_WORLD.Recv(primesToSqrt, 0, 1, MPI.OBJECT, 0, DEFAULT_TAG);
        System.out.println(primesToSqrt[0].toString());
        List<Integer> result = primesInInterval(id, nrProcs, maxN, (List<Integer>) primesToSqrt[0]);
        MPI.COMM_WORLD.Send(new Object[]{result}, 0, 1, MPI.OBJECT, 0, DEFAULT_TAG);
    }

    public static void main(String[] args) {
        MPI.Init(args);
        int me = MPI.COMM_WORLD.Rank();
        int nrProcs = MPI.COMM_WORLD.Size();
        int n = 100;
        List<Integer> primesToSqrtN = Arrays.asList(2, 3, 5, 7);
        if (me == 0) {
            master(n, nrProcs, primesToSqrtN);
        } else {
            worker(me, nrProcs);
        }
        MPI.Finalize();
    }
}
