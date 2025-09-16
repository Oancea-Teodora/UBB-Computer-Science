package com.example.repeat_until.Model.Value;

import com.example.repeat_until.Model.Type.IType;

public interface IValue {
    IType getType();
    IValue deepCopy();
}
