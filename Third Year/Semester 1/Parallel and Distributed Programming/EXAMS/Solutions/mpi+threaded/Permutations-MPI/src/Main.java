import mpi.MPI;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Main {

    private static boolean checkSolution() {
        Random rd = new Random();
        return rd.nextInt(5) == 0;
    }

    public static void bktr(List<Integer> solution, int n, List<List<Integer>> solutions) {
        if (solution.size() == n && checkSolution()) {
            solutions.add(new ArrayList<>(solution));
            return;
        }

        for (int i = 0; i < n; i++) {
            if (solution.contains(i)) continue;
            solution.add(i);
            bktr(solution, n, solutions);
            solution.remove(solution.size() - 1);
        }
    }

    private static void master(int n, int processCount) {
        int childProcessCount = processCount - 1;
        int step = n / childProcessCount; // minimum no of permutations a child process has to generate
        int remainder = n % childProcessCount;

        int start = 0, stop = 0;
        List<List<Integer>> solutions = new ArrayList<>();

        for (int i = 1; i <= childProcessCount; i++) {
            stop = start + step;

            if (remainder > 0) {
                stop++;
                remainder--;
            }

            int[] childData = new int[]{start, stop, n};
            MPI.COMM_WORLD.Send(childData, 0, 3, MPI.INT, i, 0);
            start = stop;
        }

        // receive the array sizes from each

        int finalCount = 0;

        for (int i = 1; i <= childProcessCount; i++) {
            int[] size = new int[1];
            MPI.COMM_WORLD.Recv(size, 0, 1, MPI.INT, i, 1);
            finalCount += size[0];
            System.out.printf("Process 0: Received %d valid permutations from process %d\n", size[0], i);
        }
    }

    private static void worker() {
        int[] receivedData = new int[3];
        MPI.COMM_WORLD.Recv(receivedData, 0, 3, MPI.INT, 0, 0);

        List<List<Integer>> childSolutions = new ArrayList<>(receivedData[2]); // capacity of the array list is n
        System.out.printf("Process %d: start - %d *** stop - %d\n", MPI.COMM_WORLD.Rank(), receivedData[0], receivedData[1]);
        for (int i = receivedData[0]; i < receivedData[1]; i++) {
            List<Integer> solution = new ArrayList<>();
            solution.add(i);
            bktr(solution, receivedData[2], childSolutions);
        }
        System.out.printf("Process %d generated the following solution:\n%s\n", MPI.COMM_WORLD.Rank(), childSolutions.toString());

        // send the array size back to master
        MPI.COMM_WORLD.Send(new int[]{childSolutions.size()}, 0, 1, MPI.INT, 0, 1);
    }

    public static void main(String[] args) {
        MPI.Init(args);
        int me = MPI.COMM_WORLD.Rank();
        int processCount = MPI.COMM_WORLD.Size();

        int n = 3;

        if (me == 0) {
            master(n, processCount);
        } else {
            worker();
        }

        MPI.Finalize();
    }
}
