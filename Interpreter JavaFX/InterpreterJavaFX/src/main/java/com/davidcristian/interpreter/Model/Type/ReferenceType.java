package com.davidcristian.interpreter.Model.Type;

import com.davidcristian.interpreter.Model.Value.IValue;
import com.davidcristian.interpreter.Model.Value.ReferenceValue;

public class ReferenceType implements IType {
    private IType inner;

    public ReferenceType(IType inner) {
        this.inner = inner;
    }

    public IType getInner() {
        return inner;
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof ReferenceType)
            return this.inner.equals(((ReferenceType)other).getInner());

        return false;
    }

    @Override
    public IValue defaultValue() {
        return new ReferenceValue(0, this.inner);
    }

    @Override
    public IType deepCopy() {
        return new ReferenceType(this.inner.deepCopy());
    }

    @Override
    public String toString() {
        return "Ref " + this.inner;
    }
}
