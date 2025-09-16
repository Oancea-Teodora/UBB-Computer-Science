package com.example.lock_syncronization_mechanism.Model.Statement;

import com.example.lock_syncronization_mechanism.Model.ADT.IDictionary;
import com.example.lock_syncronization_mechanism.Model.Exceptions.MyException;
import com.example.lock_syncronization_mechanism.Model.ProgramState.ProgramState;
import com.example.lock_syncronization_mechanism.Model.Type.IType;
import com.example.lock_syncronization_mechanism.Model.Value.IntValue;

public class newLockStmt implements IStatement{
    private String variableName;

    public newLockStmt(String variableName) {
        this.variableName = variableName;
    }

    @Override
    public ProgramState execute(ProgramState currentState) throws MyException {
        if (currentState.getSymbolTable().isDefined(this.variableName)) {
            throw new MyException("ERROR: The given variable(" + this.variableName + ") is already defined in the symbol table.");
        }
        int addressUsed = currentState.getLockTable().addNewLockTableEntry(-1);
        currentState.getSymbolTable().addKeyValuePair(this.variableName, new IntValue(addressUsed));
        return null;
    }

    @Override
    public IStatement deepCopy() {
        return new newLockStmt(this.variableName);
    }

    @Override
    public IDictionary<String, IType> typeCheck(IDictionary<String, IType> typeEnv) throws MyException {
        return typeEnv;
    }

    @Override
    public String toString() {
        return "newLock(" + this.variableName + ")";
    }
}
