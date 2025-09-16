package com.example.countdown_latch_synchronization_mechanism.Model.Value;

import com.example.countdown_latch_synchronization_mechanism.Model.Type.IType;

public interface IValue {
    IType getType();
    IValue deepCopy();
}
