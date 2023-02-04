package com.davidcristian.interpreter.Model.Statement;

import com.davidcristian.interpreter.Exceptions.ADTException;
import com.davidcristian.interpreter.Exceptions.ExpressionEvaluationException;
import com.davidcristian.interpreter.Exceptions.StatementExecutionException;
import com.davidcristian.interpreter.Model.ADT.Dictionary.IDictionary;
import com.davidcristian.interpreter.Model.ADT.Stack.IStack;
import com.davidcristian.interpreter.Model.Program.ProgramState;
import com.davidcristian.interpreter.Model.Type.IType;

public class CompoundStatement implements IStatement {
    private IStatement firstStatement;
    private IStatement secondStatement;

    public CompoundStatement(IStatement firstStatement, IStatement secondStatement) {
        this.firstStatement = firstStatement;
        this.secondStatement = secondStatement;
    }

    @Override
    public ProgramState execute(ProgramState state) {
        IStack<IStatement> stack = state.getExecutionStack();

        stack.push(secondStatement);
        stack.push(firstStatement);

        state.setExecutionStack(stack);

        return null;
    }

    @Override
    public IDictionary<String, IType> typeCheck(IDictionary<String, IType> typeEnv) throws StatementExecutionException, ExpressionEvaluationException, ADTException {
        return this.secondStatement.typeCheck(this.firstStatement.typeCheck(typeEnv));
    }

    @Override
    public IStatement deepCopy() {
        return new CompoundStatement(this.firstStatement.deepCopy(), this.secondStatement.deepCopy());
    }

    @Override
    public String toString() {
        return this.firstStatement.toString() + "; " + this.secondStatement.toString();
    }
}
