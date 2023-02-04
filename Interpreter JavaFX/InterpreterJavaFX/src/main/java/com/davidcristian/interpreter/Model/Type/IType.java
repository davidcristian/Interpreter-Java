package com.davidcristian.interpreter.Model.Type;

import com.davidcristian.interpreter.Model.Value.IValue;

public interface IType {
    IValue defaultValue();
    boolean equals(Object other);

    IType deepCopy();
}
