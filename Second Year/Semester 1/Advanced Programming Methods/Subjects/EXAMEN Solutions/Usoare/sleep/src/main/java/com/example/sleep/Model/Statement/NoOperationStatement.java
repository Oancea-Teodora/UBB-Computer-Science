package com.example.sleep.Model.Statement;

import com.example.sleep.Model.ADT.IDictionary;
import com.example.sleep.Model.Exceptions.MyException;
import com.example.sleep.Model.ProgramState.ProgramState;
import com.example.sleep.Model.Type.IType;

public class NoOperationStatement implements IStatement {

    public NoOperationStatement() {}

    @Override
    public ProgramState execute(ProgramState currentState) {
        return null;
    }

    @Override
    public IStatement deepCopy() {
        return new NoOperationStatement();
    }

    @Override
    public IDictionary<String, IType> typeCheck(IDictionary<String, IType> typeEnv) throws MyException {
        return typeEnv;
    }

    @Override
    public String toString() {
        return "NoOp Statement";
    }
}
