package com.davidcristian.interpreter.Model.Expression;

import com.davidcristian.interpreter.Exceptions.ADTException;
import com.davidcristian.interpreter.Exceptions.ExpressionEvaluationException;
import com.davidcristian.interpreter.Model.ADT.Dictionary.IDictionary;
import com.davidcristian.interpreter.Model.ADT.Heap.IHeap;
import com.davidcristian.interpreter.Model.Type.IType;
import com.davidcristian.interpreter.Model.Value.IValue;

public interface IExpression {
    IValue evaluate(IDictionary<String, IValue> symbolTable, IHeap<IValue> heap) throws ExpressionEvaluationException, ADTException;
    IType typeCheck(IDictionary<String, IType> typeEnv) throws ExpressionEvaluationException, ADTException;

    IExpression deepCopy();
}
