package com.example.cyclic_barrier_synchronization_mechanism.Model.ADT;

import com.example.cyclic_barrier_synchronization_mechanism.Model.Exceptions.MyException;

import java.util.ArrayList;

public interface IList<T> {
    void add(T newElem);
    ArrayList<T> getElems();
    T getElemAtIndex(int index) throws MyException;
    int size();
}
