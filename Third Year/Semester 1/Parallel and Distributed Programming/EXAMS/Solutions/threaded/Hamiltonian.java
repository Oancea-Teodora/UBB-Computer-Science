package ro.alexpopa.threaded;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.ReentrantLock;

public class Hamiltonian {

    public boolean solved = false;
    public static int V = 5;
    public ExecutorService executorService = Executors.newFixedThreadPool(10);

    public boolean isSafe(int v, boolean[][] graph, List<Integer> path, int pos){
        if (!graph[path.get(pos-1)][v])
            return false;
        for (int i = 0; i < pos; i++) {
            if (path.get(i)==v)
                return false;
        }
        return true;
    }

    public void hamiltonian(boolean[][] graph, List<Integer> path, int pos, int T){
        if(solved) {
            return;
        }
        if(pos==V){
            if (graph[path.get(pos - 1)][path.get(0)]){
                solved=true;
                this.executorService.shutdown();
                System.out.println(path.toString());
            }
            return;
        }
        if(T==1){
            for (int i = 0; i < V; i++) {
                if(isSafe(i, graph, path, pos)){
                    path.add(i);
                    hamiltonian(graph, path, pos+1, T);
                    path.remove(path.size()-1);
                }
            }
        }
        else{
            executorService.submit(()->{
                List<Integer> newPath = new ArrayList<>(path);
                for (int i = 0; i < V; i+=2) {
                    if(isSafe(i, graph, path, pos)){
                        newPath.add(i);
                        hamiltonian(graph, newPath, pos+1, T/2);
                        newPath.remove(newPath.size()-1);
                    }
                }
            });
            List<Integer> aux = new ArrayList<>(path);
            for (int i = 1; i < V; i+=2) {
                if(isSafe(i, graph, path, pos)){
                    aux.add(i);
                    hamiltonian(graph, aux, pos+1, T-T/2);
                    aux.remove(aux.size()-1);
                }
            }
        }
    }

}
