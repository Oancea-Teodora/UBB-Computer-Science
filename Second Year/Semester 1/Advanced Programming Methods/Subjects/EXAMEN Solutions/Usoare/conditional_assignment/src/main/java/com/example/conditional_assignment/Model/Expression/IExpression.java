package com.example.conditional_assignment.Model.Expression;

import com.example.conditional_assignment.Model.ADT.IDictionary;
import com.example.conditional_assignment.Model.ADT.IHeapTable;
import com.example.conditional_assignment.Model.Exceptions.MyException;
import com.example.conditional_assignment.Model.Type.IType;
import com.example.conditional_assignment.Model.Value.IValue;

public interface IExpression {
    IValue eval(IDictionary<String, IValue> symbolTable, IHeapTable<IValue> heapTable) throws MyException;
    IExpression deepCopy();
    IType typeCheck(IDictionary<String, IType> typeEnv) throws MyException;
}
