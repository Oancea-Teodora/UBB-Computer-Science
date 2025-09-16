package com.example.conditional_assignment.Model.Statement;

import com.example.conditional_assignment.Model.ADT.IDictionary;
import com.example.conditional_assignment.Model.Exceptions.MyException;
import com.example.conditional_assignment.Model.Expression.IExpression;
import com.example.conditional_assignment.Model.ProgramState.ProgramState;
import com.example.conditional_assignment.Model.Type.BoolType;
import com.example.conditional_assignment.Model.Type.IType;

public class ConditionalAssignment implements IStatement{
    private String varName;
    private IExpression expr1;
    private IExpression expr2;
    private IExpression expr3;

    public ConditionalAssignment(String varName, IExpression expr1, IExpression expr2, IExpression expr3) {
        this.varName = varName;
        this.expr1 = expr1;
        this.expr2 = expr2;
        this.expr3 = expr3;
    }

    @Override
    public ProgramState execute(ProgramState currentState) throws MyException {
        AssignStatement thenStmt = new AssignStatement(this.varName, this.expr2);
        AssignStatement elseStmt = new AssignStatement(this.varName, this.expr3);
        currentState.getExecutionStack().push(new IfStatement(this.expr1, thenStmt, elseStmt));
        return null;
    }

    @Override
    public IStatement deepCopy() {
        return new ConditionalAssignment(this.varName, this.expr1.deepCopy(), this.expr2.deepCopy(), this.expr3.deepCopy());
    }

    @Override
    public IDictionary<String, IType> typeCheck(IDictionary<String, IType> typeEnv) throws MyException {
        IType expr1Type = this.expr1.typeCheck(typeEnv);
        if (!expr1Type.equals(new BoolType())) {
            throw new MyException("TYPE CHECK ERROR: The condition given in the conditional assignment (" + this.expr1.toString() + ") is not a boolean.");
        }

        IType vType = typeEnv.lookUp(this.varName);
        IType expr2Type = this.expr2.typeCheck(typeEnv);
        IType expr3Type = this.expr3.typeCheck(typeEnv);
        if (vType.equals(expr2Type) && expr2Type.equals(expr3Type)) {
            return typeEnv;
        } else {
            throw new MyException("TYPE CHECK ERROR: The type of the variable (" + this.varName + ") and the expressions (" +
                    this.expr2.toString() + "/" + this.expr3.toString() + ") in the Conditional Assignment do not match.");
        }
    }

    @Override
    public String toString() {
        return this.varName + " = " + this.expr1.toString() + " ? " + this.expr2.toString() + " : " + this.expr3.toString();
    }
}
