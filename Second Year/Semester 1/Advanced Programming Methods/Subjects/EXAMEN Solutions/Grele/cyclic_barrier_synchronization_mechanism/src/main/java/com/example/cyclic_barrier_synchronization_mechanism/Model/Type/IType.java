package com.example.cyclic_barrier_synchronization_mechanism.Model.Type;

import com.example.cyclic_barrier_synchronization_mechanism.Model.Value.IValue;

public interface IType {
    IValue getDefaultValue();
    IType deepCopy();
}
