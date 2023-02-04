package com.davidcristian.interpreter.Model.Value;

import com.davidcristian.interpreter.Model.Type.BoolType;
import com.davidcristian.interpreter.Model.Type.IType;

public class BoolValue implements IValue {
    private boolean value;

    public BoolValue(boolean value) {
        this.value = value;
    }

    public boolean getValue() {
        return this.value;
    }

    @Override
    public IType getType() {
        return new BoolType();
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof BoolValue)
            return this.value == ((BoolValue)other).value;

        return false;
    }

    @Override
    public IValue deepCopy() {
        return new BoolValue(this.value);
    }

    @Override
    public String toString() {
        return String.valueOf(this.value);
    }
}
