import java.util.Arrays;
import java.util.concurrent.*;

public class Main {
    public static ExecutorService executorService = Executors.newFixedThreadPool(10);

    public static int[][] matrixMultiplication(int[][] a, int[][] b) {
        int size = a.length;
        int c[][] = new int[size][size];

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                for (int k = 0; k < size; k++)
                    c[i][j] += a[i][k] * b[k][j];
            }
        }
        return c;
    }

    public static int[][] matrixPow(int[][] a, int pow, int threadCount) throws ExecutionException, InterruptedException {
        if (pow == 1) {
            return a;
        }

        if (threadCount <= 1) {
            int[][] result = matrixPow(a, pow / 2, threadCount);
            result = matrixMultiplication(result, result);
            if (pow % 2 == 1) {
                result = matrixMultiplication(result, a);
            }
            return result;
        } else {
            Future<int[][]> s1 = executorService.submit(() -> matrixPow(a, pow / 2, threadCount / 2));
            int[][] result = s1.get();
            result = matrixMultiplication(result, result);

            if (pow % 2 == 1) {
                result = matrixMultiplication(result, a);
            }

            return result;
        }
    }

    public static void main(String[] args) throws ExecutionException, InterruptedException {

        int[][] a = {{1, 2, 3}, {3, 2, 1}, {2, 1, 2}};
        int pow = 3;
        int threadCount = 4;

        int[][] result = matrixPow(a, pow, threadCount);
        executorService.shutdown();
        try {
            if (!executorService.awaitTermination(300, TimeUnit.SECONDS)) {
                executorService.shutdownNow();
            }
        } catch (InterruptedException ex) {
            executorService.shutdownNow();
            ex.printStackTrace();
            Thread.currentThread().interrupt();
        }

        System.out.println(Arrays.deepToString(result));

    }
}
