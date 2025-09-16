package com.example.repeat_until.Model.Expression;

import com.example.repeat_until.Model.ADT.IDictionary;
import com.example.repeat_until.Model.ADT.IHeapTable;
import com.example.repeat_until.Model.Exceptions.MyException;
import com.example.repeat_until.Model.Type.BoolType;
import com.example.repeat_until.Model.Type.IType;
import com.example.repeat_until.Model.Value.BoolValue;
import com.example.repeat_until.Model.Value.IValue;

public class NotExpression implements IExpression{
    private IExpression expression;

    public NotExpression(IExpression expression) {
        this.expression = expression;
    }

    @Override
    public IValue eval(IDictionary<String, IValue> symbolTable, IHeapTable<IValue> heapTable) throws MyException {
        IValue val = this.expression.eval(symbolTable, heapTable);
        if (!val.getType().equals(new BoolType())) {
            throw new MyException("The operand given to NOT is not a boolean.");
        }
        BoolValue boolValue = (BoolValue) val;
        boolean b1 = boolValue.getValue();
        return new BoolValue(!b1);
    }

    @Override
    public IExpression deepCopy() {
        return new NotExpression(this.expression.deepCopy());
    }

    @Override
    public IType typeCheck(IDictionary<String, IType> typeEnv) throws MyException {
        IType expressionType = this.expression.typeCheck(typeEnv);
        if (!expressionType.equals(new BoolType())) {
            throw new MyException("The operand given to NOT is not a boolean.");
        }
        return new BoolType();
    }

    @Override
    public String toString() {
        return "!(" + this.expression.toString() + ")";
    }
}
