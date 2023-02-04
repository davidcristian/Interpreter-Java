package Model.Expression;

import Exceptions.ADTException;
import Exceptions.ExpressionEvaluationException;
import Model.ADT.Dictionary.IDictionary;
import Model.ADT.Heap.IHeap;
import Model.Type.BoolType;
import Model.Type.IType;
import Model.Value.BoolValue;
import Model.Value.IValue;

import java.util.Objects;

public class LogicExpression implements IExpression {
    private IExpression firstExpression;
    private IExpression secondExpression;
    private String operation;

    public LogicExpression(IExpression expression1, IExpression expression2, String operation) {
        this.firstExpression = expression1;
        this.secondExpression = expression2;
        this.operation = operation;
    }

    public IExpression getFirstExpression() {
        return firstExpression;
    }

    public IExpression getSecondExpression() {
        return secondExpression;
    }

    public String getOperation() {
        return operation;
    }

    @Override
    public IValue evaluate(IDictionary<String, IValue> symbolTable, IHeap<IValue> heap) throws ExpressionEvaluationException, ADTException {
        IValue firstResult;
        firstResult = this.firstExpression.evaluate(symbolTable, heap);
        if (!firstResult.getType().equals(new BoolType()))
            throw new ExpressionEvaluationException("The first operand is not a boolean!");

        IValue secondResult;
        secondResult = this.secondExpression.evaluate(symbolTable, heap);
        if (!secondResult.getType().equals(new BoolType()))
            throw new ExpressionEvaluationException("The second operand is not a boolean!");

        BoolValue boolFirstResult = (BoolValue)firstResult;
        BoolValue boolSecondResult = (BoolValue)secondResult;
        boolean bool1 = boolFirstResult.getValue();
        boolean bool2 = boolSecondResult.getValue();

        if (Objects.equals(this.operation, "and")) {
            return new BoolValue(bool1 && bool2);
        }

        if (Objects.equals(this.operation, "or")) {
            return new BoolValue(bool1 || bool2);
        }

        throw new ExpressionEvaluationException("Invalid operation!");
    }

    @Override
    public IType typeCheck(IDictionary<String, IType> typeEnv) throws ExpressionEvaluationException, ADTException {
        IType type1, type2;
        type1 = this.firstExpression.typeCheck(typeEnv);
        if (!type1.equals(new BoolType()))
            throw new ExpressionEvaluationException(String.format("[%s] The first operand is not a boolean!", this.getClass().getSimpleName()));

        type2 = this.secondExpression.typeCheck(typeEnv);
        if (!type2.equals(new BoolType()))
            throw new ExpressionEvaluationException(String.format("[%s] The second operand is not a boolean!", this.getClass().getSimpleName()));

        return new BoolType();
    }

    @Override
    public IExpression deepCopy() {
        return new LogicExpression(this.firstExpression.deepCopy(), this.secondExpression.deepCopy(), this.operation);
    }

    @Override
    public String toString() {
        return this.firstExpression.toString() + " " + this.operation + " " + this.secondExpression.toString();
    }
}
