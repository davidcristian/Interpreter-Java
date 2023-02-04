package Model.Expression;

import Exceptions.ADTException;
import Exceptions.ExpressionEvaluationException;
import Model.ADT.Dictionary.IDictionary;
import Model.ADT.Heap.IHeap;
import Model.Type.IType;
import Model.Type.IntType;
import Model.Value.IValue;
import Model.Value.IntValue;

public class ArithmeticExpression implements IExpression {
    private char operation;
    private IExpression firstExpression;
    private IExpression secondExpression;

    public ArithmeticExpression(char operation, IExpression expression1, IExpression expression2) {
        this.operation = operation;
        this.firstExpression = expression1;
        this.secondExpression = expression2;
    }

    public char getOperation() {
        return operation;
    }

    public IExpression getFirstExpression() {
        return firstExpression;
    }

    public IExpression getSecondExpression() {
        return secondExpression;
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

        switch (this.operation) {
            case '+' -> {
                return new IntValue(firstNumber + secondNumber);
            }
            case '-' -> {
                return new IntValue(firstNumber - secondNumber);
            }
            case '*' -> {
                return new IntValue(firstNumber * secondNumber);
            }
            case '/' -> {
                if (secondNumber == 0)
                    throw new ExpressionEvaluationException("Division by zero!");

                return new IntValue(firstNumber / secondNumber);
            }
            default -> throw new ExpressionEvaluationException("Invalid operation!");
        }
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

        return new IntType();
    }

    @Override
    public IExpression deepCopy() {
        return new ArithmeticExpression(this.operation, this.firstExpression.deepCopy(), this.secondExpression.deepCopy());
    }

    @Override
    public String toString() {
        return this.firstExpression.toString() + " " + this.operation + " " + this.secondExpression.toString();
    }
}
