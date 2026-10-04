package ro.alexpopa.threaded;

// number of permutations of N that satisfy a given property

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

public class Permutations {

    public AtomicInteger cnt;
    public ExecutorService executorService = Executors.newFixedThreadPool(10);
    public Permutations() {
        cnt = new AtomicInteger(0);
    }

    public boolean check(List<Integer> v){
        return v.get(0)%2==0;
    }

    public void back(List<Integer> sol, int T,int n) throws InterruptedException {
        if(sol.size()==n){
            System.out.println(sol.toString());
            if(check(sol)){
                System.out.println(sol.toString());
                cnt.getAndIncrement();
            }
            return;
        }
        if (T==1){
            for (int i = 1; i <= n ; i++) {
                if(sol.contains(i)) continue;
                sol.add(i);
                back(sol, T, n);
                sol.remove(sol.size()-1);
            }
        }
        else {
            List<Integer> x = new ArrayList<>(sol);
            executorService.submit(()->{
                for(int i=1;i<=n;i+=2){
                    if(x.contains(i)) continue;
                    x.add(i);
                    try {
                        back(x, T/2, n);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    x.remove(x.size()-1);
                }
            });
            for(int i=2;i<=n;i+=2){
                if (sol.contains(i)) continue;
                sol.add(i);
                back(sol, T-T/2,n);
                sol.remove(sol.size()-1);
            }
        }
    }
}
