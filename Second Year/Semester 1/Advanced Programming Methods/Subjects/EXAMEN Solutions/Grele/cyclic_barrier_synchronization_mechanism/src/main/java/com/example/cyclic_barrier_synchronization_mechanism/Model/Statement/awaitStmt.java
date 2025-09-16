package com.example.cyclic_barrier_synchronization_mechanism.Model.Statement;

import com.example.cyclic_barrier_synchronization_mechanism.Model.ADT.IDictionary;
import com.example.cyclic_barrier_synchronization_mechanism.Model.Exceptions.MyException;
import com.example.cyclic_barrier_synchronization_mechanism.Model.ProgramState.ProgramState;
import com.example.cyclic_barrier_synchronization_mechanism.Model.Type.IType;
import com.example.cyclic_barrier_synchronization_mechanism.Model.Type.IntType;
import com.example.cyclic_barrier_synchronization_mechanism.Model.Value.IValue;
import com.example.cyclic_barrier_synchronization_mechanism.Model.Value.IntValue;
import javafx.util.Pair;

import java.util.List;

public class awaitStmt implements IStatement{
    private String variableName;

    public awaitStmt(String variableName) {
        this.variableName = variableName;
    }

    @Override
    public ProgramState execute(ProgramState currentState) throws MyException {
        if (!currentState.getSymbolTable().isDefined(this.variableName)) {
            throw new MyException("The given barrier pointer variable (" + this.variableName + ") is not defined in the symbol table.");
        }

        IValue foundIndex = currentState.getSymbolTable().lookUp(this.variableName);
        if (!foundIndex.getType().equals(new IntType())) {
            throw new MyException("The given barrier pointer variable (" + this.variableName + ") is not an integer.");
        }

        IntValue foundIndexInteger = (IntValue) foundIndex;
        int foundIndexInt = foundIndexInteger.getValue();
        if (!currentState.getBarrierTable().isAddressUsed(foundIndexInt)) {
            throw new MyException("The index held by variable " + this.variableName + " (" + foundIndexInt + ") is not an index in the Barrier Table.");
        }

        Pair<Integer, List<Integer>> barrier = currentState.getBarrierTable().getBarrierFromAddress(foundIndexInt);
        int barrierCapacity = barrier.getKey();
        List<Integer> barrierThreadsList = barrier.getValue();
        int currentThreadId = currentState.getId();

        if (barrierCapacity > barrierThreadsList.size()) {
            if (!barrierThreadsList.contains(currentThreadId)) {
                barrierThreadsList.add(currentThreadId);
            }
            currentState.getExecutionStack().push(this);
        }

        return null;
    }

    @Override
    public IStatement deepCopy() {
        return new awaitStmt(this.variableName);
    }

    @Override
    public IDictionary<String, IType> typeCheck(IDictionary<String, IType> typeEnv) throws MyException {
        IType variableType = typeEnv.lookUp(this.variableName);
        if (!variableType.equals(new IntType())) {
            throw new MyException("The given barrier pointer (" + this.variableName + ") is not an integer.");
        }
        return typeEnv;
    }

    @Override
    public String toString() {
        return "await(" + this.variableName + ")";
    }
}
