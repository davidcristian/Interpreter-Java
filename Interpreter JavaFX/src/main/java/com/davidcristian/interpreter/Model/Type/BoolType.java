package com.davidcristian.interpreter.Model.Type;

import com.davidcristian.interpreter.Model.Value.BoolValue;

public class BoolType implements IType {


    @Override
    public BoolValue defaultValue() {
        return new BoolValue(false);
    }

    @Override
    public boolean equals(Object other) {
        return other instanceof BoolType;
    }

    @Override
    public IType deepCopy() {
        return new BoolType();
    }

    @Override
    public String toString() {
        return "bool";
    }
}
