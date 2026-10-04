package ro.alexpopa.mpi;

import mpi.MPI;
import mpi.Status;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

public class MpiArrangements {
    public static void main(String[] args) throws FileNotFoundException {
        new MpiArrangements().run(args);
    }

    private static void killAll(int numberOfProcess) {
        for (int i = 1; i < numberOfProcess; ++i) {
            MPI.COMM_WORLD.Send(new int[]{0}, 0, 1, MPI.INT, i, 2);
        }
    }

    private static void master(int n, int k, int numberOfProcesses) {
        List<Integer> solution = new ArrayList<>();
        int count = back(solution, n, k, 0, numberOfProcesses);
        System.out.println("Count = " + count);
        killAll(numberOfProcesses);
    }

    private static int back(List<Integer> solution, int n, int k, int me, int numberOfProcesses) {
        if (solution.size() == k) {
            System.out.println("solution: " + solution.toString());
            return 1;
        }
        int sum = 0;
        int child = me + numberOfProcesses / 2;
        if (numberOfProcesses >= 2 && child < numberOfProcesses) {
            List<Integer> toSend = new ArrayList<>(solution);
            MPI.COMM_WORLD.Send(new int[]{1}, 0, 1, MPI.INT, child, 2);
            MPI.COMM_WORLD.Send(new Object[]{toSend}, 0, 1, MPI.OBJECT, child, 0);
            List<Integer> temp = new ArrayList<>(solution);
            for (int i = 0; i < n; i += 2) {
                if (temp.contains(i)) continue;
                temp.add(i);
                sum += back(temp, n, k, me, numberOfProcesses / 2);
                temp.remove(temp.size() - 1);
            }
            Object[] receivedData = new Object[1];
            MPI.COMM_WORLD.Recv(receivedData, 0, 1, MPI.OBJECT, child, 1);
            sum += (int) receivedData[0];
            System.out.printf("Received new sum %d from %d to %d%n", (int) receivedData[0], child, me);
        } else {
            for (int i = 0; i < n; i++) {
                if (solution.contains(i)) continue;
                solution.add(i);
                sum += back(solution, n, k, me, 1);
                solution.remove(solution.size() - 1);
            }
        }
//        System.out.println(sum);
        return sum;
    }

    private static void worker(int n, int k, int me, int numberOfProcesses) {
        while (true) {
            int[] alive = new int[1];
            MPI.COMM_WORLD.Recv(alive, 0, 1, MPI.INT, MPI.ANY_SOURCE, 2);
            if (alive[0] == 0) {
                break;
            }
            Object[] receivedData = new Object[1];
            Status recv = MPI.COMM_WORLD.Recv(receivedData, 0, 1, MPI.OBJECT, MPI.ANY_SOURCE, 0);
            int parent = recv.source;
            List<Integer> array = (List<Integer>) receivedData[0];
            int sum = 0;
            for (int i = 1; i < n; i += 2) {
                if (array.contains(i) ) continue;
                array.add(i);
                sum += back(array, n, k, me, numberOfProcesses);
                array.remove(array.size() - 1);
            }
            MPI.COMM_WORLD.Send(new Object[]{sum}, 0, 1, MPI.OBJECT, parent, 1);
        }
    }

    public void run(String[] args) {
        MPI.Init(args);
        int selfRank = MPI.COMM_WORLD.Rank();
        int numberOfProcesses = MPI.COMM_WORLD.Size();
        int n = 4;
        int k = 3;
        if (selfRank == 0) {
            master(n, k, numberOfProcesses);
        } else {
            worker(n, k, selfRank, numberOfProcesses);
        }
        MPI.Finalize();
    }
}
