package ro.alexpopa.threaded;

import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ScalarProductSimple {

    public List<Pair<Integer,Integer>> splitWorkload(int n, int t){
        List<Pair<Integer,Integer>> pairs = new ArrayList<>();
        int index = 0;
        int step = n/t;
        int mod = n%t;
        while(index<n){
            int aux;
            if(mod>0)
                aux = 1;
            else aux = 0;
            pairs.add(new Pair(index, index+step+aux));
            index+=step+aux;
            mod--;
        }
        return pairs;
    }

    public int scalarProduct(List<Integer> a, List<Integer> b, int T) throws InterruptedException {
        List<Integer> c = new ArrayList<>();
        int n = a.size();
        for (int i = 0; i < T; i++) {
            c.add(0);
        }
        List<Pair<Integer,Integer>> pairs = splitWorkload(n, T);
        int final_sum = 0;
        ExecutorService executorService = Executors.newFixedThreadPool(T);
        for(int k=0;k<T;k++){
            int finalK = k;
            executorService.submit(()->{
                for(int x = pairs.get(finalK).getKey(); x<pairs.get(finalK).getValue(); x++){
                    c.set(finalK, a.get(x) * b.get(x));
                }
            });
        }
        executorService.shutdown();
        for (int i = 0; i < T; i++) {
            final_sum+=c.get(i);
        }
        return final_sum;
    }

}
