import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Main {
    public static ExecutorService executorService = Executors.newFixedThreadPool(10);


    public static void main(String[] args) {

        int n = 3;
//        int[][] a = {{1, 1, 1}, {1, 1, 1}, {1, 1, 1}};
//        int[][] b = {{1, 1, 1}, {1, 1, 1}, {1, 1, 1}};
        int[][] a = {{1, 2, 3}, {1, 2, 3}, {1, 2, 3}};
        int[][] b = {{3,2,1}, {3,2,1}, {3,2,1}};
        int[][] c = new int[3][3];
        int threadCount = 5;

        for (int index = 0; index < threadCount;index++){
            int finalIndex = index;
            executorService.submit(
                    ()->{
                        for(int i = finalIndex; i < n; i+= threadCount){
                            for(int j=0;j<n;j++){
                                for(int k=0;k<n;k++){
                                    c[i][j] += a[i][k] * b[k][j];
                                }
                            }
                        }
                    }
            );
        }

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
        System.out.printf("Matrix c: %s", Arrays.deepToString(c));

    }
}
