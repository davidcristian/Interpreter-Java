package com.davidcristian.interpreter.Model.ADT.Stack;

import com.davidcristian.interpreter.Exceptions.ADTException;

import java.util.List;

public interface IStack<T> {
    List<T> getElements();
    boolean isEmpty();

    T peek() throws ADTException;
    void push(T element);
    T pop() throws ADTException;
}
