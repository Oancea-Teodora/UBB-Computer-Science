package toy_interpreter.lab11_project.Model.Statement;

import toy_interpreter.lab11_project.Model.ADT.IDictionary;
import toy_interpreter.lab11_project.Model.ADT.MyDictionary;
import toy_interpreter.lab11_project.Model.ADT.MyStack;
import toy_interpreter.lab11_project.Model.Exceptions.MyException;
import toy_interpreter.lab11_project.Model.ProgramState.ProgramState;
import toy_interpreter.lab11_project.Model.Type.IType;
import toy_interpreter.lab11_project.Model.Value.IValue;

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
        return new ProgramState(forkedExecutionStack, forkedSymbolTable, currentState.getOutput(), currentState.getFileTable(), currentState.getHeapTable(), this.statement);
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
