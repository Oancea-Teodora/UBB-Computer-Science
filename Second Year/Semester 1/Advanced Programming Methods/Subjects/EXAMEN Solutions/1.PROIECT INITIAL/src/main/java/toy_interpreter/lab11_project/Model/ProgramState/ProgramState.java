package toy_interpreter.lab11_project.Model.ProgramState;

import toy_interpreter.lab11_project.Model.ADT.*;
import toy_interpreter.lab11_project.Model.Exceptions.MyException;
import toy_interpreter.lab11_project.Model.Statement.IStatement;
import toy_interpreter.lab11_project.Model.Type.IType;
import toy_interpreter.lab11_project.Model.Value.IValue;
import toy_interpreter.lab11_project.Model.Value.StringValue;

import java.io.BufferedReader;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ProgramState {
    private IStack<IStatement> executionStack;
    private IDictionary<String, IValue> symbolTable;
    private IList<IValue> output;
    private IDictionary<StringValue, BufferedReader> fileTable;
    private IHeapTable<IValue> heapTable;
    private IDictionary<String, IType> typeEnv;
    private IStatement originalProgram;
    private int id;
    private static int baseId;

    public ProgramState(IStack<IStatement> exeStack, IDictionary<String, IValue> symTable, IList<IValue> out,
                        IDictionary<StringValue, BufferedReader> fileTable, IHeapTable<IValue> heapTable, IStatement origPrg) {
        this.executionStack = exeStack;
        this.symbolTable = symTable;
        this.output = out;
        this.fileTable = fileTable;
        this.heapTable = heapTable;
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
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        return "ID: " + this.id + "\n" +
               dtf.format(now) + "\n" +
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
