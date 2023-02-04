package com.davidcristian.interpreter.Model.ADT.Stack;

import com.davidcristian.interpreter.Exceptions.ADTException;

import java.util.*;

public class MyStack<T> implements IStack<T> {
    private final Stack<T> stack;

    public MyStack() {
        this.stack = new Stack<>();
    }

    @Override
    public List<T> getElements() {
        List<T> elements = new ArrayList<>(this.stack);
        Collections.reverse(elements);

        return elements;
    }

    @Override
    public boolean isEmpty() {
        return this.stack.isEmpty();
    }

    @Override
    public T peek() throws ADTException {
        if (this.isEmpty())
            throw new ADTException("The stack is empty!");

        return this.stack.peek();
    }

    @Override
    public void push(T element) {
        this.stack.push(element);
    }

    @Override
    public T pop() throws ADTException {
        if (this.isEmpty())
            throw new ADTException("The stack is empty!");

        return this.stack.pop();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("{");

        for (T element: this.stack) {
            sb.append(element.toString()).append(" | ");
        }

        if (sb.length() > 3) {
            sb.setLength(sb.length() - 3);
        }

        sb.append("}");
        return sb.toString();
    }
}
