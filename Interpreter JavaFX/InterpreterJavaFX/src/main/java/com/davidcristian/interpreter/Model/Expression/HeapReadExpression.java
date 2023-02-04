package com.davidcristian.interpreter.Model.Expression;

import com.davidcristian.interpreter.Exceptions.ADTException;
import com.davidcristian.interpreter.Exceptions.ExpressionEvaluationException;
import com.davidcristian.interpreter.Model.ADT.Dictionary.IDictionary;
import com.davidcristian.interpreter.Model.ADT.Heap.IHeap;
import com.davidcristian.interpreter.Model.Type.IType;
import com.davidcristian.interpreter.Model.Type.ReferenceType;
import com.davidcristian.interpreter.Model.Value.IValue;
import com.davidcristian.interpreter.Model.Value.ReferenceValue;

public class HeapReadExpression implements IExpression {
    private IExpression expression;

    public HeapReadExpression(IExpression expression) {
        this.expression = expression;
    }

    @Override
    public IValue evaluate(IDictionary<String, IValue> symbolTable, IHeap<IValue> heap) throws ExpressionEvaluationException, ADTException {
        IValue result = this.expression.evaluate(symbolTable, heap);

        if (!(result instanceof ReferenceValue refValue))
            throw new ExpressionEvaluationException(String.format("The expression %s is not a reference!", this.expression));

        IValue valueFromHeap = heap.get(refValue.getAddress());
        if (valueFromHeap == null)
            throw new ExpressionEvaluationException(String.format("The address %s is not defined in the heap!", refValue.getAddress()));

        return valueFromHeap;
    }

    @Override
    public IType typeCheck(IDictionary<String, IType> typeEnv) throws ExpressionEvaluationException, ADTException {
        IType typeExpression = this.expression.typeCheck(typeEnv);
        if (!(typeExpression instanceof ReferenceType referenceType))
            throw new ExpressionEvaluationException(String.format("[%s] The type of the expression %s is not a reference type!", this.getClass().getSimpleName(), this.expression));

        return referenceType.getInner();
    }

    @Override
    public IExpression deepCopy() {
        return new HeapReadExpression(this.expression.deepCopy());
    }

    @Override
    public String toString() {
        return "rH(" + this.expression + ")";
    }
}
