package com.example.cyclic_barrier_synchronization_mechanism.Model.ADT;

import com.example.cyclic_barrier_synchronization_mechanism.Model.Exceptions.MyException;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyBarrierTable implements IBarrierTable{
    private int nextFreeAddress;
    private HashMap<Integer, Pair<Integer, List<Integer>>> elems;

    public MyBarrierTable() {
        this.nextFreeAddress = 1;
        this.elems = new HashMap<>();
    }

    @Override
    public synchronized int addNewBarrier(int barrierCapacity) {
        this.elems.put(this.nextFreeAddress, new Pair<>(barrierCapacity, new ArrayList<>()));
        this.nextFreeAddress = this.nextFreeAddress + 1;
        return this.nextFreeAddress - 1;
    }

    @Override
    public synchronized boolean isAddressUsed(int barrierAddress) {
        return this.elems.containsKey(barrierAddress);
    }

    @Override
    public synchronized Pair<Integer, List<Integer>> getBarrierFromAddress(int barrierAddress) throws MyException {
        if (!this.elems.containsKey(barrierAddress)) {
            throw new MyException("The given address (" + barrierAddress + ") is not an index in the Barrier Table.");
        }
        return this.elems.get(barrierAddress);
    }

    @Override
    public synchronized Map<Integer, Pair<Integer, List<Integer>>> getContent() {
        return this.elems;
    }
}
