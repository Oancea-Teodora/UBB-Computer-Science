package com.example.conditional_assignment.Model.Value;

import com.example.conditional_assignment.Model.Type.IType;

public interface IValue {
    IType getType();
    IValue deepCopy();
}
