import java.util.ArrayList;
import java.util.List;

public class PrimeThread extends Thread{

    int start, stop;
    List<Integer> primes;
    List<Integer> solution;

    public PrimeThread(int start, int stop, List<Integer> primes){
        this.start = start;
        this.stop = stop;
        this.primes = primes;
        this.solution = new ArrayList<>();
    }

    @Override
    public void run(){
        for(int i= start;i < stop;i++){
            if(Main.isPrime(i)){
                System.out.printf("%d is prime\n", i);
                solution.add(i);
            }
        }
    }
}
