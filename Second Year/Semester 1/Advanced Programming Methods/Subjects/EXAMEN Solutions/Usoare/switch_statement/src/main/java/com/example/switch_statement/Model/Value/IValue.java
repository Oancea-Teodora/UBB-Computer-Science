package com.example.switch_statement.Model.Value;

import com.example.switch_statement.Model.Type.IType;

public interface IValue {
    IType getType();
    IValue deepCopy();
}
