package com.davidcristian.interpreter.Model.Statement;

import com.davidcristian.interpreter.Exceptions.ADTException;
import com.davidcristian.interpreter.Exceptions.ExpressionEvaluationException;
import com.davidcristian.interpreter.Exceptions.StatementExecutionException;
import com.davidcristian.interpreter.Model.ADT.Dictionary.IDictionary;
import com.davidcristian.interpreter.Model.ADT.Dictionary.MyDictionary;
import com.davidcristian.interpreter.Model.ADT.Stack.IStack;
import com.davidcristian.interpreter.Model.ADT.Stack.MyStack;
import com.davidcristian.interpreter.Model.Program.ProgramState;
import com.davidcristian.interpreter.Model.Type.IType;
import com.davidcristian.interpreter.Model.Value.IValue;

import java.util.Map;

public class ForkStatement implements IStatement {
    private IStatement statement;

    public ForkStatement(IStatement statement) {
        this.statement = statement;
    }

    public IStatement getStatement() {
        return statement;
    }

    public void setStatement(IStatement statement) {
        this.statement = statement;
    }

    @Override
    public ProgramState execute(ProgramState state) throws StatementExecutionException, ExpressionEvaluationException, ADTException {
        IStack<IStatement> newStack = new MyStack<>();
        newStack.push(this.statement);

        IDictionary<String, IValue> newSymbolTable = new MyDictionary<>();
        for (Map.Entry<String, IValue> entry: state.getSymbolTable().getDictionary().entrySet()) {
            newSymbolTable.put(entry.getKey(), entry.getValue().deepCopy());
        }

        return new ProgramState(newStack, newSymbolTable, state.getOutput(), state.getFileTable(), state.getHeap());
    }

    @Override
    public IDictionary<String, IType> typeCheck(IDictionary<String, IType> typeEnv) throws StatementExecutionException, ExpressionEvaluationException, ADTException {
        this.statement.typeCheck(typeEnv.deepCopy());
        return typeEnv;
    }

    @Override
    public IStatement deepCopy() {
        return new ForkStatement(this.statement.deepCopy());
    }

    @Override
    public String toString() {
        return "fork(" + this.statement.toString() + ")";
    }

}
