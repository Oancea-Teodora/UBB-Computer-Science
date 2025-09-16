package com.example.countdown_latch_synchronization_mechanism.Model.Type;

import com.example.countdown_latch_synchronization_mechanism.Model.Value.IValue;
import com.example.countdown_latch_synchronization_mechanism.Model.Value.IntValue;

public class IntType implements IType{
    public IntType() {}

    @Override
    public IValue getDefaultValue() {
        return new IntValue(0);
    }

    @Override
    public IType deepCopy() {
        return new IntType();
    }

    @Override
    public String toString() {
        return "int";
    }

    @Override
    public boolean equals(Object other) {
        return other instanceof IntType;
    }
}
