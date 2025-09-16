package com.example.switch_statement.Model.Type;

import com.example.switch_statement.Model.Value.IValue;

public interface IType {
    IValue getDefaultValue();
    IType deepCopy();
}
