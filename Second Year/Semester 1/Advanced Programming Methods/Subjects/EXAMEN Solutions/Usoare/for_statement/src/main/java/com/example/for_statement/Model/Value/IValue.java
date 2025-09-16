package com.example.for_statement.Model.Value;

import com.example.for_statement.Model.Type.IType;

public interface IValue {
    IType getType();
    IValue deepCopy();
}
