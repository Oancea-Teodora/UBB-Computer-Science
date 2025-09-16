package com.example.lock_syncronization_mechanism.Model.Statement;

import com.example.lock_syncronization_mechanism.Model.ADT.IDictionary;
import com.example.lock_syncronization_mechanism.Model.Exceptions.MyException;
import com.example.lock_syncronization_mechanism.Model.ProgramState.ProgramState;
import com.example.lock_syncronization_mechanism.Model.Type.IType;
import com.example.lock_syncronization_mechanism.Model.Type.IntType;
import com.example.lock_syncronization_mechanism.Model.Value.IValue;
import com.example.lock_syncronization_mechanism.Model.Value.IntValue;

public class UnlockStatement implements IStatement{
    private String variableName;

    public UnlockStatement(String variableName) {
        this.variableName = variableName;
    }

    @Override
    public ProgramState execute(ProgramState currentState) throws MyException {
        IValue foundIndex = currentState.getSymbolTable().lookUp(this.variableName);
        if (!foundIndex.getType().equals(new IntType())) {
            throw new MyException("The type of the lock index (" + this.variableName + ") is not an Integer.");
        }

        IntValue foundIndexInteger = (IntValue) foundIndex;
        int foundIndexInt = foundIndexInteger.getValue();
        if (currentState.getLockTable().isDefined(foundIndexInt) && currentState.getLockTable().getLockTableValue(foundIndexInt) == currentState.getId()) {
            currentState.getLockTable().updateLockTableEntry(foundIndexInt, -1);
        }
        return null;
    }

    @Override
    public IStatement deepCopy() {
        return new UnlockStatement(this.variableName);
    }

    @Override
    public IDictionary<String, IType> typeCheck(IDictionary<String, IType> typeEnv) throws MyException {

        return typeEnv;
    }

    @Override
    public String toString() {
        return "unlock(" + this.variableName + ")";
    }
}
