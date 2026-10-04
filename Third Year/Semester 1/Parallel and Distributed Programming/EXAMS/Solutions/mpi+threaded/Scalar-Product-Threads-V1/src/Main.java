import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class Main {
    public static void main(String[] args) {

        ExecutorService executorService = Executors.newFixedThreadPool(10);

        AtomicInteger scalarProduct = new AtomicInteger();

//        int[] a = {1,2,3};
//        int[] b = {1,2,3};
        int[] a = {2, 2, 2, 2};
        int[] b = {2, 2, 2, 2};

        int threadCount = 5;
//        int n = 3;
        int n = 4;

        for (int index = 0; index < threadCount; index++) {
            int finalIndex = index;
            executorService.submit(
                    () -> {
                        for (int i = finalIndex; i < n; i += threadCount) {
                            scalarProduct.addAndGet(a[i] * b[i]);
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

        System.out.printf("Scalar product of a and b is %d", scalarProduct.get());
    }
}
