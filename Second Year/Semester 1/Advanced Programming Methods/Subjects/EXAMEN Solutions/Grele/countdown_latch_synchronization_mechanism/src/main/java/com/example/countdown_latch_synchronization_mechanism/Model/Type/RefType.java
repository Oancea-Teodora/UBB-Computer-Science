package com.example.countdown_latch_synchronization_mechanism.Model.Type;

import com.example.countdown_latch_synchronization_mechanism.Model.Value.IValue;
import com.example.countdown_latch_synchronization_mechanism.Model.Value.RefValue;

public class RefType implements IType{
    IType inner;

    public RefType(IType inner) {
        this.inner = inner;
    }

    public IType getInner() {
        return this.inner;
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof RefType otherRefType) {
            if (otherRefType.getInner() == null) {
                return true;
            }
            else {
                return inner.equals(otherRefType.getInner());
            }
        }
        else {
            return false;
        }
    }

    @Override
    public String toString() {
        return "Ref " + this.inner.toString();
    }

    @Override
    public IValue getDefaultValue() {
        return new RefValue(0, this.inner);
    }

    @Override
    public IType deepCopy() {
        return new RefType(this.inner);
    }
}
