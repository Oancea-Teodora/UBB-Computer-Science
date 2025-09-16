package com.example.repeat_until.Model.Statement;

import com.example.repeat_until.Model.ADT.IDictionary;
import com.example.repeat_until.Model.Exceptions.MyException;
import com.example.repeat_until.Model.Expression.IExpression;
import com.example.repeat_until.Model.Expression.NotExpression;
import com.example.repeat_until.Model.ProgramState.ProgramState;
import com.example.repeat_until.Model.Type.BoolType;
import com.example.repeat_until.Model.Type.IType;

public class RepeatUntil implements IStatement{
    IStatement stmt;
    IExpression expr;

    public RepeatUntil(IStatement stmt, IExpression expr) {
        this.stmt = stmt;
        this.expr = expr;
    }

    @Override
    public ProgramState execute(ProgramState currentState) throws MyException {
        WhileStatement whileStatement = new WhileStatement(new NotExpression(this.expr), this.stmt);
        currentState.getExecutionStack().push(new CompoundStatement(this.stmt, whileStatement));
        return null;
    }

    @Override
    public IStatement deepCopy() {
        return new RepeatUntil(this.stmt.deepCopy(), this.expr.deepCopy());
    }

    @Override
    public IDictionary<String, IType> typeCheck(IDictionary<String, IType> typeEnv) throws MyException {
        IType exprType = this.expr.typeCheck(typeEnv);
        if (!exprType.equals(new BoolType())) {
            throw new MyException("The given RepeatUntil condition (" + this.expr.toString() + ")does not evaluate to a boolean.");
        }
        this.stmt.typeCheck(typeEnv.shallowCopy());
        return typeEnv;
    }

    @Override
    public String toString() {
        return "Repeat(" + this.stmt.toString() + ") Until(" + this.expr.toString() + ")";
    }
}
