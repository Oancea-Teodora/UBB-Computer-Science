import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Main {

    public static ExecutorService executorService = Executors.newFixedThreadPool(10);

    public static void main(String[] args) {
        int[] a = {1, 2, 3};
        int[] b = {1, 2, 3};
        int n = 3;
        int m = 2 * n - 1;

        int[] c = new int[m];

        int threadCount = 4;

        for (int i = 0; i < m; i++) {
            c[i] = 0;
        }

        for (int index = 0; index < threadCount; index++) {
            int finalIndex = index;
            executorService.submit(
                    () -> {
                        for(int x = finalIndex; x <m; x+=threadCount){
                            for(int i = 0;i<n;i++){
                                if((x-i) < 0 || (x-i) >=n){continue;}
                                c[x] += a[i]*b[x-i];
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

        List<Integer> result = new ArrayList<>();
        int carry = 0;

        for(int i = c.length-1;i>=0;i--){
            c[i] = c[i] + carry;
            result.add(c[i] % 10);
            if(c[i] > 9){
                carry = c[i] / 10;
            }
            else{
                carry = 0;
            }
        }

        while(carry > 0){
            result.add(carry%10);
            carry/=10;
        }

        Collections.reverse(result);
        System.out.printf("Result: %s", result.toString());
    }
}
