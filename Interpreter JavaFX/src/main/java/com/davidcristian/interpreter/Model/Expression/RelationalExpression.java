package com.davidcristian.interpreter.Model.Expression;

import com.davidcristian.interpreter.Exceptions.ADTException;
import com.davidcristian.interpreter.Exceptions.ExpressionEvaluationException;
import com.davidcristian.interpreter.Model.ADT.Dictionary.IDictionary;
import com.davidcristian.interpreter.Model.ADT.Heap.IHeap;
import com.davidcristian.interpreter.Model.Type.BoolType;
import com.davidcristian.interpreter.Model.Type.IType;
import com.davidcristian.interpreter.Model.Type.IntType;
import com.davidcristian.interpreter.Model.Value.BoolValue;
import com.davidcristian.interpreter.Model.Value.IValue;
import com.davidcristian.interpreter.Model.Value.IntValue;

public class RelationalExpression implements IExpression {
    private IExpression firstExpression;
    private IExpression secondExpression;
    private String operator;

    public RelationalExpression(String operation, IExpression firstExpression, IExpression secondExpression) {
        this.operator = operation;
        this.firstExpression = firstExpression;
        this.secondExpression = secondExpression;
    }

    public IExpression getFirstExpression() {
        return firstExpression;
    }

    public IExpression getSecondExpression() {
        return secondExpression;
    }

    public String getOperator() {
        return operator;
    }

    @Override
    public IValue evaluate(IDictionary<String, IValue> symbolTable, IHeap<IValue> heap) throws ExpressionEvaluationException, ADTException {
        IValue firstResult;
        firstResult = this.firstExpression.evaluate(symbolTable, heap);
        if (!firstResult.getType().equals(new IntType()))
            throw new ExpressionEvaluationException("The first operand is not an integer!");

        IValue secondResult;
        secondResult = this.secondExpression.evaluate(symbolTable, heap);
        if (!secondResult.getType().equals(new IntType()))
            throw new ExpressionEvaluationException("The second operand is not an integer!");

        IntValue intFirstResult = (IntValue)firstResult;
        IntValue intSecondResult = (IntValue)secondResult;
        int firstNumber = intFirstResult.getValue();
        int secondNumber = intSecondResult.getValue();

        return switch (this.operator) {
            case ">=" -> new BoolValue(firstNumber >= secondNumber);
            case ">" -> new BoolValue(firstNumber > secondNumber);
            case "<=" -> new BoolValue(firstNumber <= secondNumber);
            case "<" -> new BoolValue(firstNumber < secondNumber);
            case "==" -> new BoolValue(firstNumber == secondNumber);
            case "!=" -> new BoolValue(firstNumber != secondNumber);
            default -> throw new ExpressionEvaluationException("Invalid operator!");
        };
    }

    @Override
    public IType typeCheck(IDictionary<String, IType> typeEnv) throws ExpressionEvaluationException, ADTException {
        IType type1, type2;
        type1 = this.firstExpression.typeCheck(typeEnv);
        if (!type1.equals(new IntType()))
            throw new ExpressionEvaluationException(String.format("[%s] The first operand is not an integer!", this.getClass().getSimpleName()));

        type2 = this.secondExpression.typeCheck(typeEnv);
        if (!type2.equals(new IntType()))
            throw new ExpressionEvaluationException(String.format("[%s] The second operand is not an integer!", this.getClass().getSimpleName()));

        return new BoolType();
    }

    @Override
    public IExpression deepCopy() {
        return new RelationalExpression(this.operator, this.firstExpression.deepCopy(), this.secondExpression.deepCopy());
    }

    @Override
    public String toString() {
        return this.firstExpression.toString() + " " + this.operator + " " + this.secondExpression.toString();
    }
}
