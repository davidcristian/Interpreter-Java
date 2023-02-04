package com.davidcristian.interpreter.Model.Type;

import com.davidcristian.interpreter.Model.Value.IntValue;

public class IntType implements IType {


    @Override
    public IntValue defaultValue() {
        return new IntValue(0);
    }
    @Override
    public boolean equals(Object other) {
        return other instanceof IntType;
    }

    @Override
    public IType deepCopy() {
        return new IntType();
    }

    @Override
    public String toString() {
        return "int";
    }
}
