package com.example.cyclic_barrier_synchronization_mechanism.Model.Statement;

import com.example.cyclic_barrier_synchronization_mechanism.Model.ADT.IDictionary;
import com.example.cyclic_barrier_synchronization_mechanism.Model.Exceptions.MyException;
import com.example.cyclic_barrier_synchronization_mechanism.Model.Expression.IExpression;
import com.example.cyclic_barrier_synchronization_mechanism.Model.ProgramState.ProgramState;
import com.example.cyclic_barrier_synchronization_mechanism.Model.Type.IType;
import com.example.cyclic_barrier_synchronization_mechanism.Model.Type.IntType;
import com.example.cyclic_barrier_synchronization_mechanism.Model.Value.IValue;
import com.example.cyclic_barrier_synchronization_mechanism.Model.Value.IntValue;

public class newBarrierStmt implements IStatement{
    private String variableName;
    private IExpression expression;

    public newBarrierStmt(String variableName, IExpression expression) {
        this.variableName = variableName;
        this.expression = expression;
    }

    @Override
    public ProgramState execute(ProgramState currentState) throws MyException {
        if (!currentState.getSymbolTable().isDefined(this.variableName)) {
            throw new MyException("The given barrier pointer variable (" + this.variableName + ") is not defined in the symbol table.");
        }

        IValue variableValue = currentState.getSymbolTable().lookUp(this.variableName);
        if (!variableValue.getType().equals(new IntType())) {
            throw new MyException("The given barrier pointer (" + this.variableName + ") is not an integer.");
        }

        IValue expressionValue = this.expression.eval(currentState.getSymbolTable(), currentState.getHeapTable());
        if (!expressionValue.getType().equals(new IntType())) {
            throw new MyException("The given barrier capacity is not an integer (" + this.expression.toString() + " given).");
        }
        IntValue expressionValueInteger = (IntValue) expressionValue;
        int expressionValueInt = expressionValueInteger.getValue();

        int addressUsed = currentState.getBarrierTable().addNewBarrier(expressionValueInt);
        currentState.getSymbolTable().addKeyValuePair(this.variableName, new IntValue(addressUsed));

        return null;
    }

    @Override
    public IDictionary<String, IType> typeCheck(IDictionary<String, IType> typeEnv) throws MyException {
        IType variableType = typeEnv.lookUp(this.variableName);
        IType expressionType = this.expression.typeCheck(typeEnv);
        if (!variableType.equals(new IntType())) {
            throw new MyException("The barrier pointer variable " + this.variableName + " is not an Integer.");
        }
        if (!expressionType.equals(new IntType())) {
            throw new MyException("The barrier capacity provided is not an Integer (" + this.expression.toString() + " given).");
        }
        return typeEnv;
    }

    @Override
    public IStatement deepCopy() {
        return new newBarrierStmt(this.variableName, this.expression);
    }


    @Override
    public String toString() {
        return "newBarrier(" + this.variableName + ", " + this.expression.toString() + ")";
    }
}
