package com.example.for_statement.Model.Type;

import com.example.for_statement.Model.Value.IValue;

public interface IType {
    IValue getDefaultValue();
    IType deepCopy();
}
