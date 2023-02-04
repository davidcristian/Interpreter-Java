package com.davidcristian.interpreter.Model.Statement;

import com.davidcristian.interpreter.Exceptions.ADTException;
import com.davidcristian.interpreter.Exceptions.ExpressionEvaluationException;
import com.davidcristian.interpreter.Exceptions.StatementExecutionException;
import com.davidcristian.interpreter.Model.ADT.Dictionary.IDictionary;
import com.davidcristian.interpreter.Model.Expression.IExpression;
import com.davidcristian.interpreter.Model.Program.ProgramState;
import com.davidcristian.interpreter.Model.Type.IType;
import com.davidcristian.interpreter.Model.Type.StringType;
import com.davidcristian.interpreter.Model.Value.IValue;
import com.davidcristian.interpreter.Model.Value.StringValue;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;

public class OpenFileStatement implements IStatement {
    private IExpression expression;

    public OpenFileStatement(IExpression expression) {
        this.expression = expression;
    }

    @Override
    public ProgramState execute(ProgramState state) throws StatementExecutionException, ExpressionEvaluationException, ADTException {
        IValue result = this.expression.evaluate(state.getSymbolTable(), state.getHeap());

        if (!result.getType().equals(new StringType()))
            throw new StatementExecutionException(String.format("Expression %s does not evaluate to a string!", this.expression.toString()));

        StringValue expressionValue = (StringValue)result;
        String fileName = expressionValue.getValue();
        IDictionary<String, BufferedReader> fileTable = state.getFileTable();

        if (fileTable.isDefined(fileName))
            throw new StatementExecutionException(String.format("File %s is already open!", fileName));

        BufferedReader fileDescriptor;
        try {
            fileDescriptor = new BufferedReader(new FileReader(fileName));
        } catch (FileNotFoundException e) {
            throw new StatementExecutionException(String.format("File %s could not be opened!", fileName));
        }

        fileTable.put(fileName, fileDescriptor);
        state.setFileTable(fileTable);

        return null;
    }

    @Override
    public IDictionary<String, IType> typeCheck(IDictionary<String, IType> typeEnv) throws StatementExecutionException, ExpressionEvaluationException, ADTException {
        IType typeExpression = this.expression.typeCheck(typeEnv);

        if (!typeExpression.equals(new StringType()))
            throw new StatementExecutionException(String.format("[%s] Expression %s does not evaluate to a string!", this.getClass().getSimpleName(), this.expression.toString()));

        return typeEnv;
    }

    @Override
    public IStatement deepCopy() {
        return new OpenFileStatement(this.expression.deepCopy());
    }

    @Override
    public String toString() {
        return "openRFile(" + this.expression.toString() + ")";
    }
}
