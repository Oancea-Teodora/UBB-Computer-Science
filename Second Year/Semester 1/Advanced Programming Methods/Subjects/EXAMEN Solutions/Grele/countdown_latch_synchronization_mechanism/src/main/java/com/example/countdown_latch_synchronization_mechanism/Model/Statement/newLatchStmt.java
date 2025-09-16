package com.example.countdown_latch_synchronization_mechanism.Model.Statement;

import com.example.countdown_latch_synchronization_mechanism.Model.ADT.IDictionary;
import com.example.countdown_latch_synchronization_mechanism.Model.Exceptions.MyException;
import com.example.countdown_latch_synchronization_mechanism.Model.Expression.IExpression;
import com.example.countdown_latch_synchronization_mechanism.Model.ProgramState.ProgramState;
import com.example.countdown_latch_synchronization_mechanism.Model.Type.IType;
import com.example.countdown_latch_synchronization_mechanism.Model.Type.IntType;
import com.example.countdown_latch_synchronization_mechanism.Model.Value.IValue;
import com.example.countdown_latch_synchronization_mechanism.Model.Value.IntValue;

public class newLatchStmt implements IStatement{
    private String varName;
    private IExpression expression;

    public newLatchStmt(String varName, IExpression expression) {
        this.varName = varName;
        this.expression = expression;
    }

    @Override
    public ProgramState execute(ProgramState currentState) throws MyException {
        IValue varValue = currentState.getSymbolTable().lookUp(this.varName);
        if (!varValue.getType().equals(new IntType())) {
            throw new MyException("ERROR: The given variable (" + this.varName + ") is not an integer: it cannot hold the address of a countdown latch.");
        }

        IValue num1 = this.expression.eval(currentState.getSymbolTable(), currentState.getHeapTable());
        if (!num1.getType().equals(new IntType())) {
            throw new MyException("ERROR: The given countdown latch counter is not an integer (" + this.expression.toString() + " given).");
        }
        IntValue num1Integer = (IntValue) num1;
        int num1Int = num1Integer.getValue();

        int addressUsed = currentState.getLatchTable().addNewLatch(num1Int);
        currentState.getSymbolTable().addKeyValuePair(this.varName, new IntValue(addressUsed));

        return null;
    }

    @Override
    public IStatement deepCopy() {
        return new newLatchStmt(this.varName, this.expression);
    }

    @Override
    public IDictionary<String, IType> typeCheck(IDictionary<String, IType> typeEnv) throws MyException {
        IType varType = typeEnv.lookUp(this.varName);
        if (!varType.equals(new IntType())) {
            throw new MyException("ERROR: The given variable (" + this.varName + ") is not an integer: it cannot hold the address of a countdown latch.");
        }
        IType expressionType = this.expression.typeCheck(typeEnv);
        if (!expressionType.equals(new IntType())) {
            throw new MyException("ERROR: The given countdown latch counter is not an integer (" + this.expression.toString() + " given).");
        }
        return typeEnv;
    }

    @Override
    public String toString() {
        return "newLatch(" + this.varName + ", " + this.expression.toString() + ")";
    }

}
