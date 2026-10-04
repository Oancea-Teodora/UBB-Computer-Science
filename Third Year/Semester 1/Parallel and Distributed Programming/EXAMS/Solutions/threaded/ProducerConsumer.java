package ro.alexpopa.threaded;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ProducerConsumer {

    private static class ProducerConsumerQueue<T> {
        public void enqueue(T val) {
            m_mtx.lock();
            m_queue.add(val);
            m_cv.signal();
            m_mtx.unlock();
        }

        /* Gets the next element from the queue. If the queue is empty but not closed, this function blocks.
        If the queue is empty and closed, returns null.*/
        public T dequeue() {
            T ret;
            m_mtx.lock();
            while (true) {
                if (!m_queue.isEmpty()) {
                    ret = m_queue.remove();
                    m_mtx.unlock();
                    return ret;
                }
                if (m_isEnd) {
                    m_mtx.unlock();
                    return null;
                }
                try {
                    m_cv.await();
                } catch(InterruptedException e) {
                    System.err.println("Exception: " + e);
                }
            }
        }

        /* Closes the queue.*/
        public void close() {
            m_mtx.lock();
            m_isEnd = true;
            m_cv.signalAll();
            m_mtx.unlock();
        }

        private final Lock m_mtx = new ReentrantLock();
        Queue<T> m_queue = new ArrayDeque<T>();
        private final Condition m_cv = m_mtx.newCondition();
        boolean m_isEnd = false;
    };

    private static class Producer implements Runnable {
        public Producer(ProducerConsumerQueue<Integer> queue, long nrNumbers) {
            this.queue = queue;
            this.nrNumbers = nrNumbers;
        }

        @Override
        public void run() {
            for (long i = 0; i < nrNumbers; ++i) {
                queue.enqueue((int)i);
            }
            queue.close();
        }

        private final ProducerConsumerQueue<Integer> queue;
        private final long nrNumbers;
    }

    private static class Adder implements Runnable {
        public Adder(ProducerConsumerQueue<Integer> inQueue, ProducerConsumerQueue<Integer> outQueue) {
            this.inQueue = inQueue;
            this.outQueue = outQueue;
        }

        @Override
        public void run() {
            int sum = 0;
            while (true) {
                Integer x = inQueue.dequeue();
                if (x == null) {
                    break;
                }
                sum += x;
            }
            outQueue.enqueue(sum);
        }

        private final ProducerConsumerQueue<Integer> inQueue;
        private final ProducerConsumerQueue<Integer> outQueue;
    }

    private static class Printer implements Runnable {
        public Printer(ProducerConsumerQueue<Integer> inQueue, int nrAdders) {
            this.inQueue = inQueue;
            this.nrAdders = nrAdders;
        }

        @Override
        public void run() {
            int sum = 0;
            for (int i = 0; i < nrAdders; ++i) {
                Integer x = inQueue.dequeue();
                if (x == null) {
                    System.err.println("Unexpected end of second queue.");
                    return;
                }
                sum += x;
            }
            System.out.println("Sum=" + sum);
        }

        private final ProducerConsumerQueue<Integer> inQueue;
        private final int nrAdders;
    }

    public static void main(String[] args) {
        int nrAdders;
        long nrNumbers;
        if (args.length != 2) {
            System.err.println("Usage: vector_sum_multithread nrAdders nrNumbers");
            return;
        }
        try {
            nrAdders = Integer.parseInt(args[0]);
            nrNumbers = Integer.parseInt(args[1]);
        } catch(Exception e) {
            System.err.println("Exception: " + e);
            return;
        }

        List<Thread> threads = new ArrayList<Thread>();
        ProducerConsumerQueue<Integer> firstQueue = new ProducerConsumerQueue<Integer>();
        ProducerConsumerQueue<Integer> secondQueue = new ProducerConsumerQueue<Integer>();

        threads.add(new Thread(new Printer(secondQueue, nrAdders)));
        threads.get(0).start();
        for(int i=0 ; i<nrAdders ; ++i) {
            threads.add(new Thread(new Adder(firstQueue, secondQueue)));
            threads.get(i+1).start();
        }
        threads.add(new Thread(new Producer(firstQueue, nrNumbers)));
        threads.get(nrAdders+1).start();

        try {
            for (int i = 0; i < nrAdders + 2; ++i) {
                threads.get(i).join();
            }
        } catch(InterruptedException e) {
            System.err.println("Exception: " + e);
            return;
        }
    }
}
