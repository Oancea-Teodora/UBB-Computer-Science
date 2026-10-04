package ro.alexpopa.mpi;

import mpi.MPI;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class MpiBigNumProduct {


    public static void master(List<Integer> a, List<Integer> b, int nrProcs){

        int n = a.size();
        int m = 2*n - 1;
        int toShare = nrProcs - 1;
        int step = n / toShare;
        int remainder = n % toShare;

        int start = 0, stop = 0;

        List<Integer> result = new ArrayList(2*n);
        for (int i = 0; i < 2 * n; i++) {
            result.add(0);
        }

        int carry = 0;

        for (int i = 1; i <= toShare ; i++) {
            stop = start + step;
            if (remainder>0){
                stop++;
                remainder--;
            }
            int countB = stop-start;
            List<Integer> bCopy = new ArrayList<>();
            for(int j = start; j<stop; j++){
                bCopy.add(b.get(j));
            }

            int[] metadata = new int[]{n, countB, start};
            MPI.COMM_WORLD.Send(metadata,0,3,MPI.INT, i, 0);
            MPI.COMM_WORLD.Send(new Object[]{a},0,1,MPI.OBJECT,i,0);
            MPI.COMM_WORLD.Send(new Object[]{bCopy},0,1,MPI.OBJECT,i,0);

            start = stop;
        }
        for (int i = 1; i <= toShare ; i++) {
            Object[] received = new Object[1];

            MPI.COMM_WORLD.Recv(received,0, 1, MPI.OBJECT, i, 0);

            List<Integer> receivedList = (List<Integer>) received[0];
            carry = 0;

            for (int p = 0; p < m+1; p++) {
                result.set(p,result.get(p) + receivedList.get(p) + carry);
                carry = result.get(p)/10;
                result.set(p,result.get(p)%10);
            }

        }
        if (carry==1){
            result.set(m, 1);
        }
        Collections.reverse(result);
        System.out.println(result);
    }

    public static void worker(int id, int nrProcs){
        int[] metadata = new int[3];
        Object[] objs = new Object[2];
        MPI.COMM_WORLD.Recv(metadata, 0, 3, MPI.INT, 0, 0);
        MPI.COMM_WORLD.Recv(objs, 0, 1, MPI.OBJECT, 0, 0);
        MPI.COMM_WORLD.Recv(objs, 1, 1, MPI.OBJECT, 0, 0);
        int n = metadata[0];
        int bSize = metadata[1];
        int start = metadata[2];
        List<Integer> a = (List<Integer>) objs[0];
        List<Integer> b = (List<Integer>) objs[1];
        List<Integer> result = new ArrayList(2*n);
        for (int i = 0; i < 2 * n; i++) {
            result.add(0);
        }
        for (int i = 0; i < bSize; i++) {
            for (int j = 0; j < n; j++) {
                int prod = a.get(j) * b.get(i);
                result.set(start+i+j, result.get(start+i+j) + prod);
            }
        }

        MPI.COMM_WORLD.Send(new Object[]{result}, 0, 1, MPI.OBJECT, 0, 0);
    }


    public static void main(String[] args) {
        MPI.Init(args);
        int me = MPI.COMM_WORLD.Rank();
        int nrProcs = MPI.COMM_WORLD.Size();
        List<Integer> a = Arrays.asList(1,2,3); // is actually 321
        List<Integer> b = Arrays.asList(1,2,3); // is actually 321
        if (me==0){
            master(a,b,nrProcs);
        }
        else{
            worker(me, nrProcs);
        }
        MPI.Finalize();
    }

}
