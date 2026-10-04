import mpi.MPI;
import mpi.Status;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Quicksort {

    private static void killAll(int processCount){
        for(int i=1;i<processCount;i++){
            // tells a child process when to stop by sending it 0
            MPI.COMM_WORLD.Send(new int[]{0}, 0, 1, MPI.INT, i, 2);
        }
    }

    private static int partition(List<Integer> array, int start, int end){
        int pivot = array.get(end);
        int i = start - 1;

        for(int j = start; j < end; j++){
            if(array.get(j) < pivot){
                i++;
                // swap
                int tmp = array.get(i);
                array.set(i, array.get(j));
                array.set(j, tmp);
            }
        }

        int tmp = array.get(i+1);
        array.set(i+1, array.get(end));
        array.set(end, tmp);

        return i+1;
    }

    private static void quicksortUtil(List<Integer> array, int start, int end, int me, int processCount){

        if (start >= end)
            return;

        List<Integer> result1, result2;

        int p = partition(array, start, end); // get the pivot
        int child = me + processCount/2;

        if(processCount >= 2 && child < processCount){ // if the current process has children left

            List<Integer> toSend = new ArrayList<>(array);

            MPI.COMM_WORLD.Send(new int[]{1}, 0, 1, MPI.INT, child, 2); // still alive
            MPI.COMM_WORLD.Send(new Object[]{toSend, start, p-1}, 0, 3, MPI.OBJECT, child, 0); // child process has to sort the smaller half obtained

            result2 = new ArrayList<>(array);
            quicksortUtil(result2, p+1, end, me, processCount/2); // current process sorts the upper half

            Object[] receivedData = new Object[1];
            MPI.COMM_WORLD.Recv(receivedData, 0, 1, MPI.OBJECT, child, 0); // receive the sorted lower half from the child process
            result1 = (List<Integer>) receivedData[0];

        } else{
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

    private static void master(List<Integer> array, int processCount){
        quicksortUtil(array, 0, array.size()-1, 0, processCount);
        System.out.println(array.toString());
        killAll(processCount);
    }

    private static void worker(int me, int processCount){

        while(true){
            int[] alive = new int[1];
            MPI.COMM_WORLD.Recv(alive, 0, 1, MPI.INT, MPI.ANY_SOURCE, 2);

            // it stops upon receving 0
            if(alive[0] == 0){
                break;
            }

            Object[] receivedData = new Object[3]; // receives the array to sort, where it starts and where it ends
            Status status = MPI.COMM_WORLD.Recv(receivedData,0,3, MPI.OBJECT, MPI.ANY_SOURCE, 0);
            int parent = status.source;
            List<Integer> array = (List<Integer>) receivedData[0];
            int start = (int) receivedData[1];
            int end = (int) receivedData[2];

            quicksortUtil(array, start, end, me, processCount);

            // sends the sorted array to the parent
            MPI.COMM_WORLD.Send(new Object[]{array}, 0, 1, MPI.OBJECT, parent, 0);

        }

    }


    public static void main(String[] args) {
        MPI.Init(args);
        int selfRank = MPI.COMM_WORLD.Rank();
        int processCount = MPI.COMM_WORLD.Size();

        List<Integer> array = Arrays.asList(9, 8, 7, 1, 1, 1, 6, 5, 4, 3, 2, 1);

        if(selfRank == 0){
            master(array, processCount);
        }
        else{
            worker(selfRank, processCount);
        }

        MPI.Finalize();
    }
}
