package com.example.conditional_assignment.Model.Statement;

import com.example.conditional_assignment.Model.ADT.IDictionary;
import com.example.conditional_assignment.Model.Exceptions.MyException;
import com.example.conditional_assignment.Model.Expression.IExpression;
import com.example.conditional_assignment.Model.ProgramState.ProgramState;
import com.example.conditional_assignment.Model.Type.BoolType;
import com.example.conditional_assignment.Model.Type.IType;
import com.example.conditional_assignment.Model.Value.BoolValue;
import com.example.conditional_assignment.Model.Value.IValue;

public class IfStatement implements IStatement {
    private IExpression expression;
    private IStatement thenStatement;
    private IStatement elseStatement;

    public IfStatement(IExpression expression, IStatement thenStatement, IStatement elseStatement) {
        this.expression = expression;
        this.thenStatement = thenStatement;
        this.elseStatement = elseStatement;
    }

    @Override
    public ProgramState execute(ProgramState currentState) throws MyException {
        IValue conditional = this.expression.eval(currentState.getSymbolTable(), currentState.getHeapTable());
        if (conditional.getType().equals(new BoolType())) {
            BoolValue boolConditional = (BoolValue) conditional;
            if (boolConditional.getValue()) {
                currentState.getExecutionStack().push(this.thenStatement);
            } else {
                currentState.getExecutionStack().push(this.elseStatement);
            }
        } else {
            throw new MyException("Conditional expression is not a boolean.");
        }
        return null;
    }

    @Override
    public IStatement deepCopy() {
        return new IfStatement(this.expression.deepCopy(), this.thenStatement.deepCopy(), this.elseStatement.deepCopy());
    }

    @Override
    public IDictionary<String, IType> typeCheck(IDictionary<String, IType> typeEnv) throws MyException {
        IType typeExp = this.expression.typeCheck(typeEnv);
        if (typeExp.equals(new BoolType())) {
            this.thenStatement.typeCheck(typeEnv.shallowCopy());
            this.elseStatement.typeCheck(typeEnv.shallowCopy());
            return typeEnv;
        } else {
            throw new MyException("TYPE CHECK ERROR: The condition of IF is not of type bool.");
        }
    }

    @Override
    public String toString() {
        return "IF (" + this.expression.toString() + ") " +
                "THEN (" + this.thenStatement.toString() + ") " +
                "ELSE (" + this.elseStatement.toString() + ")";
    }
}
