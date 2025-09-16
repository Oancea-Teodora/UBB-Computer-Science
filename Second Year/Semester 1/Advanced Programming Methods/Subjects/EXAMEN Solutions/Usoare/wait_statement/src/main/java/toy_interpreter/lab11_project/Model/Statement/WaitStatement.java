package toy_interpreter.lab11_project.Model.Statement;

import toy_interpreter.lab11_project.Model.ADT.IDictionary;
import toy_interpreter.lab11_project.Model.ADT.IStack;
import toy_interpreter.lab11_project.Model.ADT.MyStack;
import toy_interpreter.lab11_project.Model.Exceptions.MyException;
import toy_interpreter.lab11_project.Model.Expression.ValueExpression;
import toy_interpreter.lab11_project.Model.ProgramState.ProgramState;
import toy_interpreter.lab11_project.Model.Type.IType;
import toy_interpreter.lab11_project.Model.Value.IntValue;

public class WaitStatement implements IStatement {
    private int number;

    public WaitStatement(int number)
    {
        this.number = number;
    }

    @Override
    public ProgramState execute(ProgramState currentState) throws MyException {
        IStack<IStatement> stk = currentState.getExecutionStack();
        if(number!=0)
        {
            stk.push(new CompoundStatement(new PrintStatement(new ValueExpression(new IntValue(number))), new WaitStatement(number-1)));
        }
        return null;
    }

    @Override
    public String toString()
    {
        return "Wait("+this.number+")";
    }

    @Override
    public IStatement deepCopy() {
        return new WaitStatement(number);
    }

    @Override
    public IDictionary<String, IType> typeCheck(IDictionary<String, IType> typeEnv) throws MyException {
        return typeEnv;
    }
}
