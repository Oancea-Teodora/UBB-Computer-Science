import mpi.MPI;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Main {

    private static List<Integer> solution_to_color(int number, int n, int k){
        List<Integer> solution = new ArrayList<>();
        for(int i=0;i<n;i++){
            solution.add(number % k);
            number /= k;
        }
        return solution;
    }

    private static boolean checkSolution(){
        Random rd = new Random();
        return rd.nextInt(5) == 0;
    }

    private static void master(int n, int k) {
        int workerCount = MPI.COMM_WORLD.Size() - 1;
        int maxValue = 1;

        for (int i = 0; i < n; i++) {
            maxValue *= k;
        }

        // version 1: send start offset, end and value
        for(int i=1;i<workerCount;i++){
            int[] start = new int[1];
            start[0] = i;
            //             MPI.COMM_WORLD.Send(new Object[]{array}, 0, 1, MPI.OBJECT, parent, 0);
            MPI.COMM_WORLD.Send(new int[]{i}, 0, 1, MPI.INT, i, 1);
            MPI.COMM_WORLD.Send(new int[]{maxValue}, 0, 1, MPI.INT, i, 2);
            MPI.COMM_WORLD.Send(new int[]{workerCount}, 0, 1, MPI.INT, i, 3);
        }

        for(int i=1;i<workerCount;i++) {
            Object[] receivedData = new Object[1];
            MPI.COMM_WORLD.Recv(receivedData, 0, 1, MPI.OBJECT, i, 4); // receive the sorted lower half from the child process
            System.out.printf("Solution from %d -> %s\n", i, receivedData[0].toString());
        }

    }

    private static void worker(int n, int k) {
        int[] start = new int[1];
        int[] end = new int[1];
        int[] step = new int[1];
        //         MPI.COMM_WORLD.Recv(matrixSize,0,1,MPI.INT,0,1);
        MPI.COMM_WORLD.Recv(start, 0, 1, MPI.INT, 0, 1);
        System.out.printf("Worker %d received start %d \n", MPI.COMM_WORLD.Rank(), start[0]);
        MPI.COMM_WORLD.Recv(end, 0, 1, MPI.INT, 0, 2);
        System.out.printf("Worker %d received end %d \n", MPI.COMM_WORLD.Rank(), end[0]);
        MPI.COMM_WORLD.Recv(step, 0, 1, MPI.INT, 0, 3);
        System.out.printf("Worker %d received step %d \n", MPI.COMM_WORLD.Rank(), step[0]);

        // computing the solution here

        for(int number = start[0]; number < end[0]; number+=step[0]){
            List<Integer> sol = solution_to_color(number, n, k);
            if(checkSolution()){
                MPI.COMM_WORLD.Send(new Object[]{sol}, 0, 1, MPI.OBJECT, 0, 4);
                return;
            }
        }

    }

    public static void main(String[] args) {

        MPI.Init(args);

        int me = MPI.COMM_WORLD.Rank();
        int n = 5;
        int k = 3;


        if (me == 0) {
            master(n, k);
        } else {
            worker(n, k);
        }

        MPI.Finalize();

    }
}
