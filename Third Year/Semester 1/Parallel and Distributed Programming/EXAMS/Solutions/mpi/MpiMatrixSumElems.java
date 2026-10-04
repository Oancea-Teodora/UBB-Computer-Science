package ro.alexpopa.mpi;

import mpi.MPI;
import mpi.Status;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MpiMatrixSumElems {
    private static void killAll(int numberOfProcess) {
        for (int i = 1; i < numberOfProcess; ++i) {
            MPI.COMM_WORLD.Send(new int[]{0}, 0, 1, MPI.INT, i, 2);
        }
    }

    private static void master(List<List<Integer>> matrix, int numberOfProcesses) {
        List<Integer> flatten = new ArrayList<>();
        matrix.forEach(flatten::addAll);
        int sum = back(flatten, 0, numberOfProcesses);
        System.out.println("Sum = " + sum);
        killAll(numberOfProcesses);
    }

    private static int back(List<Integer> input, int me, int numberOfProcesses) {
        if (input.size() == 1) return input.get(0);
        int sum1, sum2;
        int child = me + numberOfProcesses / 2;
        if (numberOfProcesses >= 2 && child < numberOfProcesses) {
            List<Integer> toSend = new ArrayList<>(input.subList(0, input.size() / 2));
            MPI.COMM_WORLD.Send(new int[]{1}, 0, 1, MPI.INT, child, 2);
            MPI.COMM_WORLD.Send(new Object[]{toSend}, 0, 1, MPI.OBJECT, child, 0);
            List<Integer> temp = new ArrayList<>(input.subList(input.size() / 2, input.size()));
            sum1 = back(temp, me, numberOfProcesses / 2);
            Object[] receivedData = new Object[1];
            MPI.COMM_WORLD.Recv(receivedData, 0, 1, MPI.OBJECT, child, 0);
            sum2 = (int) receivedData[0];
        } else {
            sum1 = back(input.subList(0, input.size() / 2), me, 1);
            sum2 = back(input.subList(input.size() / 2, input.size()), me, 1);
        }
        return sum1 + sum2;
    }

    private static void worker(int me, int numberOfProcesses) {
        while (true) {
            int[] alive = new int[1];
            MPI.COMM_WORLD.Recv(alive, 0, 1, MPI.INT, MPI.ANY_SOURCE, 2);
            if (alive[0] == 0) {
                break;
            }
            Object[] receivedData = new Object[1];
            Status status = MPI.COMM_WORLD.Recv(receivedData, 0, 1, MPI.OBJECT, MPI.ANY_SOURCE, 0);
            int parent = status.source;
            List<Integer> array = (List<Integer>) receivedData[0];
            int sum = back(array, me, numberOfProcesses);
            MPI.COMM_WORLD.Send(new Object[]{sum}, 0, 1, MPI.OBJECT, parent, 0);
        }
    }

    public static void main(String[] args) {
        MPI.Init(args);
        int selfRank = MPI.COMM_WORLD.Rank();
        int numberOfProcesses = MPI.COMM_WORLD.Size();
        if (selfRank == 0) {
            master(Arrays.asList(Arrays.asList(1, 2), Arrays.asList(4, 5), Arrays.asList(7, 8)), numberOfProcesses);
        } else {
            worker(selfRank, numberOfProcesses);
        }
        MPI.Finalize();
    }
}
