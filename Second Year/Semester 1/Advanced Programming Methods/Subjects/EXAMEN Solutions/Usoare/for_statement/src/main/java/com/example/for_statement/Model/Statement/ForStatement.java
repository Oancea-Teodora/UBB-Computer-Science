package com.example.for_statement.Model.Statement;

import com.example.for_statement.Model.ADT.IDictionary;
import com.example.for_statement.Model.Exceptions.MyException;
import com.example.for_statement.Model.Expression.IExpression;
import com.example.for_statement.Model.ProgramState.ProgramState;
import com.example.for_statement.Model.Type.BoolType;
import com.example.for_statement.Model.Type.IType;

public class ForStatement implements IStatement{
    IStatement initialValue;
    IExpression comparisonExpression;
    IStatement increment;
    IStatement actionStatement;

    public ForStatement(IStatement initialValue, IExpression comparisonExpression, IStatement increment, IStatement actionStatement) {
        this.initialValue = initialValue;
        this.comparisonExpression = comparisonExpression;
        this.increment = increment;
        this.actionStatement = actionStatement;
    }

    @Override
    public ProgramState execute(ProgramState currentState) throws MyException {
        IStatement stmt1 = this.initialValue;
        WhileStatement stmt2 = new WhileStatement(this.comparisonExpression, new CompoundStatement(this.actionStatement, this.increment));
        CompoundStatement newStmt = new CompoundStatement(stmt1, stmt2);
        currentState.getExecutionStack().push(newStmt);
        return null;
    }

    @Override
    public IStatement deepCopy() {
        return new ForStatement(this.initialValue.deepCopy(), this.comparisonExpression.deepCopy(), this.increment.deepCopy(), this.actionStatement.deepCopy());
    }

    @Override
    public IDictionary<String, IType> typeCheck(IDictionary<String, IType> typeEnv) throws MyException {
        IType comparisonExpressionType = this.comparisonExpression.typeCheck(typeEnv);
        if (!comparisonExpressionType.equals(new BoolType())) {
            throw new MyException("TYPE CHECK ERROR: The condition of FOR is not of type bool.");
        }
        this.initialValue.typeCheck(typeEnv);
        this.increment.typeCheck(typeEnv);
        this.actionStatement.typeCheck(typeEnv.shallowCopy());
        return typeEnv;
    }

    @Override
    public String toString() {
        return "for(" + this.initialValue.toString() + "; " + this.comparisonExpression.toString() + "; " + this.increment.toString() + ") " + this.actionStatement.toString();
    }
}
