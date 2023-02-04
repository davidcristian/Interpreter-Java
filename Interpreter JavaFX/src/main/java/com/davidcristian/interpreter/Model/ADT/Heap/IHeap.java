package com.davidcristian.interpreter.Model.ADT.Heap;

import com.davidcristian.interpreter.Exceptions.ADTException;

import java.util.HashMap;
import java.util.Set;

public interface IHeap<V> {
    HashMap<Integer, V> getHeap();
    void setHeap(HashMap<Integer, V> heap);

    Set<Integer> keySet();
    boolean containsKey(Integer key);
    Integer allocate(V value);
    V get(Integer key) throws ADTException;

    void put(Integer key, V value);
    void update(Integer key, V value) throws ADTException;
    void remove(Integer key) throws ADTException;
}
