package Model.Statement;

import Exceptions.ADTException;
import Exceptions.ExpressionEvaluationException;
import Exceptions.StatementExecutionException;
import Model.ADT.Dictionary.IDictionary;
import Model.ADT.Stack.IStack;
import Model.Expression.IExpression;
import Model.Program.ProgramState;
import Model.Type.BoolType;
import Model.Type.IType;
import Model.Value.BoolValue;
import Model.Value.IValue;

public class WhileStatement implements IStatement {
    private IExpression expression;
    private IStatement statement;

    public WhileStatement(IExpression expression, IStatement statement) {
        this.expression = expression;
        this.statement = statement;
    }

    @Override
    public ProgramState execute(ProgramState state) throws StatementExecutionException, ExpressionEvaluationException, ADTException {
        IStack<IStatement> executionStack = state.getExecutionStack();
        IValue result = this.expression.evaluate(state.getSymbolTable(), state.getHeap());

        if (!result.getType().equals(new BoolType()))
            throw new StatementExecutionException(String.format("[%s] The condition of the while statement is not a boolean!", this.getClass().getSimpleName()));

        BoolValue boolValue = (BoolValue)result;
        if (boolValue.getValue()) {
            executionStack.push(this);
            executionStack.push(this.statement);
        }

        return null;
    }

    @Override
    public IDictionary<String, IType> typeCheck(IDictionary<String, IType> typeEnv) throws StatementExecutionException, ExpressionEvaluationException, ADTException {
        IType typeExpression = this.expression.typeCheck(typeEnv);

        if (!typeExpression.equals(new BoolType()))
            throw new StatementExecutionException(String.format("[%s] The condition of the while statement is not a boolean!", this.getClass().getSimpleName()));

        this.statement.typeCheck(typeEnv.deepCopy());
        return typeEnv;
    }
    
    @Override
    public IStatement deepCopy() {
        return new WhileStatement(this.expression.deepCopy(), this.statement.deepCopy());
    }

    @Override
    public String toString() {
        return "while(" + this.expression + ") {" + this.statement + "}";
    }
}
