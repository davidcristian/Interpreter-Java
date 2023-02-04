package com.davidcristian.interpreter.Model.Statement;

import com.davidcristian.interpreter.Exceptions.ADTException;
import com.davidcristian.interpreter.Exceptions.ExpressionEvaluationException;
import com.davidcristian.interpreter.Exceptions.StatementExecutionException;
import com.davidcristian.interpreter.Model.ADT.Dictionary.IDictionary;
import com.davidcristian.interpreter.Model.Expression.IExpression;
import com.davidcristian.interpreter.Model.Program.ProgramState;
import com.davidcristian.interpreter.Model.Type.IType;
import com.davidcristian.interpreter.Model.Type.IntType;
import com.davidcristian.interpreter.Model.Type.StringType;
import com.davidcristian.interpreter.Model.Value.IValue;
import com.davidcristian.interpreter.Model.Value.IntValue;
import com.davidcristian.interpreter.Model.Value.StringValue;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Objects;

public class ReadFileStatement implements IStatement {
    private IExpression expression;
    private String variableName;

    public ReadFileStatement(IExpression expression, String variableName) {
        this.expression = expression;
        this.variableName = variableName;
    }

    @Override
    public ProgramState execute(ProgramState state) throws StatementExecutionException, ExpressionEvaluationException, ADTException {
        IDictionary<String, IValue> symbolTable = state.getSymbolTable();
        IDictionary<String, BufferedReader> fileTable =state.getFileTable();

        if (!symbolTable.isDefined(this.variableName))
            throw new StatementExecutionException(String.format("Variable %s is not defined!", this.variableName));

        IValue value = symbolTable.lookUp(this.variableName);
        if (!value.getType().equals(new IntType()))
            throw new StatementExecutionException(String.format("Variable %s is not an integer!", this.variableName));

        value = this.expression.evaluate(symbolTable, state.getHeap());
        if (!value.getType().equals(new StringType()))
            throw new StatementExecutionException(String.format("Expression %s does not evaluate to a string!", this.expression.toString()));

        StringValue stringValue = (StringValue)value;
        String fileName = stringValue.getValue();
        if (!fileTable.isDefined(fileName))
            throw new StatementExecutionException(String.format("File %s is not open!", fileName));

        BufferedReader fileDescriptor = fileTable.lookUp(fileName);
        try {
            String line = fileDescriptor.readLine();
            if (line == null || Objects.equals(line, ""))
                line = "0";

            symbolTable.put(this.variableName, new IntValue(Integer.parseInt(line)));
        } catch (IOException e) {
            throw new StatementExecutionException(String.format("Could not read from file %s!", fileName));
        }

        return null;
    }

    @Override
    public IDictionary<String, IType> typeCheck(IDictionary<String, IType> typeEnv) throws StatementExecutionException, ExpressionEvaluationException, ADTException {
        IType typeVariable = typeEnv.lookUp(this.variableName);
        IType typeExpression = this.expression.typeCheck(typeEnv);

        if (!typeExpression.equals(new StringType()))
            throw new StatementExecutionException(String.format("[%s] Expression %s does not evaluate to a string!", this.getClass().getSimpleName(), this.expression.toString()));

        if (!typeVariable.equals(new IntType()))
            throw new StatementExecutionException(String.format("[%s] Variable %s is not an integer!", this.getClass().getSimpleName(), this.variableName));

        return typeEnv;
    }

    @Override
    public IStatement deepCopy() {
        return new ReadFileStatement(this.expression.deepCopy(), this.variableName);
    }

    @Override
    public String toString() {
        return "readFile(" + this.expression.toString() + ", " + this.variableName + ")";
    }
}
