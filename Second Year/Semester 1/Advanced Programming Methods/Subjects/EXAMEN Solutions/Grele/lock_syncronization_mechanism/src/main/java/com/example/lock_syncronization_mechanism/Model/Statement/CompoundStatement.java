package com.example.lock_syncronization_mechanism.Model.Statement;

import com.example.lock_syncronization_mechanism.Model.ADT.IDictionary;
import com.example.lock_syncronization_mechanism.Model.ADT.IStack;
import com.example.lock_syncronization_mechanism.Model.Exceptions.MyException;
import com.example.lock_syncronization_mechanism.Model.ProgramState.ProgramState;
import com.example.lock_syncronization_mechanism.Model.Type.IType;

public class CompoundStatement implements IStatement{
    private IStatement statement1;
    private IStatement statement2;

    public CompoundStatement(IStatement s1, IStatement s2) {
        this.statement1 = s1;
        this.statement2 = s2;
    }

    @Override
    public ProgramState execute(ProgramState currentState) {
        IStack<IStatement> executionStack = currentState.getExecutionStack();
        executionStack.push(this.statement2);
        executionStack.push(this.statement1);
        return null;
    }

    @Override
    public IStatement deepCopy() {
        return new CompoundStatement(this.statement1.deepCopy(), this.statement2.deepCopy());
    }

    @Override
    public IDictionary<String, IType> typeCheck(IDictionary<String, IType> typeEnv) throws MyException {
        return this.statement2.typeCheck(this.statement1.typeCheck(typeEnv));
    }

    @Override
    public String toString() {
        return "(" + this.statement1.toString() + "; " + this.statement2.toString() + ")";
    }
}
