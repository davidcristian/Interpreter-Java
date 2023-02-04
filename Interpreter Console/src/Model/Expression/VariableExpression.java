package Model.Expression;

import Exceptions.ADTException;
import Exceptions.ExpressionEvaluationException;
import Model.ADT.Dictionary.IDictionary;
import Model.ADT.Heap.IHeap;
import Model.Type.IType;
import Model.Value.IValue;

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
