package com.example.for_statement.Model.Expression;

import com.example.for_statement.Model.ADT.IDictionary;
import com.example.for_statement.Model.ADT.IHeapTable;
import com.example.for_statement.Model.Exceptions.MyException;
import com.example.for_statement.Model.Type.IType;
import com.example.for_statement.Model.Value.IValue;

public interface IExpression {
    IValue eval(IDictionary<String, IValue> symbolTable, IHeapTable<IValue> heapTable) throws MyException;
    IExpression deepCopy();
    IType typeCheck(IDictionary<String, IType> typeEnv) throws MyException;
}
