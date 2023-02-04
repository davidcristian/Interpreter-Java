package com.davidcristian.interpreter.Model.ADT.List;

import com.davidcristian.interpreter.Exceptions.ADTException;

import java.util.ArrayList;
import java.util.List;

public class MyList<T> implements IList<T> {
    private final List<T> list;

    public MyList() {
        this.list = new ArrayList<>();
    }

    @Override
    public List<T> getList() {
        return this.list;
    }

    @Override
    public int size() {
        return this.list.size();
    }

    @Override
    public boolean isEmpty() {
        return this.list.isEmpty();
    }

    @Override
    public T get(int index) throws ADTException {
        if (index < 0 || index >= this.list.size())
            throw new ADTException(String.format("The index %d is out of bounds!", index));

        return this.list.get(index);
    }

    @Override public void add(T element) {
        this.list.add(element);
    }

    @Override
    public void remove(T element) throws ADTException {
        if (!this.list.contains(element))
            throw new ADTException(String.format("The element %s is not in the list!", element));

        this.list.remove(element);
    }

    @Override
    public void removeIndex(int index) throws ADTException {
        if (index < 0 || index >= this.list.size())
            throw new ADTException(String.format("The index %d is out of bounds!", index));

        this.list.remove(index);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("{");

        for (T element : this.list) {
            sb.append(element.toString()).append(", ");
        }

        if (sb.length() > 2) {
            sb.setLength(sb.length() - 2);
        }

        sb.append("}");
        return sb.toString();
    }
}
