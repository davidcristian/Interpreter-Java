package com.davidcristian.interpreter.Model.Expression;

import com.davidcristian.interpreter.Exceptions.ADTException;
import com.davidcristian.interpreter.Exceptions.ExpressionEvaluationException;
import com.davidcristian.interpreter.Model.ADT.Dictionary.IDictionary;
import com.davidcristian.interpreter.Model.ADT.Heap.IHeap;
import com.davidcristian.interpreter.Model.Type.IType;
import com.davidcristian.interpreter.Model.Value.IValue;

public class VariableExpression implements IExpression {
    private String key;

    public VariableExpression(String id) {
        this.key = id;
    }

    public String getKey() {
        return key;
    }

    @Override
    public IValue evaluate(IDictionary<String, IValue> symbolTable, IHeap<IValue> heap) throws ADTException {
        return symbolTable.lookUp(key);
    }

    @Override
    public IType typeCheck(IDictionary<String, IType> typeEnv) throws ExpressionEvaluationException, ADTException {
        return typeEnv.lookUp(key);
    }

    @Override
    public IExpression deepCopy() {
        return new VariableExpression(key);
    }

    @Override
    public String toString() {
        return this.key;
    }
}
