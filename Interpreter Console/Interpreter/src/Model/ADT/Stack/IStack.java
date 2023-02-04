package Model.ADT.Stack;

import Exceptions.ADTException;

import java.util.List;

public interface IStack<T> {
    List<T> getElements();
    boolean isEmpty();

    T peek() throws ADTException;
    void push(T element);
    T pop() throws ADTException;
}
