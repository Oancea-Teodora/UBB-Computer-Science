import java.util.ArrayList;
import java.util.List;
import java.lang.Math;

import static java.lang.Math.sqrt;


public class Main {

    public static boolean isPrime(int x){
        if (x < 2){
            return false;
        }
        if (x == 2){
            return true;
        }
        if ( x%2 == 0){
            return false;
        }
        for(int j=3;j*j<=x;j+=2){
            if(x%j == 0){
                return false;
            }
        }
        return true;
    }

    public static boolean checkPrime(int n, List<Integer> checkNumbers){
        for(Integer number: checkNumbers){
            if(n%number == 0)
                return false;
        }
        return true;
    }

    public static List<Integer> primesSqrtN(int N){
        List<Integer> primes = new ArrayList<>();
        for(int i=2;i<=sqrt(N);i++){
            if(isPrime(i)){
                primes.add(i);
            }
        }
        return primes;
    }


    public static void main(String[] args) throws InterruptedException {
        int n = 25;
        int threadCount = 5;

        List<Integer> primesForN = primesSqrtN(n);
        int size = n - (int)sqrt(n) + 1;
        int start = (int)sqrt(n) + 1;
        int stop;
        int step = size/threadCount;
        int remainder = size%threadCount;

        List<Integer> solution = new ArrayList<>();

        List<Thread> threads = new ArrayList<>();

        for(int i=0;i<threadCount;i++){
            stop = start + step;
            if(remainder > 0){
                stop++;
                remainder--;
            }
            if(stop > n){
                stop = n;
            }

            int finalStart = start;
            int finalStop = stop;
            Thread th = new Thread(
                    ()->{
                        for(int j = finalStart; j < finalStop; j++){
                            if(checkPrime(j, primesForN)){
                                System.out.printf("%d is prime\n", j);
                                synchronized (solution) {
                                    solution.add(j);
                                }
                            }
                        }
                    }
            );
            threads.add(th);

            System.out.printf("For thread %d: start - %d; stop - %d\n", i, start, stop);
            start = stop;
        }

        for(Thread t: threads){
            t.start();
        }
        for(Thread t:threads){
            t.join();
        }

        System.out.printf("Final list of prime numbers: %s", solution.toString());


    }
}
