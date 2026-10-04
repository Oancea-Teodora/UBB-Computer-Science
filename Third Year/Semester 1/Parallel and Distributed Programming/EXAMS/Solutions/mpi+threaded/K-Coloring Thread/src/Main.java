import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        int n, k;
        Scanner myInput = new Scanner(System.in);

        System.out.print("Enter n: ");
        n = myInput.nextInt();

        System.out.print("Enter k: ");
        k = myInput.nextInt();

        int maxValue = 1;

        for(int i=0;i<n;i++){
            maxValue *= k;
        }

        int threadCount = 3;
        List<Thread> threads = new ArrayList<>();

        for (int i = 0; i < threadCount; i++) {
            threads.add(new kthread(i, maxValue, threadCount, n, k));
        }

        for (Thread thread : threads) {
            thread.start();
        }

        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }
}
