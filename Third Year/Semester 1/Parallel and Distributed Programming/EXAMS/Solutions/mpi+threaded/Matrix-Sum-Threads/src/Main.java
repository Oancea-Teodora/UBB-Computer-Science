import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Main {

    private static ExecutorService executorService = Executors.newFixedThreadPool(10);

    public static List<Integer> linearizeMatrix(int[][] a, int n, int m){
        List<Integer> solution = new ArrayList<>();

        for(int i=0;i<n;i++){
            for(int j=0;j<m;j++){
                solution.add(a[i][j]);
            }
        }
        return solution;
    }

    public static int recursiveSum(List<Integer> a, int beginIndex, int endIndex, int threadCount) throws InterruptedException, ExecutionException {
        if(endIndex == beginIndex + 1){
            return a.get(beginIndex);
        }

        int middle = (beginIndex + endIndex)/2;

        if(threadCount <= 1){
            return recursiveSum(a, beginIndex, middle, 1) + recursiveSum(a, middle, endIndex, 1);
        }

        Future<Integer> s1 = executorService.submit(
                () -> recursiveSum(a, beginIndex, middle, threadCount/2)
        );
        int s2 = recursiveSum(a, middle, endIndex, threadCount - threadCount/2);

        return s1.get() + s2;

    }

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        int[][] data = {{1, 2, 3, 4, 1}, {5, 6, 7, 8, 1}, {9, 10,11,12,0}};
        List<Integer> result = linearizeMatrix(data, 3, 5);
        int res = recursiveSum(result,0,result.size(),4);
        executorService.shutdownNow();

        System.out.printf("%d\n", res);
    }
}
