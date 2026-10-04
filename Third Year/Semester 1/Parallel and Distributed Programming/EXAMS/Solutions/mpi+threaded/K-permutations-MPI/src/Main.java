import mpi.MPI;

import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void bktr(List<Integer> solution ,int n, int k, List<List<Integer>> solutions){
        if(solution.size() == k){
            solutions.add(new ArrayList<>(solution));
            return;
        }

        // for combinations
//        int last = 0;
//        if(solution.size()>0)
//            last = solution.get(solution.size()-1);
//
//        for (int i = last+1; i <= n ; i++) {
//            if(solution.contains(i)) continue;
//            solution.add(i);
//            bktr(solution, n, k, solutions);
//            solution.remove(solution.size()-1);
//
//        }

        for(int i=0;i<n;i++){
            if(solution.contains(i)) continue;
            solution.add(i);
            bktr(solution, n, k, solutions);
            solution.remove(solution.size()-1);
        }
    }


    public static void master(int n, int k, int processCount){
        int finalCount = 0;
        int[] childSize = new int[1];
        int childProcessCount = processCount - 1;
        int step = n / childProcessCount;
        int remainder = n % childProcessCount;

        int start = 0, stop = 0;

        for(int i=1;i<=childProcessCount;i++){
            stop = start + step;

            if(remainder > 0){
                stop++;
                remainder--;
            }

            int[] childData = new int[]{start, stop,n, k};
            MPI.COMM_WORLD.Send(childData,0,4,MPI.INT, i, 1);
            start = stop;

            // receive the size of the solution
            MPI.COMM_WORLD.Recv(childSize,0,1,MPI.INT,i,2);
            System.out.printf("Received size %d from process %d\n", childSize[0], i);
            finalCount += childSize[0];
        }

        System.out.printf("Final size: %d\n", finalCount);
    }

    public static void worker(){
        int[] receivedData = new int[4];
        MPI.COMM_WORLD.Recv(receivedData, 0, 4, MPI.INT, 0, 1);

        List<List<Integer>> childSolutions = new ArrayList<>(receivedData[2]);
        System.out.printf("Process %d: start - %d *** stop - %d\n", MPI.COMM_WORLD.Rank(), receivedData[0], receivedData[1]);

        for(int i= receivedData[0]; i < receivedData[1]; i++){
            List<Integer> solution = new ArrayList<>();
            solution.add(i);
            bktr(solution, receivedData[2], receivedData[3], childSolutions);
        }
        System.out.printf("Process %d generated the following solution:\n%s\n", MPI.COMM_WORLD.Rank(), childSolutions.toString());

        // send the size of the solution array to master
        MPI.COMM_WORLD.Send(new int[]{childSolutions.size()}, 0,1,MPI.INT, 0,2);

    }

    public static void main(String[] args) {
        MPI.Init(args);

        int me = MPI.COMM_WORLD.Rank();
        int processCount = MPI.COMM_WORLD.Size();

        int n = 5;
        int k = 3;

        if(me == 0){
            master(n, k, processCount);
        }
        else{
            worker();
        }

        MPI.Finalize();
    }
}
