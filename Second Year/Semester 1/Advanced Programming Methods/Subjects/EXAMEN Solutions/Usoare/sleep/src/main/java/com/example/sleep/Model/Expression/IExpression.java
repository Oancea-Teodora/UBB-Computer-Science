package com.example.sleep.Model.Expression;

import com.example.sleep.Model.ADT.IDictionary;
import com.example.sleep.Model.ADT.IHeapTable;
import com.example.sleep.Model.Exceptions.MyException;
import com.example.sleep.Model.Type.IType;
import com.example.sleep.Model.Value.IValue;

public interface IExpression {
    IValue eval(IDictionary<String, IValue> symbolTable, IHeapTable<IValue> heapTable) throws MyException;
    IExpression deepCopy();
    IType typeCheck(IDictionary<String, IType> typeEnv) throws MyException;
}
