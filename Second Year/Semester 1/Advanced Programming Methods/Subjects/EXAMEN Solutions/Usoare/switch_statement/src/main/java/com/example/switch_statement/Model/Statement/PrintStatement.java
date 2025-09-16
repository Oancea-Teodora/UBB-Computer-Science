package com.example.switch_statement.Model.Statement;

import com.example.switch_statement.Model.ADT.IDictionary;
import com.example.switch_statement.Model.Exceptions.MyException;
import com.example.switch_statement.Model.Expression.IExpression;
import com.example.switch_statement.Model.ProgramState.ProgramState;
import com.example.switch_statement.Model.Type.IType;
import com.example.switch_statement.Model.Value.IValue;

public class PrintStatement implements IStatement{
    private IExpression expression;

    public PrintStatement(IExpression e) {
        this.expression = e;
    }

    @Override
    public ProgramState execute(ProgramState currentState) throws MyException {
        IValue expressionValue = this.expression.eval(currentState.getSymbolTable(), currentState.getHeapTable());
        currentState.getOutput().add(expressionValue);
        return null;
    }

    @Override
    public IStatement deepCopy() {
        return new PrintStatement(this.expression.deepCopy());
    }

    @Override
    public IDictionary<String, IType> typeCheck(IDictionary<String, IType> typeEnv) throws MyException {
        this.expression.typeCheck(typeEnv);
        return typeEnv;
    }

    @Override
    public String toString() {
        return "print(" + this.expression.toString() + ")";
    }
}
