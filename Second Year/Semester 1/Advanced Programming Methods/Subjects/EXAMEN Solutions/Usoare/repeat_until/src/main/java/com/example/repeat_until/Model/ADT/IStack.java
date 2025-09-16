package com.example.repeat_until.Model.ADT;

import java.util.Stack;

public interface IStack<T> {
    void push(T newElem);
    T pop();
    T peek();
    boolean isEmpty();
    Stack<T> getContent();
}
