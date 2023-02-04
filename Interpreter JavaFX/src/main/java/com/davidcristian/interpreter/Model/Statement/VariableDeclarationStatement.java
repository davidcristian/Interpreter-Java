package com.davidcristian.interpreter.Model.Statement;

import com.davidcristian.interpreter.Exceptions.ADTException;
import com.davidcristian.interpreter.Exceptions.ExpressionEvaluationException;
import com.davidcristian.interpreter.Exceptions.StatementExecutionException;
import com.davidcristian.interpreter.Model.ADT.Dictionary.IDictionary;
import com.davidcristian.interpreter.Model.Program.ProgramState;
import com.davidcristian.interpreter.Model.Type.IType;
import com.davidcristian.interpreter.Model.Value.IValue;

public class VariableDeclarationStatement implements IStatement {
    private String name;
    private IType type;

    public VariableDeclarationStatement(String name, IType type) {
        this.name = name;
        this.type = type;
    }

    @Override
    public ProgramState execute(ProgramState state) throws StatementExecutionException {
        IDictionary<String, IValue> symbolTable = state.getSymbolTable();
        if (symbolTable.isDefined(this.name))
            throw new StatementExecutionException(String.format("Variable %s is already defined!", this.name));

        symbolTable.put(this.name, this.type.defaultValue());
        state.setSymbolTable(symbolTable);

        return null;
    }

    @Override
    public IDictionary<String, IType> typeCheck(IDictionary<String, IType> typeEnv) throws StatementExecutionException, ExpressionEvaluationException, ADTException {
        typeEnv.put(this.name, this.type);
        return typeEnv;
    }

    @Override
    public IStatement deepCopy() {
        return new VariableDeclarationStatement(this.name, this.type.deepCopy());
    }

    @Override
    public String toString() {
        return this.type.toString() + " " + this.name;
    }
}
