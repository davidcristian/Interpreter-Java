package com.davidcristian.interpreter.Model.Statement;

import com.davidcristian.interpreter.Exceptions.ADTException;
import com.davidcristian.interpreter.Exceptions.ExpressionEvaluationException;
import com.davidcristian.interpreter.Exceptions.StatementExecutionException;
import com.davidcristian.interpreter.Model.ADT.Dictionary.IDictionary;
import com.davidcristian.interpreter.Model.ADT.Stack.IStack;
import com.davidcristian.interpreter.Model.Expression.IExpression;
import com.davidcristian.interpreter.Model.Program.ProgramState;
import com.davidcristian.interpreter.Model.Type.BoolType;
import com.davidcristian.interpreter.Model.Type.IType;
import com.davidcristian.interpreter.Model.Value.BoolValue;
import com.davidcristian.interpreter.Model.Value.IValue;

public class IfStatement implements IStatement {
    private IExpression expression;
    IStatement thenStatement;
    IStatement elseStatement;

    public IfStatement(IExpression expression, IStatement thenStatement, IStatement elseStatement) {
        this.expression = expression;
        this.thenStatement = thenStatement;
        this.elseStatement = elseStatement;
    }

    @Override
    public ProgramState execute(ProgramState state) throws StatementExecutionException, ADTException, ExpressionEvaluationException {
        IValue result = this.expression.evaluate(state.getSymbolTable(), state.getHeap());

        if (!(result instanceof BoolValue boolResult))
            throw new StatementExecutionException("Expression is not a boolean!");

        IStatement statement;
        if (boolResult.getValue()) {
            statement = thenStatement;
        }
        else {
            statement = elseStatement;
        }

        IStack<IStatement> stack = state.getExecutionStack();
        stack.push(statement);
        state.setExecutionStack(stack);

        return null;
    }

    @Override
    public IDictionary<String, IType> typeCheck(IDictionary<String, IType> typeEnv) throws StatementExecutionException, ExpressionEvaluationException, ADTException {
        IType typeExpression = this.expression.typeCheck(typeEnv);

        if (!typeExpression.equals(new BoolType()))
            throw new StatementExecutionException(String.format("[%s] The condition of the if statement is not a boolean!", this.getClass().getSimpleName()));

        this.thenStatement.typeCheck(typeEnv.deepCopy());
        this.elseStatement.typeCheck(typeEnv.deepCopy());
        return typeEnv;
    }

    @Override
    public IStatement deepCopy() {
        return new IfStatement(this.expression.deepCopy(), this.thenStatement.deepCopy(), this.elseStatement.deepCopy());
    }

    @Override
    public String toString() {
        return "if(" + this.expression.toString() + ")" + " then " + this.thenStatement.toString() + ", else "  + this.elseStatement.toString();
    }
}
