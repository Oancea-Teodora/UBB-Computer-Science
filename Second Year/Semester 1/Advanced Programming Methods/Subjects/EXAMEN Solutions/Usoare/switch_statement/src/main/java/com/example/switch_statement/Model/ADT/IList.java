package com.example.switch_statement.Model.ADT;

import com.example.switch_statement.Model.Exceptions.MyException;

import java.util.ArrayList;

public interface IList<T> {
    void add(T newElem);
    ArrayList<T> getElems();
    T getElemAtIndex(int index) throws MyException;
    int size();
}
