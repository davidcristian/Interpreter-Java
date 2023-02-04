package Model.Statement;

import Exceptions.ADTException;
import Exceptions.ExpressionEvaluationException;
import Exceptions.StatementExecutionException;
import Model.ADT.Dictionary.IDictionary;
import Model.Expression.IExpression;
import Model.Program.ProgramState;
import Model.Type.IType;
import Model.Type.StringType;
import Model.Value.IValue;
import Model.Value.StringValue;

import java.io.BufferedReader;
import java.io.IOException;

public class CloseFileStatement implements IStatement {
    private IExpression expression;

    public CloseFileStatement(IExpression expression) {
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

        if (!fileTable.isDefined(fileName))
            throw new StatementExecutionException(String.format("File %s is not open!", fileName));

        BufferedReader fileDescriptor = fileTable.lookUp(fileName);
        try {
            fileDescriptor.close();
        } catch (IOException e) {
            throw new StatementExecutionException(String.format("File %s could not be closed!", fileName));
        }

        fileTable.remove(fileName);
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
        return new CloseFileStatement(this.expression.deepCopy());
    }

    @Override
    public String toString() {
        return "closeRFile(" + this.expression.toString() + ")";
    }
}
