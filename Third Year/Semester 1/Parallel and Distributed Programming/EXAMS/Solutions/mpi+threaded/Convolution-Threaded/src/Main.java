import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Main {

    public static ExecutorService executorService = Executors.newFixedThreadPool(10);

    public static void main(String[] args) {

        int[] a = {1, 2, 3};
        int[] b = {1, 2, 3};
        int[] c = new int[3];
        int threadCount = 3;
        int n = 3;


        for(int index = 0; index <= threadCount; index ++){
            int finalIndex = index;
            executorService.submit(
                    ()->{
                        for(int i = finalIndex; i<n; i+=threadCount){
                            for(int j=0;j<n;j++){
                                c[i] += a[j] * b[(i-j + n)%n];
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

        System.out.printf("Solution c: %s", Arrays.toString(c));

        /*
        r[0] = a[0] * b[0] + a[1] * b[2] + a[2] * b[1] = 1 * 1 + 2 * 3 + 3 * 2 = 1 + 6 + 6 = 13
        r[1] = a[0] * b[1] + a[1] * b[0] + a[2] * b[2] = 1 * 2 + 2 * 1 + 3 * 3 = 2 + 2 + 9 = 13
        r[2] = a[0] * b[2] + a[1] * b[1] + a[2] * b[0] = 1 * 3 + 2 * 2 + 3 * 1 = 3 + 4 + 3 = 10
         */

    }
}
