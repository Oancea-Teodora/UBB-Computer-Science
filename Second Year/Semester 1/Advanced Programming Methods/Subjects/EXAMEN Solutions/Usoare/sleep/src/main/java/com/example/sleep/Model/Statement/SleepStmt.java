package com.example.sleep.Model.Statement;

import com.example.sleep.Model.ADT.IDictionary;
import com.example.sleep.Model.Exceptions.MyException;
import com.example.sleep.Model.ProgramState.ProgramState;
import com.example.sleep.Model.Type.IType;

public class SleepStmt implements IStatement{
    private int number;

    public SleepStmt(int number) {
        this.number = number;
    }

    @Override
    public ProgramState execute(ProgramState currentState) throws MyException {
        if (this.number > 0) {
            currentState.getExecutionStack().push(new SleepStmt(this.number - 1));
        }
        return null;
    }

    @Override
    public IStatement deepCopy() {
        return new SleepStmt(this.number);
    }

    @Override
    public IDictionary<String, IType> typeCheck(IDictionary<String, IType> typeEnv) throws MyException {
        return typeEnv;
    }

    @Override
    public String toString() {
        return "sleep(" + this.number + ")";
    }
}
