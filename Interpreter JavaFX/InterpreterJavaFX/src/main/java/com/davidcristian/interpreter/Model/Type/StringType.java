package com.davidcristian.interpreter.Model.Type;

import com.davidcristian.interpreter.Model.Value.IValue;
import com.davidcristian.interpreter.Model.Value.StringValue;

public class StringType implements IType {


    @Override
    public IValue defaultValue() {
        return new StringValue("");
    }

    @Override
    public boolean equals(Object other) {
        return other instanceof StringType;
    }

    @Override
    public IType deepCopy() {
        return new StringType();
    }

    @Override
    public String toString() {
        return "string";
    }
}
