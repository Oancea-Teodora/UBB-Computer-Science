package ro.alexpopa.mpi;

import mpi.MPI;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class MpiMatrixProduct {

    int getIndexFromCell(int i, int j, int m) {
        return i * m + j;
    }

    List<Integer> getCellFromIndex(int index, int m) {
        return Arrays.asList(index / m, index % m);
    }

    List<Integer> getCellsById(int id, int n, int m, int nrProcesses) {
        int chunk = (n * m) / nrProcesses;
        int remaining = (n * m) % nrProcesses;
        int last = (n * m) - remaining;

        int start = id * chunk;
        int stop = (id + 1) * chunk;
        List<Integer> cells = IntStream.range(start, stop).boxed().collect(Collectors.toList());

        if (id < remaining) {
            cells.add(last + id);
        }
        return cells;
    }

    List<List<Integer>> createEmptyMatrix(int n, int m) {
        List<List<Integer>> matrix = new ArrayList<>();
        for (int i = 0; i < n; ++i) {
            matrix.add(IntStream.range(0, m).map(value -> 0).boxed().collect(Collectors.toList()));
        }
        return matrix;
    }

    Integer computeSingleElement(List<List<Integer>> a, List<List<Integer>> b, int i, int j) {
        int sum = 0;
        for (int k = 0; k < b.size(); ++k) {
            sum += a.get(i).get(k) * b.get(k).get(j);
        }
        return sum;
    }

    List<List<Integer>> work(int n, int m, List<List<Integer>> a, List<List<Integer>> b, List<Integer> cells) {
        List<List<Integer>> matrix = createEmptyMatrix(n, m);
        cells.stream()
                .map(cell -> getCellFromIndex(cell, m))
                .forEach(cell -> {
                    int i = cell.get(0), j = cell.get(1);
                    matrix.get(i).set(j, computeSingleElement(a, b, i, j));
                });
        return matrix;
    }

    void unitePartialSolution(List<List<Integer>> solution, List<List<Integer>> temp) {
        int n = solution.size(), m = solution.get(0).size();
        for (int i = 0; i < n; ++i) {
            for (int j = 0; j < m; ++j) {
                int previous = solution.get(i).get(j);
                solution.get(i).set(j, previous + temp.get(i).get(j));
            }
        }
    }

    void master(int nrProcesses, List<List<Integer>> a, List<List<Integer>> b) {
        int n = a.size(), m = b.get(0).size();
        List<Integer> masterCells = getCellsById(0, n, m, nrProcesses);
        List<List<Integer>> solution = createEmptyMatrix(n, m);

        for (int i = 1; i < nrProcesses; ++i) {
            List<Integer> childCells = getCellsById(i, n, m, nrProcesses);
            MPI.COMM_WORLD.Send(new Object[]{a}, 0, 1, MPI.OBJECT, i, 0);
            MPI.COMM_WORLD.Send(new Object[]{b}, 0, 1, MPI.OBJECT, i, 0);
            MPI.COMM_WORLD.Send(new Object[]{childCells}, 0, 1, MPI.OBJECT, i, 0);
        }

        List<List<Integer>> part = work(n, m, a, b, masterCells);
        unitePartialSolution(solution, part);
        for (int i = 1; i < nrProcesses; ++i) {
            Object[] temp = new Object[1];
            MPI.COMM_WORLD.Recv(temp, 0, 1, MPI.OBJECT, i, 1);
            List<List<Integer>> childResult = (List<List<Integer>>) temp[0];
            unitePartialSolution(solution, childResult);
        }

        System.out.println(solution);
    }

    void worker(int myId, int nrProcesses) {
        Object[] a = new Object[1];
        Object[] b = new Object[1];
        Object[] c = new Object[1];
        MPI.COMM_WORLD.Recv(a, 0, 1, MPI.OBJECT, 0, 0);
        MPI.COMM_WORLD.Recv(b, 0, 1, MPI.OBJECT, 0, 0);
        MPI.COMM_WORLD.Recv(c, 0, 1, MPI.OBJECT, 0, 0);
        List<List<Integer>> matrix1 = (List<List<Integer>>) a[0];
        List<List<Integer>> matrix2 = (List<List<Integer>>) b[0];
        List<Integer> cells = (List<Integer>) c[0];

        int n = matrix1.size(), m = matrix2.get(0).size();
        List<List<Integer>> part = work(n, m, matrix1, matrix2, cells);
        MPI.COMM_WORLD.Send(new Object[]{part}, 0, 1, MPI.OBJECT, 0, 1);
    }

    public void run(String[] args)
    {
        MPI.Init(args);
        int currentIndex = MPI.COMM_WORLD.Rank();
        int clusterSize = MPI.COMM_WORLD.Size();

        List<List<Integer>> a = Arrays.asList(Arrays.asList(1, 2, 10), Arrays.asList(1, 2, 3), Arrays.asList(1, 2, 3));
        List<List<Integer>> b = Arrays.asList(Arrays.asList(3, 2), Arrays.asList(3, 2), Arrays.asList(3, 2));


        if (currentIndex == 0)
        {
            master(clusterSize, a, b);
        }
        else
        {
            worker(currentIndex, clusterSize);
        }
        MPI.Finalize();
    }

    public static void main(String[] args) {
        new MpiMatrixProduct().run(args);
    }
}
