package com.davidcristian.interpreter.Model.Program;

import com.davidcristian.interpreter.Exceptions.ADTException;
import com.davidcristian.interpreter.Exceptions.ExpressionEvaluationException;
import com.davidcristian.interpreter.Exceptions.StatementExecutionException;
import com.davidcristian.interpreter.Model.ADT.Dictionary.IDictionary;
import com.davidcristian.interpreter.Model.ADT.Heap.IHeap;
import com.davidcristian.interpreter.Model.ADT.List.IList;
import com.davidcristian.interpreter.Model.ADT.Stack.IStack;
import com.davidcristian.interpreter.Model.Statement.IStatement;
import com.davidcristian.interpreter.Model.Value.IValue;

import java.io.BufferedReader;
import java.util.Objects;

public class ProgramState {
    private IStack<IStatement> executionStack;
    private IDictionary<String, IValue> symbolTable;
    private IList<IValue> output;
    private IDictionary<String, BufferedReader> fileTable;
    private IHeap<IValue> heap;

    private IStatement originalProgram;
    private final int id;
    private static int lastID = 0;

    public ProgramState(IStack<IStatement> executionStack, IDictionary<String, IValue> symbolTable, IList<IValue> output, IDictionary<String, BufferedReader> fileTable, IHeap<IValue> heap, IStatement originalProgram) {
        this.originalProgram = originalProgram;

        this.executionStack = executionStack;
        this.symbolTable = symbolTable;
        this.output = output;
        this.fileTable = fileTable;
        this.heap = heap;
        this.executionStack.push(this.originalProgram.deepCopy());
        this.id = this.setID();
    }

    public ProgramState(IStack<IStatement> executionStack, IDictionary<String, IValue> symbolTable, IList<IValue> output, IDictionary<String, BufferedReader> fileTable, IHeap<IValue> heap) {
        this.executionStack = executionStack;
        this.symbolTable = symbolTable;
        this.output = output;
        this.fileTable = fileTable;
        this.heap = heap;
        this.id = this.setID();
    }

    public boolean isOriginal() throws ADTException {
        if (this.originalProgram == null)
            return false;

        return Objects.equals(this.executionStack.peek().toString(), this.originalProgram.toString());
    }

    public IStack<IStatement> getExecutionStack() {
        return executionStack;
    }

    public void setExecutionStack(IStack<IStatement> executionStack) {
        this.executionStack = executionStack;
    }

    public IDictionary<String, IValue> getSymbolTable() {
        return symbolTable;
    }

    public void setSymbolTable(IDictionary<String, IValue> symbolTable) {
        this.symbolTable = symbolTable;
    }

    public IList<IValue> getOutput() {
        return output;
    }

    public void setOutput(IList<IValue> output) {
        this.output = output;
    }

    public IDictionary<String, BufferedReader> getFileTable() {
        return fileTable;
    }

    public void setFileTable(IDictionary<String, BufferedReader> fileTable) {
        this.fileTable = fileTable;
    }

    public IHeap<IValue> getHeap() {
        return heap;
    }

    public void setHeap(IHeap<IValue> heap) {
        this.heap = heap;
    }

    public IStatement getOriginalProgram() {
        return originalProgram;
    }

    public int getID() {
        return id;
    }

    public synchronized int setID() {
        return ++lastID;
    }

    public boolean isNotCompleted() {
        return !this.executionStack.isEmpty();
    }

    public ProgramState oneStep() throws StatementExecutionException, ExpressionEvaluationException, ADTException {
        if (this.executionStack.isEmpty())
            throw new StatementExecutionException("The execution stack is empty!");

        IStatement currentStatement = this.executionStack.pop();
        return currentStatement.execute(this);
    }

    @Override
    public String toString() {
        return  "Program ID: " + this.id + "\n" +
                "Execution stack:\n" + this.executionStack.toString() + "\n" +
                "Symbol table:\n" + this.symbolTable.toString() + "\n" +
                "Output:\n" + this.output.toString() + "\n" +
                "File table:\n" + this.fileTable.toString() + "\n" +
                "Heap:\n" + this.heap.toString();
    }
}
