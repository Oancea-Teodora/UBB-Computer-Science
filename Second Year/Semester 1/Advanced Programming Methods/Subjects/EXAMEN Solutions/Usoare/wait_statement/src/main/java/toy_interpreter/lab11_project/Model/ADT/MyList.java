package toy_interpreter.lab11_project.Model.ADT;

import toy_interpreter.lab11_project.Model.Exceptions.MyException;

import java.util.ArrayList;

public class MyList<T> implements IList<T>{
    private ArrayList<T> elems;

    public MyList() {
        this.elems = new ArrayList<T>();
    }

    @Override
    public synchronized void add(T newElem) {
        this.elems.add(newElem);
    }

    @Override
    public synchronized ArrayList<T> getElems() {
        return this.elems;
    }

    @Override
    public synchronized T getElemAtIndex(int index) throws MyException {
        if (index >= this.elems.size()) {
            throw new MyException("Failed to get element: the given index is too large.");
        }
        return this.elems.get(index);
    }

    @Override
    public synchronized int size() {
        return this.elems.size();
    }

    @Override
    public synchronized String toString() {
        StringBuilder elemsInString = new StringBuilder();
        for (int i = 0; i < this.elems.size(); i++) {
            elemsInString.append(this.elems.get(i).toString());
            if (i < this.elems.size() - 1) {
                elemsInString.append("\n");
            }
        }
        return elemsInString.toString();
    }
}
