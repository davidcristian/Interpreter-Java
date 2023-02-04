package com.davidcristian.interpreter.Model.ADT.List;

import com.davidcristian.interpreter.Exceptions.ADTException;

import java.util.List;

public interface IList<T> {
    List<T> getList();

    int size();
    boolean isEmpty();
    T get(int index) throws ADTException;

    void add(T element);
    void remove(T element) throws ADTException;
    void removeIndex(int index) throws ADTException;
}
