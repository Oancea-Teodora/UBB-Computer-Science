package com.example.switch_statement.Model.Expression;

import com.example.switch_statement.Model.ADT.IDictionary;
import com.example.switch_statement.Model.ADT.IHeapTable;
import com.example.switch_statement.Model.Exceptions.MyException;
import com.example.switch_statement.Model.Type.IType;
import com.example.switch_statement.Model.Value.IValue;

public class VariableExpression implements IExpression{
    private String id;

    public VariableExpression(String id) {
        this.id = id;
    }

    @Override
    public IValue eval(IDictionary<String, IValue> symbolTable, IHeapTable<IValue> heapTable) throws MyException {
        return symbolTable.lookUp(this.id);
    }

    @Override
    public IExpression deepCopy() {
        return new VariableExpression(this.id);
    }

    @Override
    public IType typeCheck(IDictionary<String, IType> typeEnv) throws MyException {
        return typeEnv.lookUp(this.id);
    }

    @Override
    public String toString() {
        return this.id;
    }
}
