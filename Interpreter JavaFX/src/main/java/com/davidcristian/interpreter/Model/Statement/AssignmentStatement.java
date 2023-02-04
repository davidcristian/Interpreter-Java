package com.davidcristian.interpreter.Model.Statement;

import com.davidcristian.interpreter.Exceptions.ADTException;
import com.davidcristian.interpreter.Exceptions.StatementExecutionException;
import com.davidcristian.interpreter.Exceptions.ExpressionEvaluationException;
import com.davidcristian.interpreter.Model.ADT.Dictionary.IDictionary;
import com.davidcristian.interpreter.Model.Program.ProgramState;
import com.davidcristian.interpreter.Model.Expression.IExpression;
import com.davidcristian.interpreter.Model.Type.IType;
import com.davidcristian.interpreter.Model.Value.IValue;

public class AssignmentStatement implements IStatement {
    private String id;
    private IExpression expression;

    public AssignmentStatement(String id, IExpression expression) {
        this.id = id;
        this.expression = expression;
    }

    @Override
    public ProgramState execute(ProgramState state) throws StatementExecutionException, ExpressionEvaluationException, ADTException {
        IDictionary<String, IValue> symbolTable = state.getSymbolTable();

        if (!symbolTable.isDefined(this.id))
            throw new StatementExecutionException(String.format("Variable %s is not defined!", this.id));

        IValue value = this.expression.evaluate(symbolTable, state.getHeap());
        IType typeId = (symbolTable.lookUp(this.id)).getType();

        if (!value.getType().equals(typeId))
            throw new StatementExecutionException(String.format("Variable %s is of type %s and cannot be assigned value %s of type %s!", this.id, typeId, value, value.getType()));

        symbolTable.update(this.id, value);
        state.setSymbolTable(symbolTable);

        return null;
    }

    @Override
    public IDictionary<String, IType> typeCheck(IDictionary<String, IType> typeEnv) throws StatementExecutionException, ExpressionEvaluationException, ADTException {
        IType typeVariable = typeEnv.lookUp(this.id);
        IType typeExpression = this.expression.typeCheck(typeEnv);

        if (!typeVariable.equals(typeExpression))
            throw new StatementExecutionException(String.format("[%s] Variable %s is of type %s and cannot be assigned value %s of type %s!", this.getClass().getSimpleName(), this.id, typeVariable, this.expression.toString(), typeExpression));

        return typeEnv;
    }

    @Override
    public IStatement deepCopy() {
        return new AssignmentStatement(this.id, this.expression.deepCopy());
    }

    @Override
    public String toString() {
        return id + " = " + expression.toString();
    }
}
