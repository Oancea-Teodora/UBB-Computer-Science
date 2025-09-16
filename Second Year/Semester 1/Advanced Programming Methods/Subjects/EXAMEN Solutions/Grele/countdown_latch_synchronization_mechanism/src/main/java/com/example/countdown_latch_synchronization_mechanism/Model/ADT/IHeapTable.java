package com.example.countdown_latch_synchronization_mechanism.Model.ADT;

import com.example.countdown_latch_synchronization_mechanism.Model.Exceptions.MyException;

import java.util.Map;

public interface IHeapTable<V> {
    int addNewHeapEntry(V value);
    V getHeapValue(int address) throws MyException;
    void updateHeapEntry(int address, V newValue) throws MyException;
    boolean isDefined(int address);
    void setContent(Map<Integer, V> newContent);
    Map<Integer, V> getContent();
}
