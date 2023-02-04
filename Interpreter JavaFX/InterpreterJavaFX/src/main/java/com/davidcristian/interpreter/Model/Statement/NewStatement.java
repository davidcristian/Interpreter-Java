package com.davidcristian.interpreter.Model.Statement;

import com.davidcristian.interpreter.Exceptions.ADTException;
import com.davidcristian.interpreter.Exceptions.ExpressionEvaluationException;
import com.davidcristian.interpreter.Exceptions.StatementExecutionException;
import com.davidcristian.interpreter.Model.ADT.Dictionary.IDictionary;
import com.davidcristian.interpreter.Model.ADT.Heap.IHeap;
import com.davidcristian.interpreter.Model.Expression.IExpression;
import com.davidcristian.interpreter.Model.Program.ProgramState;
import com.davidcristian.interpreter.Model.Type.IType;
import com.davidcristian.interpreter.Model.Type.ReferenceType;
import com.davidcristian.interpreter.Model.Value.IValue;
import com.davidcristian.interpreter.Model.Value.ReferenceValue;

public class NewStatement implements IStatement {
    private final String variableName;
    private final IExpression expression;

    public NewStatement(String variableName, IExpression expression) {
        this.variableName = variableName;
        this.expression = expression;
    }

    @Override
    public ProgramState execute(ProgramState state) throws StatementExecutionException, ExpressionEvaluationException, ADTException {
        IDictionary<String, IValue> symbolTable = state.getSymbolTable();
        IHeap<IValue> heap = state.getHeap();

        if (!symbolTable.isDefined(this.variableName))
            throw new StatementExecutionException(String.format("Variable %s is not defined!", this.variableName));

        IValue value = symbolTable.lookUp(this.variableName);
        if (!(value instanceof ReferenceValue))
            throw new StatementExecutionException(String.format("Variable %s is not a reference!", this.variableName));

        IValue expressionValue = this.expression.evaluate(state.getSymbolTable(), state.getHeap());
        IType locationType = ((ReferenceValue)value).getLocationType();
        if (!locationType.equals(expressionValue.getType()))
            throw new StatementExecutionException(String.format("Variable %s is of type %s and cannot be assigned value %s of type %s!", this.variableName, locationType, expressionValue, expressionValue.getType()));

        int location = state.getHeap().allocate(expressionValue);
        state.getSymbolTable().update(this.variableName, new ReferenceValue(location, expressionValue.getType()));
        state.setSymbolTable(symbolTable);
        state.setHeap(heap);

        return null;
    }

    @Override
    public IDictionary<String, IType> typeCheck(IDictionary<String, IType> typeEnv) throws StatementExecutionException, ExpressionEvaluationException, ADTException {
        IType typeVariable = typeEnv.lookUp(this.variableName);
        IType typeExpression = this.expression.typeCheck(typeEnv);

        if (!typeVariable.equals(new ReferenceType(typeExpression)))
            throw new StatementExecutionException(String.format("[%s] Variable %s is of type %s and cannot be assigned value %s of type %s!", this.getClass().getSimpleName(), this.variableName, typeVariable, this.expression.toString(), typeExpression));

        return typeEnv;
    }

    @Override
    public IStatement deepCopy() {
        return new NewStatement(this.variableName, this.expression.deepCopy());
    }

    @Override
    public String toString() {
        return "new(" + this.variableName + ", " + this.expression + ")";
    }
}
