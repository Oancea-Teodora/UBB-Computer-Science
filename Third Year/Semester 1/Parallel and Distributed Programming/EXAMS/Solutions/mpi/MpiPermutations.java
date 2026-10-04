package ro.alexpopa.mpi;

import mpi.MPI;

import java.util.*;

public class MpiPermutations {

    public static boolean check(List<Integer> vec){
        return vec.get(0)%2==0;
    }

    public static void back(List<Integer> sol, int n, List<List<Integer>> sols){
        if(sol.size()==n){
            if(check(sol)){
                System.out.println(sol.toString());
                sols.add(new ArrayList<>(sol));
            }
            return;
        }

        for (int i = 0; i <= n ; i++) {
            if(sol.contains(i)) continue;
            sol.add(i);
            back(sol, n, sols);
            sol.remove(sol.size()-1);

        }
    }
    private static void worker() {
        int[] metadata = new int[3];
        MPI.COMM_WORLD.Recv(metadata, 0, 3, MPI.INT, 0, 0);

        List<List<Integer>> localSols = new ArrayList<>(metadata[2]);
        for (int i = metadata[0]; i < metadata[1]; i++) {
            List<Integer> sol = new ArrayList<>();
            sol.add(i);
            back(sol, metadata[2], localSols);
        }
        System.out.println(localSols);
        MPI.COMM_WORLD.Send(new Object[]{localSols}, 0,1,MPI.OBJECT, 0,0);
    }

    private static void master(int n, int nrProcs) {
        int toShare = nrProcs - 1;
        int step = n / toShare;
        int remainder = n % toShare;

        int start = 0, stop = 0;
        List<List<Integer>> solutions = new ArrayList<>();
        for (int i = 1; i <= toShare; i++) {
            stop = start+step;
            if(remainder>0){
                stop++;
                remainder--;
            }

            int[] metadata = new int[]{start, stop, n};
            MPI.COMM_WORLD.Send(metadata, 0, 3, MPI.INT, i, 0);
            start = stop;
        }

        for (int i = 1; i <= toShare ; i++) {
            Object[] objects = new Object[1];

            MPI.COMM_WORLD.Recv(objects, 0, 1, MPI.OBJECT, i, 0);
            solutions.addAll((List<List<Integer>>) objects[0]);
        }
        System.out.println(solutions);

    }

    public static void main(String[] args) {
        MPI.Init(args);
        int me = MPI.COMM_WORLD.Rank();
        int nrProcs = MPI.COMM_WORLD.Size();
        int n = 3;
        if (me==0){
            master(n, nrProcs);
        }
        else{
            worker();
        }
        MPI.Finalize();
    }



}
