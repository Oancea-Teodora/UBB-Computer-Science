package com.example.sleep.Model.Value;

import com.example.sleep.Model.Type.IType;

public interface IValue {
    IType getType();
    IValue deepCopy();
}
