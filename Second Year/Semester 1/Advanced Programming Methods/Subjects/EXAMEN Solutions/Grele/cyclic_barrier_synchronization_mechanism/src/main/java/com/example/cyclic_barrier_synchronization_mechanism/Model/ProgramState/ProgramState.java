package com.example.cyclic_barrier_synchronization_mechanism.Model.ProgramState;

import com.example.cyclic_barrier_synchronization_mechanism.Model.ADT.*;
import com.example.cyclic_barrier_synchronization_mechanism.Model.Exceptions.MyException;
import com.example.cyclic_barrier_synchronization_mechanism.Model.Statement.IStatement;
import com.example.cyclic_barrier_synchronization_mechanism.Model.Type.IType;
import com.example.cyclic_barrier_synchronization_mechanism.Model.Value.IValue;
import com.example.cyclic_barrier_synchronization_mechanism.Model.Value.StringValue;

import java.io.BufferedReader;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ProgramState {
    private IStack<IStatement> executionStack;
    private IDictionary<String, IValue> symbolTable;
    private IList<IValue> output;
    private IDictionary<StringValue, BufferedReader> fileTable;
    private IHeapTable<IValue> heapTable;
    private IBarrierTable barrierTable;
    private IDictionary<String, IType> typeEnv;
    private IStatement originalProgram;
    private int id;
    private static int baseId;

    public ProgramState(IStack<IStatement> exeStack, IDictionary<String, IValue> symTable, IList<IValue> out,
                        IDictionary<StringValue, BufferedReader> fileTable, IHeapTable<IValue> heapTable,
                        IBarrierTable barrierTable, IStatement origPrg) {
        this.executionStack = exeStack;
        this.symbolTable = symTable;
        this.output = out;
        this.fileTable = fileTable;
        this.heapTable = heapTable;
        this.barrierTable = barrierTable;
        this.typeEnv = new MyDictionary<>();
        this.originalProgram = origPrg.deepCopy();
        this.executionStack.push(this.originalProgram);
        this.incrementBaseId();
        this.id = this.getBaseId();
    }

    public int getId() {
        return this.id;
    }

    private synchronized int getBaseId() {
        return baseId;
    }

    private synchronized void incrementBaseId() {
        baseId = baseId + 1;
    }

    public IStack<IStatement> getExecutionStack() {
        return this.executionStack;
    }

    public IDictionary<String, IValue> getSymbolTable() {
        return this.symbolTable;
    }

    public IList<IValue> getOutput() {
        return this.output;
    }

    public IDictionary<StringValue, BufferedReader> getFileTable() { return this.fileTable; }

    public IHeapTable<IValue> getHeapTable() { return this.heapTable; }

    public IBarrierTable getBarrierTable() { return this.barrierTable; }

    public void typeCheck() throws MyException {
        this.originalProgram.typeCheck(this.typeEnv);
    }

    public IStatement getOriginalProgram() {
        return this.originalProgram;
    }

    public boolean isNotCompleted() {
        return !this.executionStack.isEmpty();
    }

    public ProgramState oneStep() throws MyException {
        IStack<IStatement> executionStack = this.executionStack;
        if (executionStack.isEmpty()) {
            throw new MyException("Program state's execution stack is empty.");
        }
        IStatement topStatement = executionStack.pop();
        return topStatement.execute(this);
    }

    @Override
    public String toString() {
        return "ID: " + this.id + "\n" +
               "Execution Stack:\n" +
               this.executionStack.toString() + "\n" +
               "Heap:\n" +
               this.heapTable.toString() + "\n" +
               "Symbol Table:\n" +
               this.symbolTable.toString() + "\n" +
               "Output:\n" +
               this.output.toString() + "\n" +
               "-".repeat(50);
    }
}
