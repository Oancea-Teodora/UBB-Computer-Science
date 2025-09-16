package toy_interpreter.lab11_project.Model.ADT;

import java.util.ListIterator;
import java.util.Stack;

public class MyStack<T> implements IStack<T>{
    private Stack<T> elems;

    public MyStack() {
        this.elems = new Stack<T>();
    }

    @Override
    public void push(T newElem) {
        this.elems.push(newElem);
    }

    @Override
    public T pop() {
        return elems.pop();
    }

    @Override
    public T peek() {
        return this.elems.peek();
    }

    @Override
    public boolean isEmpty() {
        return this.elems.empty();
    }

    @Override
    public Stack<T> getContent() {
        return this.elems;
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        ListIterator<T> stackIterator = this.elems.listIterator(this.elems.size());
        while (stackIterator.hasPrevious()) {
            s.append(stackIterator.previous().toString()).append("\n");
        }
        if (!s.isEmpty()) {
            return s.deleteCharAt(s.length() - 1).toString();
        }
        else {
            return "";
        }
    }
}
