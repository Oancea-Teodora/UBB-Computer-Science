package toy_interpreter.lab11_project.Model.ADT;

import toy_interpreter.lab11_project.Model.Exceptions.MyException;

import java.util.HashMap;
import java.util.Map;

public class MyHeapTable<V> implements IHeapTable<V>{
    private HashMap<Integer, V> elems;
    private int nextFreeLocation;

    public MyHeapTable() {
        this.elems = new HashMap<Integer, V>();
        this.nextFreeLocation = 1;
    }

    @Override
    public synchronized int addNewHeapEntry(V value) {
        this.elems.put(this.nextFreeLocation, value);
        this.nextFreeLocation = this.nextFreeLocation + 1;
        return this.nextFreeLocation - 1;   // returns the address on which the value was stored
    }

    @Override
    public synchronized V getHeapValue(int address) throws MyException {
        if (!this.elems.containsKey(address)) {
            throw new MyException("ERROR: Could not return heap value. The given address is not a key in the heap table.");
        }
        else {
            return this.elems.get(address);   // if we get here, the function call will always succeed
        }
    }

    @Override
    public synchronized void updateHeapEntry(int address, V newValue) throws MyException {
        if (!this.elems.containsKey(address)) {
            throw new MyException("ERROR: Could not update heap entry. The given address is not a key in the heap table");
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
    public synchronized void setContent(Map<Integer, V> newContent) {
        this.elems.clear();
        this.elems.putAll(newContent);
    }

    @Override
    public synchronized Map<Integer, V> getContent() {
        return this.elems;
    }

    @Override
    public synchronized String toString() {
        StringBuilder elemsInString = new StringBuilder();
        int i = 0;
        for (Map.Entry<Integer, V> entry: this.elems.entrySet()) {
            int key = entry.getKey();
            V value = entry.getValue();
            elemsInString.append(key);
            elemsInString.append(" --> ");
            elemsInString.append(value.toString());
            if (i < this.elems.size() - 1) {
                elemsInString.append("\n");
            }
            i += 1;
        }
        return elemsInString.toString();
    }
}
