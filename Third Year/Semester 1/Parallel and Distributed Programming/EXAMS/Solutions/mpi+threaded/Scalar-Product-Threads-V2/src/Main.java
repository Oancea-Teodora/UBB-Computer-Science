import java.util.concurrent.*;

public class Main {

    public static ExecutorService executorService = Executors.newFixedThreadPool(10);

    public static int scalarProduct(int[] a, int[] b, int start, int end, int threadCount) throws ExecutionException, InterruptedException {
        if(end == start + 1){
            return a[start] * b[start];
        }

        int middle = (start + end)/2;

        if(threadCount <= 1){
            return scalarProduct(a, b, start, middle, 1) + scalarProduct(a, b, middle, end, 1);
        }

        Future<Integer> f1 = executorService.submit(()->scalarProduct(a, b,start, middle, threadCount/2));
        int f2 = scalarProduct(a, b, middle, end, threadCount - threadCount/2);

        return f1.get() + f2;
    }

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        int[] a = {1, 2, 3};
        int[]  b= {1, 2, 3};

        int threadCount = 5;
        int n = 3;

        int result = scalarProduct(a, b, 0, n, threadCount);

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

        System.out.printf("Scalar product of a and b is %d", result);


    }
}
