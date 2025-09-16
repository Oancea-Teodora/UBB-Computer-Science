package com.example.countdown_latch_synchronization_mechanism.Model.Type;

import com.example.countdown_latch_synchronization_mechanism.Model.Value.IValue;

public interface IType {
    IValue getDefaultValue();
    IType deepCopy();
}
