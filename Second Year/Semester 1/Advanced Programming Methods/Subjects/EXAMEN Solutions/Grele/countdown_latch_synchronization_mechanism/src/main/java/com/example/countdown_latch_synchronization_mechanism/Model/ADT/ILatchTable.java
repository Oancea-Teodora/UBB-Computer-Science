package com.example.countdown_latch_synchronization_mechanism.Model.ADT;

import com.example.countdown_latch_synchronization_mechanism.Model.Exceptions.MyException;

import java.util.Map;

public interface ILatchTable {
    int addNewLatch(int counter);
    int getLatchCounter(int latchAddress) throws MyException;
    void decrementLatchCounter(int latchAddress) throws MyException;
    boolean isLatchAddressUsed(int latchAddress);
    Map<Integer, Integer> getContent();
}
