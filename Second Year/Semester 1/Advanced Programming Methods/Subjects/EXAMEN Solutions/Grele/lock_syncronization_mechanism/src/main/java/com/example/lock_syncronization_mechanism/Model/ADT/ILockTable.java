package com.example.lock_syncronization_mechanism.Model.ADT;

import com.example.lock_syncronization_mechanism.Model.Exceptions.MyException;

import java.util.Map;

public interface ILockTable {
    int addNewLockTableEntry(int value);
    int getLockTableValue(int address) throws MyException;
    void updateLockTableEntry(int address, int newValue) throws MyException;
    boolean isDefined(int address);
    void setContent(Map<Integer, Integer> newContent);
    Map<Integer, Integer> getContent();
}
