package com.davidcristian.interpreter.Exceptions;

public class ExpressionEvaluationException extends InterpreterException {
    public ExpressionEvaluationException() {
        super();
    }

    public ExpressionEvaluationException(String message) {
        super(message);
    }
}
