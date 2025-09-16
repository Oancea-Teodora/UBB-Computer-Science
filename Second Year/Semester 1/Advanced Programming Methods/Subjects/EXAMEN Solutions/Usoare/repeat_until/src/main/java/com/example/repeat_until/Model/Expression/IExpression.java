package com.example.repeat_until.Model.Expression;

import com.example.repeat_until.Model.ADT.IDictionary;
import com.example.repeat_until.Model.ADT.IHeapTable;
import com.example.repeat_until.Model.Exceptions.MyException;
import com.example.repeat_until.Model.Type.IType;
import com.example.repeat_until.Model.Value.IValue;

public interface IExpression {
    IValue eval(IDictionary<String, IValue> symbolTable, IHeapTable<IValue> heapTable) throws MyException;
    IExpression deepCopy();
    IType typeCheck(IDictionary<String, IType> typeEnv) throws MyException;
}
