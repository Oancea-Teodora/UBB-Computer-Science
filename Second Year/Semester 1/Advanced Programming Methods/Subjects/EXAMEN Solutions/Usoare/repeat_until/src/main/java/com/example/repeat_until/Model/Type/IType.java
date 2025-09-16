package com.example.repeat_until.Model.Type;

import com.example.repeat_until.Model.Value.IValue;

public interface IType {
    IValue getDefaultValue();
    IType deepCopy();
}
