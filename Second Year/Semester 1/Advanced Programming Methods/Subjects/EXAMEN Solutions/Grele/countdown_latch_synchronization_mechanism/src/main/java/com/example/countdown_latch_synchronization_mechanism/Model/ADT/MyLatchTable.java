package com.example.countdown_latch_synchronization_mechanism.Model.ADT;

import com.example.countdown_latch_synchronization_mechanism.Model.Exceptions.MyException;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class MyLatchTable implements ILatchTable{
    private HashMap<Integer, Integer> elems;
    private AtomicInteger nextFreeLocation;

    public MyLatchTable() {
        this.nextFreeLocation = new AtomicInteger(1);
        this.elems = new HashMap<>();
    }

    @Override
    public synchronized int addNewLatch(int counter) {
        int addressUsed = this.nextFreeLocation.getAndIncrement();
        this.elems.put(addressUsed, counter);
        return addressUsed;
    }

    @Override
    public synchronized int getLatchCounter(int latchAddress) throws MyException {
        if (!this.elems.containsKey(latchAddress)) {
            throw new MyException("ERROR: The address " + latchAddress + " is not an index in the Latch Table.");
        }
        return this.elems.get(latchAddress);
    }

    @Override
    public synchronized void decrementLatchCounter(int latchAddress) throws MyException {
        if (!this.elems.containsKey(latchAddress)) {
            throw new MyException("ERROR: The address " + latchAddress + " is not an index in the Latch Table.");
        }
        if (this.elems.get(latchAddress) == 0) {
            throw new MyException("ERROR: The counter of the latch at the address " + latchAddress + " already reached zero.");
        }
        this.elems.replace(latchAddress, this.elems.get(latchAddress) - 1);
    }

    @Override
    public synchronized boolean isLatchAddressUsed(int latchAddress) {
        return this.elems.containsKey(latchAddress);
    }

    @Override
    public synchronized Map<Integer, Integer> getContent() {
        return this.elems;
    }
}
