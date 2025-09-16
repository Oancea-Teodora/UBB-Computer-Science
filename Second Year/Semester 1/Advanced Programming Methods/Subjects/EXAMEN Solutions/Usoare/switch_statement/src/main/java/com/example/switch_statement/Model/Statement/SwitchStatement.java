package com.example.switch_statement.Model.Statement;

import com.example.switch_statement.Model.ADT.IDictionary;
import com.example.switch_statement.Model.Exceptions.MyException;
import com.example.switch_statement.Model.Expression.IExpression;
import com.example.switch_statement.Model.Expression.RelationalExpression;
import com.example.switch_statement.Model.ProgramState.ProgramState;
import com.example.switch_statement.Model.Type.IType;

public class SwitchStatement implements IStatement{
    private IExpression exp, exp1, exp2;
    private IStatement stmt1, stmt2, stmt3;

    public SwitchStatement(IExpression exp, IExpression exp1, IStatement stmt1, IExpression exp2, IStatement stmt2, IStatement stmt3) {
        this.exp = exp;
        this.exp1 = exp1;
        this.stmt1 = stmt1;
        this.exp2 = exp2;
        this.stmt2 = stmt2;
        this.stmt3 = stmt3;
    }

    @Override
    public ProgramState execute(ProgramState currentState) throws MyException {
        RelationalExpression ifCond1 = new RelationalExpression(this.exp, this.exp1, "==");
        RelationalExpression ifCond2 = new RelationalExpression(this.exp, this.exp2, "==");

        IfStatement innerIfStmt = new IfStatement(ifCond2, this.stmt2, this.stmt3);
        IfStatement outerIfStmt = new IfStatement(ifCond1, this.stmt1, innerIfStmt);

        currentState.getExecutionStack().push(outerIfStmt);
        return null;
    }

    @Override
    public IStatement deepCopy() {
        return new SwitchStatement(this.exp.deepCopy(), this.exp1.deepCopy(), this.stmt1.deepCopy(), this.exp2.deepCopy(),
                this.stmt2.deepCopy(), this.stmt3.deepCopy());
    }

    @Override
    public IDictionary<String, IType> typeCheck(IDictionary<String, IType> typeEnv) throws MyException {
        IType expType = this.exp.typeCheck(typeEnv);
        IType exp1Type = this.exp1.typeCheck(typeEnv);
        IType exp2Type = this.exp2.typeCheck(typeEnv);
        if (!(expType.equals(exp1Type) && exp1Type.equals(exp2Type))) {
            throw new MyException("TYPE CHECK ERROR: The comparison expressions given in the switch statement (" +
                    this.exp.toString() + ", " + this.exp1.toString() + ", " + this.exp2.toString() + " given) do not match in type.");
        }

        this.stmt1.typeCheck(typeEnv.shallowCopy());
        this.stmt2.typeCheck(typeEnv.shallowCopy());
        this.stmt3.typeCheck(typeEnv.shallowCopy());
        return typeEnv;
    }

    @Override
    public String toString() {
        return "switch(" + this.exp.toString() + ") (case " + this.exp1.toString() + ": " + this.stmt1.toString() + ") " +
                "(case " + this.exp2.toString() + ": " + this.stmt2.toString() + ") (default: " + this.stmt3.toString() + ")";
    }
}
