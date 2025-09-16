package toy_interpreter.lab11_project.Model.Statement;

import toy_interpreter.lab11_project.Model.ADT.IDictionary;
import toy_interpreter.lab11_project.Model.Exceptions.MyException;
import toy_interpreter.lab11_project.Model.Expression.IExpression;
import toy_interpreter.lab11_project.Model.ProgramState.ProgramState;
import toy_interpreter.lab11_project.Model.Type.IType;
import toy_interpreter.lab11_project.Model.Type.IntType;
import toy_interpreter.lab11_project.Model.Value.IValue;
import toy_interpreter.lab11_project.Model.Value.IntValue;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class CreateSemaphoreStatement implements IStatement {

    private IExpression exp;
    private String varName;
    private static final Lock lock = new ReentrantLock();

    public CreateSemaphoreStatement(String variableName, IExpression expression) {
        this.varName = variableName;
        this.exp = expression;
    }

    @Override
    public ProgramState execute(ProgramState currentState) throws MyException {
        lock.lock();
        IValue cond = exp.eval(currentState.getSymbolTable(), currentState.getHeapTable());
        if(!currentState.getSymbolTable().isDefined(varName))
            throw new MyException("The variable is not defined in the Symbol Table!");
        if(!cond.getType().equals(new IntType()))
            throw new MyException("The variable is not an interger");

        IValue varValue = currentState.getSymbolTable().lookUp(varName);
        int NL = ((IntValue) varValue).getValue();
        currentState


        lock.unlock();
        return null;
    }

    @Override
    public IStatement deepCopy() {
        return null;
    }

    @Override
    public IDictionary<String, IType> typeCheck(IDictionary<String, IType> typeEnv) throws MyException {
        return null;
    }
}
