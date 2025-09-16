package com.example.cyclic_barrier_synchronization_mechanism.Model.Value;

import com.example.cyclic_barrier_synchronization_mechanism.Model.Type.IType;

public interface IValue {
    IType getType();
    IValue deepCopy();
}
