package com.example.cyclic_barrier_synchronization_mechanism.Model.ADT;

import com.example.cyclic_barrier_synchronization_mechanism.Model.Exceptions.MyException;
import javafx.util.Pair;

import java.util.List;
import java.util.Map;

public interface IBarrierTable {
    int addNewBarrier(int barrierCapacity);
    boolean isAddressUsed(int barrierAddress);
    Pair<Integer, List<Integer>> getBarrierFromAddress(int barrierAddress) throws MyException;
    Map<Integer, Pair<Integer, List<Integer>>> getContent();
}
