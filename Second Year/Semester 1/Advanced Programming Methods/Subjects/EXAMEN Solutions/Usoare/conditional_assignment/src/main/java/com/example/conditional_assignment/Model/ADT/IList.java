package com.example.conditional_assignment.Model.ADT;

import com.example.conditional_assignment.Model.Exceptions.MyException;

import java.util.ArrayList;

public interface IList<T> {
    void add(T newElem);
    ArrayList<T> getElems();
    T getElemAtIndex(int index) throws MyException;
    int size();
}
