package com.example.lock_syncronization_mechanism.Model.ADT;


import com.example.lock_syncronization_mechanism.Model.Exceptions.MyException;

import java.util.HashMap;
import java.util.Map;

public class LockTable implements ILockTable {
    private HashMap<Integer, Integer> elems;
    private int nextFreeLocation;
    
    public LockTable() {
        this.elems = new HashMap<>();
        this.nextFreeLocation = 1;
    }
    
    @Override
    public synchronized int addNewLockTableEntry(int value) {
        this.elems.put(this.nextFreeLocation, value);
        this.nextFreeLocation = this.nextFreeLocation + 1;
        return this.nextFreeLocation - 1;   // returns the address on which the value was stored
    }

    @Override
    public synchronized int getLockTableValue(int address) throws MyException {
        if (!this.elems.containsKey(address)) {
            throw new MyException("ERROR: Could not return lock value. The given address is not a key in the lock table.");
        }
        else {
            return this.elems.get(address);   // if we get here, the function call will always succeed
        }
    }

    @Override
    public synchronized void updateLockTableEntry(int address, int newValue) throws MyException {
        if (!this.elems.containsKey(address)) {
            throw new MyException("ERROR: Could not update lock entry. The given address is not a key in the lock table");
        }
        else {
            this.elems.put(address, newValue);
        }
    }

    @Override
    public synchronized boolean isDefined(int address) {
        return this.elems.containsKey(address);
    }

    @Override
    public synchronized void setContent(Map<Integer, Integer> newContent) {
        this.elems.clear();
        this.elems.putAll(newContent);
    }

    @Override
    public synchronized Map<Integer, Integer> getContent() {
        return this.elems;
    }

    @Override
    public synchronized String toString() {
        StringBuilder elemsInString = new StringBuilder();
        int i = 0;
        for (Map.Entry<Integer, Integer> entry: this.elems.entrySet()) {
            int key = entry.getKey();
            int value = entry.getValue();
            elemsInString.append(key);
            elemsInString.append(" --> ");
            elemsInString.append(value);
            if (i < this.elems.size() - 1) {
                elemsInString.append("\n");
            }
            i += 1;
        }
        return elemsInString.toString();
    }
}
