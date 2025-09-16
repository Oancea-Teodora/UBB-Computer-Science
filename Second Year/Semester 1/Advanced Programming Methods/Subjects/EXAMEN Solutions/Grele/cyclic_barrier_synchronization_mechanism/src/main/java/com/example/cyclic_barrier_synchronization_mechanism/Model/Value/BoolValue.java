package com.example.cyclic_barrier_synchronization_mechanism.Model.Value;

import com.example.cyclic_barrier_synchronization_mechanism.Model.Type.BoolType;
import com.example.cyclic_barrier_synchronization_mechanism.Model.Type.IType;

import java.util.Objects;

public class BoolValue implements IValue{
    private final boolean value;

    public BoolValue(boolean value) {
        this.value = value;
    }

    public boolean getValue() {
        return this.value;
    }

    @Override
    public String toString() {
        return "" + this.value;
    }

    @Override
    public IType getType() {
        return new BoolType();
    }

    @Override
    public IValue deepCopy() {
        return new BoolValue(this.value);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BoolValue boolValue = (BoolValue) o;
        return value == boolValue.value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
