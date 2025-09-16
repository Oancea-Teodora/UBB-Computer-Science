package com.example.repeat_until.Model.Statement;

import com.example.repeat_until.Model.ADT.IDictionary;
import com.example.repeat_until.Model.Exceptions.MyException;
import com.example.repeat_until.Model.Expression.IExpression;
import com.example.repeat_until.Model.ProgramState.ProgramState;
import com.example.repeat_until.Model.Type.IType;
import com.example.repeat_until.Model.Value.IValue;

public class AssignStatement implements IStatement{
    private String id;
    private IExpression expression;

    public AssignStatement(String id, IExpression expression) {
        this.id = id;
        this.expression = expression;
    }

    @Override
    public ProgramState execute(ProgramState currentState) throws MyException {
        IDictionary<String, IValue> symbolTable = currentState.getSymbolTable();
        if (symbolTable.isDefined(this.id)) {
            IValue val = this.expression.eval(symbolTable, currentState.getHeapTable());
            IType type = symbolTable.lookUp(this.id).getType();
            if (val.getType().equals(type)) {
                symbolTable.addKeyValuePair(this.id, val);
            } else {
                throw new MyException("Declared type of variable " + id + " and type of the assigned expression do not match.");
            }
        } else {
            throw new MyException("Variable " + id + " was not previously declared.");
        }
        return null;
    }

    @Override
    public IStatement deepCopy() {
        return new AssignStatement(this.id, this.expression.deepCopy());
    }

    @Override
    public IDictionary<String, IType> typeCheck(IDictionary<String, IType> typeEnv) throws MyException {
        IType typeVar = typeEnv.lookUp(this.id);
        IType typeExp = this.expression.typeCheck(typeEnv);
        if (typeVar.equals(typeExp)) {
            return typeEnv;
        } else {
            throw new MyException("TYPE CHECK ERROR: Right hand side and left hand side in assignment have different types.");
        }
    }

    @Override
    public String toString() {
        return this.id + " = " + this.expression.toString();
    }
}
