package ro.alexpopa.threaded;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;

public class QuickSort {
    private static final ExecutorService EXECUTOR_SERVICE = Executors.newCachedThreadPool();

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        List<Integer> array = Arrays.asList(9, 9, 8, 7, 6, 5, 4, 3, 2, 1);
        quicksort(array, 0, array.size() - 1);
        array.forEach(s -> System.out.print(s + " "));
        EXECUTOR_SERVICE.shutdown();
        EXECUTOR_SERVICE.awaitTermination(10, TimeUnit.MINUTES);
    }

    private static Object quicksort(List<Integer> array, int start, int end) throws ExecutionException, InterruptedException {
        if(start < end) {
            int p = partition(array, start, end);
            List<Integer> copy1 = new ArrayList<>(array);
            List<Integer> copy2 = new ArrayList<>(array);
            Future<Object> result1 = EXECUTOR_SERVICE.submit(() -> quicksort(copy1, start, p - 1));
            Future<Object> result2 = EXECUTOR_SERVICE.submit(() -> quicksort(copy2, p + 1, end));
            result1.get();
            result2.get();
            for(int i = start; i <= p - 1; i++) {
                array.set(i, copy1.get(i));
            }
            for(int i = p + 1; i < end; i++) {
                array.set(i, copy2.get(i));
            }
        }
        return null;
    }

    private static int partition(List<Integer> array, int start, int end) {
        int pivot = array.get(end);
        int i = start - 1;
        for(int j = start; j < end; j++) {
            if(array.get(j) < pivot) {
                i++;

                int temp = array.get(i);
                array.set(i, array.get(j));
                array.set(j, temp);
            }
        }

        int temp = array.get(i + 1);
        array.set(i + 1, array.get(end));
        array.set(end, temp);

        return i + 1;
    }
}
