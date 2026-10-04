package ro.alexpopa.mpi;

import mpi.MPI;
import mpi.Status;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MpiQuickSort {

    private static void killAll(int numberOfProcess) {
        for (int i = 1; i < numberOfProcess; ++i) {
            MPI.COMM_WORLD.Send(new int[]{0}, 0, 1, MPI.INT, i, 2);
        }
    }

    private static void master(List<Integer> array, int numberOfProcesses) {
        quicksortUtil(array, 0, array.size() - 1, 0, numberOfProcesses);
        System.out.println("--------------");
//        array.forEach(s -> System.out.print(s + " "));
        System.out.println(array.toString());
        killAll(numberOfProcesses);
    }

    private static void quicksortUtil(List<Integer> array, int start, int end, int me, int numberOfProcesses) {
//        System.out.println("received to sort util: " + array.toString());
        if (start >= end) return;
        List<Integer> result1, result2;
        int p = partition(array, start, end);
        int child = me + numberOfProcesses / 2;
        if (numberOfProcesses >= 2 && child < numberOfProcesses) {
            List<Integer> toSend = new ArrayList<>(array);
//            System.out.println("master = parent is: " + me + " child is " + child + " [] " + toSend.toString() + " start " + start + " end " + (p - 1));
            MPI.COMM_WORLD.Send(new int[]{1}, 0, 1, MPI.INT, child, 2);
            MPI.COMM_WORLD.Send(new Object[]{toSend, start, p - 1}, 0, 3, MPI.OBJECT, child, 0);

//            System.out.println("master = parent is " + me + " local sort " + "[]" + " start " + (p + 1) + " end " + end);
            result2 = new ArrayList<>(array);
            quicksortUtil(result2, p + 1, end, me, numberOfProcesses / 2);

            Object[] receivedData = new Object[1];
            MPI.COMM_WORLD.Recv(receivedData, 0, 1, MPI.OBJECT, child, 0);
            result1 = (List<Integer>) receivedData[0];
        } else {
//            System.out.println("offline [] " + array.toString());
            result1 = new ArrayList<>(array);
            result2 = new ArrayList<>(array);
            quicksortUtil(result1, start, p - 1, me, 1);
            quicksortUtil(result2, p + 1, end, me, 1);
        }
        for (int i = start; i <= p - 1; i++) {
            array.set(i, result1.get(i));
        }
        for (int i = p + 1; i < end; i++) {
            array.set(i, result2.get(i));
        }
    }

    private static int partition(List<Integer> array, int start, int end) {
        int pivot = array.get(end);
        int i = start - 1;
        for (int j = start; j < end; j++) {
            if (array.get(j) < pivot) {
                i++;
                int temp = array.get(i);
                array.set(i, array.get(j));
                array.set(j, temp);
            }
        }

        int temp = array.get(i + 1);
        array.set(i + 1, array.get(end));
        array.set(end, temp);

        return i + 1;
    }

    private static void worker(int me, int numberOfProcesses) {
        while (true) {
            int[] alive = new int[1];
            MPI.COMM_WORLD.Recv(alive, 0, 1, MPI.INT, MPI.ANY_SOURCE, 2);
            if (alive[0] == 0) {
                break;
            }
            Object[] receivedData = new Object[3];
            Status status = MPI.COMM_WORLD.Recv(receivedData, 0, 3, MPI.OBJECT, MPI.ANY_SOURCE, 0);
            int parent = status.source;
            List<Integer> array = (List<Integer>) receivedData[0];
            int start = (int) receivedData[1];
            int end = (int) receivedData[2];
//            System.out.println("child = child is " + me + " parent is " + parent + " [] " + array.toString() + " start " + start + " end " + end);
            quicksortUtil(array, start, end, me, numberOfProcesses);

            MPI.COMM_WORLD.Send(new Object[]{array}, 0, 1, MPI.OBJECT, parent, 0);
        }
    }

    public static void main(String[] args) {
        MPI.Init(args);
        int selfRank = MPI.COMM_WORLD.Rank();
        int numberOfProcesses = MPI.COMM_WORLD.Size();
        List<Integer> array = Arrays.asList(9, 8, 7, 1, 1, 1, 6, 5, 4, 3, 2, 1);
        if (selfRank == 0) {
            master(array, numberOfProcesses);
        } else {
            worker(selfRank, numberOfProcesses);
        }
        MPI.Finalize();
    }
}
