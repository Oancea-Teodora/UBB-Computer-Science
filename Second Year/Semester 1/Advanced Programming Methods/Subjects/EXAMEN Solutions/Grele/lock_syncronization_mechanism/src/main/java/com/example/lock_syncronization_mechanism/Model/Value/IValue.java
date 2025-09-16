package com.example.lock_syncronization_mechanism.Model.Value;

import com.example.lock_syncronization_mechanism.Model.Type.IType;

public interface IValue {
    IType getType();
    IValue deepCopy();
}
