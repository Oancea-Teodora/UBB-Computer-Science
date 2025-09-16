package com.example.lock_syncronization_mechanism.Model.Type;

import com.example.lock_syncronization_mechanism.Model.Value.IValue;

public interface IType {
    IValue getDefaultValue();
    IType deepCopy();
}
