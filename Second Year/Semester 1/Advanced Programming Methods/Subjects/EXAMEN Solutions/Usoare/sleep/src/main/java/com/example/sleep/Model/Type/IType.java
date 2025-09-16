package com.example.sleep.Model.Type;

import com.example.sleep.Model.Value.IValue;

public interface IType {
    IValue getDefaultValue();
    IType deepCopy();
}
