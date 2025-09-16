package com.example.conditional_assignment.Model.Statement;

import com.example.conditional_assignment.Model.ADT.IDictionary;
import com.example.conditional_assignment.Model.Exceptions.MyException;
import com.example.conditional_assignment.Model.Expression.IExpression;
import com.example.conditional_assignment.Model.ProgramState.ProgramState;
import com.example.conditional_assignment.Model.Type.BoolType;
import com.example.conditional_assignment.Model.Type.IType;
import com.example.conditional_assignment.Model.Value.BoolValue;
import com.example.conditional_assignment.Model.Value.IValue;

public class WhileStatement implements IStatement{
    private IExpression expression;
    private IStatement statement;

    public WhileStatement(IExpression expression, IStatement statement) {
        this.expression = expression;
        this.statement = statement;
    }

    @Override
    public ProgramState execute(ProgramState currentState) throws MyException {
        IValue conditional = this.expression.eval(currentState.getSymbolTable(), currentState.getHeapTable());
        if (!conditional.getType().equals(new BoolType())) {
            throw new MyException("ERROR: The given while conditional expression(" + this.expression.toString() + ") is not a boolean.");
        }
        BoolValue boolConditional = (BoolValue) conditional;
        if (boolConditional.getValue()) {
            currentState.getExecutionStack().push(this);
            currentState.getExecutionStack().push(this.statement);
        }
        return null;
    }

    @Override
    public IStatement deepCopy() {
        return new WhileStatement(this.expression.deepCopy(), this.statement.deepCopy());
    }

    @Override
    public IDictionary<String, IType> typeCheck(IDictionary<String, IType> typeEnv) throws MyException {
        IType typeExp = this.expression.typeCheck(typeEnv);
        if (typeExp.equals(new BoolType())) {
            this.statement.typeCheck(typeEnv.shallowCopy());
            return typeEnv;
        } else {
            throw new MyException("TYPE CHECK ERROR: The condition of WHILE is not of type bool.");
        }
    }

    @Override
    public String toString() {
        return "While (" + this.expression.toString() + ") { " + this.statement.toString() + " }";
    }
}
