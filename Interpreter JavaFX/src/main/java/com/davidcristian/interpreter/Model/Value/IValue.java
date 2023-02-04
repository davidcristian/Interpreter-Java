package com.davidcristian.interpreter.Model.Value;

import com.davidcristian.interpreter.Model.Type.IType;

public interface IValue {
    IType getType();
    boolean equals(Object other);

    IValue deepCopy();
}
