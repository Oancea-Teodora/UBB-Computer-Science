package ro.alexpopa.mpi;

import mpi.MPI;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class MpiMatrixSum {

    public List<List<Integer>> initializeEmptyMatrix(int rows, int cols) {
        // build a matrix with zeros to be used in computations
        List<List<Integer>> matrix = new ArrayList<>();
        for (int i = 0; i < rows; i++) {
            List<Integer> line = new ArrayList<>();
            for (int j = 0; j < cols; j++) {
                line.add(0);
            }
            matrix.add(line);
        }
        return matrix;
    }

    public List<Integer> computeCoordinatesFromIndex(int index, int cols) {
        return Arrays.asList(index / cols, index % cols);
    }

    public List<List<Integer>> computeWorkForElements(int rows, int cols, List<List<Integer>> a, List<List<Integer>> b, List<Integer> elems) {
        // for each element, get its coordinates in the form (i,j) and then compute the value for those coordinates, i.e. add a[i][j]+b[i][j]
        List<List<Integer>> workedMatrix = initializeEmptyMatrix(rows, cols);
        for (Integer elem : elems) {
            List<Integer> coordinates = computeCoordinatesFromIndex(elem, cols);
            int i = coordinates.get(0);
            int j = coordinates.get(1);
            workedMatrix.get(i).set(j, a.get(i).get(j) + b.get(i).get(j));
        }
        return workedMatrix;
    }

    public List<Integer> getElementsById(int myId, int rows, int cols, int nrProcs) {
        // function which computes the necessary workload for each process with the id=myId
        int step = (rows * cols) / nrProcs;
        int rem = (rows * cols) % nrProcs;

        int extra = (rows * cols) - rem; // extra meaning elements which are unassigned at the moment to any process

        int begin = myId * step;
        int end = (myId + 1) * step;

        List<Integer> elems = new ArrayList<>();
        for (int i = begin; i < end; i++) {
            elems.add(i);
        }

        if (myId < rem) {   // add remaining elements from the modulo computation in order to ensure an equal amount to each process
            elems.add(extra + myId);
        }
        return elems;
    }

    public void mergeSolutionsSoFar(List<List<Integer>> solution, List<List<Integer>> partial) {
        // add the partial result to the solution, i.e. sum each element in solution with each one in partial
        int rows = solution.size(), cols = solution.get(0).size();
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                int prev = solution.get(i).get(j);
                solution.get(i).set(j, prev + partial.get(i).get(j));
            }
        }
    }

    public List<List<Integer>> master(List<List<Integer>> a, List<List<Integer>> b, int nrProcs) {
        int rows = a.size(), cols = b.get(0).size();
        List<List<Integer>> result = initializeEmptyMatrix(rows, cols);

        for (int i = 1; i < nrProcs; i++) { // send work to workers
            List<Integer> workerElems = getElementsById(i, rows, cols, nrProcs);
            MPI.COMM_WORLD.Send(new Object[]{workerElems}, 0, 1, MPI.OBJECT, i, 0);
            MPI.COMM_WORLD.Send(new Object[]{a}, 0, 1, MPI.OBJECT, i, 0);
            MPI.COMM_WORLD.Send(new Object[]{b}, 0, 1, MPI.OBJECT, i, 0);
        }

        // let master do his own work
        List<Integer> masterElements = getElementsById(0, rows, cols, nrProcs);
        List<List<Integer>> masterWork = computeWorkForElements(rows, cols, a, b, masterElements);
        mergeSolutionsSoFar(result, masterWork);

        for (int i = 1; i < nrProcs; i++) { // merge worker solutions
            Object[] fromWorker = new Object[1];
            MPI.COMM_WORLD.Recv(fromWorker, 0, 1, MPI.OBJECT, i, 0);
            List<List<Integer>> workerResult = (List<List<Integer>>) fromWorker[0];
            mergeSolutionsSoFar(result, workerResult);
        }

        return result;
    }

    public void worker(int myId, int nrProcs) {
        Object[] workload = new Object[1];
        Object[] matrixATemp = new Object[1];
        Object[] matrixBTemp = new Object[1];
        MPI.COMM_WORLD.Recv(workload, 0, 1, MPI.OBJECT, 0, 0);
        MPI.COMM_WORLD.Recv(matrixATemp, 0, 1, MPI.OBJECT, 0, 0);
        MPI.COMM_WORLD.Recv(matrixBTemp, 0, 1, MPI.OBJECT, 0, 0);

        List<Integer> elems = (List<Integer>) workload[0];
        List<List<Integer>> a = (List<List<Integer>>) matrixATemp[0];
        List<List<Integer>> b = (List<List<Integer>>) matrixBTemp[0];

        // compute and send back result
        int rows = a.size(), cols = b.get(0).size();
        List<List<Integer>> workerResult = computeWorkForElements(rows, cols, a, b, elems);
        MPI.COMM_WORLD.Send(new Object[]{workerResult}, 0, 1, MPI.OBJECT, 0, 0);
    }

    public void run(String[] args)
    {
        MPI.Init(args);
        int currentIndex = MPI.COMM_WORLD.Rank();
        int clusterSize = MPI.COMM_WORLD.Size();

        List<List<Integer>> a = Arrays.asList(Arrays.asList(1, 2, 10), Arrays.asList(1, 2, 3), Arrays.asList(1, 2, 3));
        List<List<Integer>> b = Arrays.asList(Arrays.asList(3, 2,1), Arrays.asList(3, 2,1), Arrays.asList(3, 2,1));


        if (currentIndex == 0)
        {
            List<List<Integer>> result = master(a, b, clusterSize);
            System.out.println(result);
        }
        else
        {
            worker(currentIndex, clusterSize);
        }
        MPI.Finalize();
    }

    public static void main(String[] args) {
        new MpiMatrixSum().run(args);
    }
}
