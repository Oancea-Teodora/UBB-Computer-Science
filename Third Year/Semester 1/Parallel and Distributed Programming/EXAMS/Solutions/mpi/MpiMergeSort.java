package ro.alexpopa.mpi;

import mpi.MPI;
import mpi.Status;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MpiMergeSort {
    private static void killAll(int numberOfProcess) {
        for (int i = 1; i < numberOfProcess; ++i) {
            MPI.COMM_WORLD.Send(new int[]{0}, 0, 1, MPI.INT, i, 2);
        }
    }

    private static void master(List<Integer> array, int numberOfProcesses) {
        List<Integer> solution = mergeSortUtil(array, 0, numberOfProcesses);
        System.out.println("--------------");
        solution.forEach(s -> System.out.print(s + " "));
        killAll(numberOfProcesses);
    }

    private static List<Integer> mergeSortUtil(List<Integer> array, int me, int numberOfProcesses) {
        if (array.size() <= 1) return array;
        List<Integer> result1, result2;
        int child = me + numberOfProcesses / 2;
        if (numberOfProcesses >= 2 && child < numberOfProcesses) {
            List<Integer> toSend = new ArrayList<>(array.subList(0, array.size() / 2));
            MPI.COMM_WORLD.Send(new int[]{1}, 0, 1, MPI.INT, child, 2);
            MPI.COMM_WORLD.Send(new Object[]{toSend}, 0, 1, MPI.OBJECT, child, 0);
            result2 = mergeSortUtil(array.subList(array.size() / 2, array.size()), me, numberOfProcesses / 2);
            Object[] receivedData = new Object[1];
            MPI.COMM_WORLD.Recv(receivedData, 0, 1, MPI.OBJECT, child, 0);
            result1 = (List<Integer>) receivedData[0];
        } else {
            result1 = mergeSortUtil(array.subList(0, array.size() / 2), me, 1);
            result2 = mergeSortUtil(array.subList(array.size() / 2, array.size()), me, 1);
        }
        return merge(result1, result2);
    }

    private static List<Integer> merge(List<Integer> list1, List<Integer> list2) {
        List<Integer> result = new ArrayList<>();
        int i = 0, j = 0;
        while (i < list1.size() && j < list2.size()) {
            if (list1.get(i) < list2.get(j)) {
                result.add(list1.get(i));
                i++;
            } else {
                result.add(list2.get(j));
                j++;
            }
        }
        result.addAll(list1.subList(i, list1.size()));
        result.addAll(list2.subList(j, list2.size()));
        return result;
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
            array = mergeSortUtil(array, me, numberOfProcesses);

            MPI.COMM_WORLD.Send(new Object[]{array}, 0, 1, MPI.OBJECT, parent, 0);
        }
    }

    public static void main(String[] args) {
        MPI.Init(args);
        int selfRank = MPI.COMM_WORLD.Rank();
        int numberOfProcesses = MPI.COMM_WORLD.Size();
        List<Integer> array = Arrays.asList(7, 2, 5, 6, 9, 218, 1, 2, 3, 4, 2, 5, 8, 12, 8, 2, 3, 8);
        if (selfRank == 0) {
            master(array, numberOfProcesses);
        } else {
            worker(selfRank, numberOfProcesses);
        }
        MPI.Finalize();
    }

}
