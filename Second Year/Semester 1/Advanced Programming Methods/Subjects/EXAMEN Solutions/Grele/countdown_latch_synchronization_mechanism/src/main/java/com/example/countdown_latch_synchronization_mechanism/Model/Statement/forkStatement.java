package com.example.countdown_latch_synchronization_mechanism.Model.Statement;

import com.example.countdown_latch_synchronization_mechanism.Model.ADT.IDictionary;
import com.example.countdown_latch_synchronization_mechanism.Model.ADT.MyDictionary;
import com.example.countdown_latch_synchronization_mechanism.Model.ADT.MyStack;
import com.example.countdown_latch_synchronization_mechanism.Model.Exceptions.MyException;
import com.example.countdown_latch_synchronization_mechanism.Model.ProgramState.ProgramState;
import com.example.countdown_latch_synchronization_mechanism.Model.Type.IType;
import com.example.countdown_latch_synchronization_mechanism.Model.Value.IValue;

import java.util.Map;
import java.util.stream.Collectors;

public class forkStatement implements IStatement{
    private IStatement statement;

    public forkStatement(IStatement stmt) {
        this.statement = stmt;
    }

    @Override
    public ProgramState execute(ProgramState currentState) throws MyException {
        MyStack<IStatement> forkedExecutionStack = new MyStack<>();     // need to create a new execution stack
        // Need to create a deep copy of the symbol table
        // We cannot use the already existing method <shallowCopy> from IDictionary since that method will not
        // create a deep copy of the dictionary's values, and we need that to ensure we won't have conflicts
        Map<String, IValue> symbolTableContent = currentState.getSymbolTable().getContent();
        MyDictionary<String, IValue> forkedSymbolTable = new MyDictionary<>();
        forkedSymbolTable.setContent(symbolTableContent.entrySet().stream()
                                        .collect(Collectors.toMap(e -> e.getKey(), e -> e.getValue().deepCopy())));
        return new ProgramState(forkedExecutionStack, forkedSymbolTable, currentState.getOutput(), currentState.getFileTable(), currentState.getHeapTable(), currentState.getLatchTable(), this.statement);
    }

    @Override
    public IStatement deepCopy() {
        return new forkStatement(this.statement.deepCopy());
    }

    @Override
    public IDictionary<String, IType> typeCheck(IDictionary<String, IType> typeEnv) throws MyException {
        this.statement.typeCheck(typeEnv.shallowCopy());
        return typeEnv;
    }

    @Override
    public String toString() {
        return "fork(" + this.statement.toString() + ")";
    }
}
