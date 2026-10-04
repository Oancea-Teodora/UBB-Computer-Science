import mpi.MPI;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.sqrt;

public class Main {

    public static boolean isPrime(int x) {
        if (x < 2) {
            return false;
        }
        if (x == 2) {
            return true;
        }
        if (x % 2 == 0) {
            return false;
        }
        for (int j = 3; j * j <= x; j += 2) {
            if (x % j == 0) {
                return false;
            }
        }
        return true;
    }

    public static boolean checkPrime(int n, List<Integer> checkNumbers) {
        for (Integer number : checkNumbers) {
            if (n % number == 0)
                return false;
        }
        return true;
    }

    public static List<Integer> primesSqrtN(int N) {
        List<Integer> primes = new ArrayList<>();
        for (int i = 2; i <= sqrt(N); i++) {
            if (isPrime(i)) {
                primes.add(i);
            }
        }
        return primes;
    }

    public static void master(int n, int processCount) {
        List<Integer> primesN = primesSqrtN(n);
        int start = (int) Math.sqrt(n) + 1;
        int stop;
        int size = n - start + 1;
        int step = size / (processCount - 1);
        int remainder = size % (processCount - 1);


        for (int i = 1; i < processCount; i++) {
            stop = start + step;
            if (remainder > 0) {
                stop++;
                remainder--;
            }

            if (stop > n)
                stop = n;

            // send to each process the list, the start index, the end index and n
            MPI.COMM_WORLD.Send(new int[]{start, stop, n}, 0, 3, MPI.INT, i, 1);
            MPI.COMM_WORLD.Send(new Object[]{primesN}, 0, 1, MPI.OBJECT, i, 2);

            start = stop;
        }

        for(int i=1;i<processCount;i++){

            Object[] processList = new Object[1];
            MPI.COMM_WORLD.Recv(processList,0,1,MPI.OBJECT, i, 3);
            primesN.addAll((List<Integer>)processList[0]);
        }

        System.out.printf("Final prime array: %s\n", primesN.toString());

    }

    public static void worker() {
        // receive the information from master
        int[] receivedData = new int[3];
        Object[] receivedArray = new Object[1];

        MPI.COMM_WORLD.Recv(receivedData,0,3,MPI.INT,0,1);
        MPI.COMM_WORLD.Recv(receivedArray,0,1,MPI.OBJECT,0,2);

        System.out.printf("Process %d received: start -- %d, end -- %d, array -- %s\n", MPI.COMM_WORLD.Rank(), receivedData[0], receivedData[1],receivedArray[0]);

        List<Integer> partialSolution = new ArrayList<>();
        for(int i = receivedData[0]; i < receivedData[1];i++){
            if(checkPrime(i, (List<Integer>) receivedArray[0])){
                partialSolution.add(i);
            }
        }

        System.out.printf("Process' %d partial solution: %s\n", MPI.COMM_WORLD.Rank(), partialSolution.toString());

        // send it to the master
        MPI.COMM_WORLD.Send(new Object[]{partialSolution}, 0, 1, MPI.OBJECT, 0, 3);
    }

    public static void main(String[] args) {
        MPI.Init(args);

        int me = MPI.COMM_WORLD.Rank();
        int processCount = MPI.COMM_WORLD.Size();

        int n = 25;

        if (me == 0) {
            master(n, processCount);
        } else {
            worker();
        }

        MPI.Finalize();
    }
}
