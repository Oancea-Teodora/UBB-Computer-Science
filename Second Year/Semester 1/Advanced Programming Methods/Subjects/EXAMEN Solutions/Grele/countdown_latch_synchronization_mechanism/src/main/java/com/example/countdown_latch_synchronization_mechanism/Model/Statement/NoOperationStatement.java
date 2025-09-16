package com.example.countdown_latch_synchronization_mechanism.Model.Statement;

import com.example.countdown_latch_synchronization_mechanism.Model.ADT.IDictionary;
import com.example.countdown_latch_synchronization_mechanism.Model.Exceptions.MyException;
import com.example.countdown_latch_synchronization_mechanism.Model.ProgramState.ProgramState;
import com.example.countdown_latch_synchronization_mechanism.Model.Type.IType;

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
