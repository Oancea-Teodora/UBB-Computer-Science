import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class Main {

    public static AtomicInteger cnt;
    public static ExecutorService executorService = Executors.newFixedThreadPool(10);

    public static boolean check(List<Integer> v) {
        return v.get(0) % 2 == 0;
    }

    public static void bktr(List<Integer> solution, int threadCount, int n, int k) {
        if (solution.size() == k) {
            System.out.printf("%s\n", solution.toString());
            cnt.getAndIncrement();
            return;
        }
        int last = 0;
        if (solution.size() > 0)
            last = solution.get(solution.size() - 1);

        if (threadCount == 1) {
            // bktr for combinations


            for (int i = last + 1; i <= n; i++) {
                if (solution.contains(i)) continue;
                solution.add(i);
                bktr(solution, threadCount, n, k);
                solution.remove(solution.size() - 1);

            }
        }
        else{
            List<Integer> x = new ArrayList<>(solution);

            int finalLast = last;
            executorService.submit(
                    ()->{
                      for(int i = finalLast +1; i<=n; i+=2){
                          if(x.contains(i)) continue;
                          x.add(i);
                          bktr(x, threadCount/2, n,k);
                          x.remove(x.size()-1);
                      }
                    }
            );
            for (int i = last + 2; i <= n; i+=2) {
                if (solution.contains(i)) continue;
                solution.add(i);
                bktr(solution, threadCount - threadCount/2, n, k);
                solution.remove(solution.size() - 1);

            }
        }
    }

    public static void main(String[] args) {
        cnt = new AtomicInteger(0);
        bktr(new ArrayList<>(), 3, 5,3);
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
        System.out.printf("Count: %d", cnt.get());
    }
}
