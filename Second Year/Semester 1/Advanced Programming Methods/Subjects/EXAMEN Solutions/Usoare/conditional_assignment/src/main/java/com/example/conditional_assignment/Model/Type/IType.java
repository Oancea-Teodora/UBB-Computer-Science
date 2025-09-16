package com.example.conditional_assignment.Model.Type;

import com.example.conditional_assignment.Model.Value.IValue;

public interface IType {
    IValue getDefaultValue();
    IType deepCopy();
}
