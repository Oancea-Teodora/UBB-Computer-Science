import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class Main {

    public static AtomicInteger cnt;
    public static ExecutorService executorService = Executors.newFixedThreadPool(10);

    public static boolean check(List<Integer> v){
        return v.get(0)%2==0;
    }

    public static void bktr(List<Integer> solution, int threadCount, int n){
        if(solution.size() == n){
            System.out.printf("%s\n", solution.toString());
            cnt.getAndIncrement();
            return;
        }

        if(threadCount == 1){
            // basic backtracking for permutation generation
            for(int i=1;i<=n;i++){
                if(solution.contains(i)) continue;
                solution.add(i);
                bktr(solution, threadCount, n);
                solution.remove(solution.size()-1);
            }
        }
        else{
            List<Integer> x = new ArrayList<>(solution);

            executorService.submit(
                    () ->{
                        for(int i=2;i<=n;i+=2){
                            if(x.contains(i)) continue;
                            x.add(i);
                            bktr(x, threadCount/2, n);
                            x.remove(x.size()-1);
                        }
                    }
            );

            for(int i=1;i<=n;i+=2){
                if(solution.contains(i)) continue;
                solution.add(i);
                bktr(solution, threadCount - threadCount/2, n);
                solution.remove(solution.size()-1);
            }
        }
    }

    public static void main(String[] args) {
        cnt = new AtomicInteger(0);
        bktr(new ArrayList<>(), 5, 3);
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
    }
}
