package ro.alexpopa.mpi;

import mpi.MPI;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MpiScalarProduct {
    public static void master(List<Integer> a, List<Integer> b, int nrProcs){

        int n = a.size();
        int toShare = nrProcs - 1;
        int step = n / toShare;
        int remainder = n % toShare;

        int start = 0, stop = 0;

        List<Integer> result = new ArrayList(n);
        for (int i = 0; i < n; i++) {
            result.add(0);
        }

        for (int i = 1; i <= toShare ; i++) {
            stop = start + step;
            if (remainder>0){
                stop++;
                remainder--;
            }

            int[] metadata = new int[]{start, stop};
            MPI.COMM_WORLD.Send(metadata,0,2,MPI.INT, i, 0);
            MPI.COMM_WORLD.Send(new Object[]{a},0,1,MPI.OBJECT,i,0);
            MPI.COMM_WORLD.Send(new Object[]{b},0,1,MPI.OBJECT,i,0);

            start = stop;
        }

        for (int i = 1; i <= toShare; i++) {
            Object[] received = new Object[1];
            int[] metadata = new int[2];
            MPI.COMM_WORLD.Recv(metadata, 0, 2, MPI.INT, i, 0);
            MPI.COMM_WORLD.Recv(received,0, 1, MPI.OBJECT, i, 0);

            start = metadata[0];
            stop = metadata[1];
            List<Integer> receivedList = (List<Integer>) received[0];

            for (int p = start; p < stop; p++) {
                result.set(p, receivedList.get(p));
            }
        }
        System.out.println(result.stream().reduce(0,(x,y)-> x+y));
    }

    public static void worker(int id, int nrProcs){
        int[] metadata = new int[2];
        Object[] objs = new Object[2];
        MPI.COMM_WORLD.Recv(metadata, 0, 2, MPI.INT, 0, 0);
        MPI.COMM_WORLD.Recv(objs, 0, 1, MPI.OBJECT, 0, 0);
        MPI.COMM_WORLD.Recv(objs, 1, 1, MPI.OBJECT, 0, 0);
        int start = metadata[0];
        int stop = metadata[1];
        List<Integer> a = (List<Integer>) objs[0];
        List<Integer> b = (List<Integer>) objs[1];
        List<Integer> result = new ArrayList(a.size());
        for (int i = 0; i < a.size(); i++) {
            result.add(0);
        }
        for (int i = start; i < stop; i++) {
            result.set(i,a.get(i) * b.get(i));
        }

        MPI.COMM_WORLD.Send(metadata, 0, 2, MPI.INT, 0, 0);
        MPI.COMM_WORLD.Send(new Object[]{result}, 0, 1, MPI.OBJECT, 0, 0);
    }


    public static void main(String[] args) {
        MPI.Init(args);
        int me = MPI.COMM_WORLD.Rank();
        int nrProcs = MPI.COMM_WORLD.Size();
        List<Integer> a = Arrays.asList(1,2,3,4);
        List<Integer> b = Arrays.asList(1,2,3,5);
        if (me==0){
            master(a,b,nrProcs);
        }
        else{
            worker(me, nrProcs);
        }
        MPI.Finalize();
    }

}
