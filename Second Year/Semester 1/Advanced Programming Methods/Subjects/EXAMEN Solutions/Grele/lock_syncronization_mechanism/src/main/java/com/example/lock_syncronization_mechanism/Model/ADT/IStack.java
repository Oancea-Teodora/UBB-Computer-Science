package com.example.lock_syncronization_mechanism.Model.ADT;

import java.util.Stack;

public interface IStack<T> {
    void push(T newElem);
    T pop();
    T peek();
    boolean isEmpty();
    Stack<T> getContent();
}
