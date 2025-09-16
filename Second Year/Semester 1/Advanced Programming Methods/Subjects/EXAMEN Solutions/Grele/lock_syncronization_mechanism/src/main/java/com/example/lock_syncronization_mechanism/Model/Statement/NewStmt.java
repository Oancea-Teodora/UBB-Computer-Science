package com.example.lock_syncronization_mechanism.Model.Statement;

import com.example.lock_syncronization_mechanism.Model.ADT.IDictionary;
import com.example.lock_syncronization_mechanism.Model.Exceptions.MyException;
import com.example.lock_syncronization_mechanism.Model.Expression.IExpression;
import com.example.lock_syncronization_mechanism.Model.ProgramState.ProgramState;
import com.example.lock_syncronization_mechanism.Model.Type.IType;
import com.example.lock_syncronization_mechanism.Model.Type.RefType;
import com.example.lock_syncronization_mechanism.Model.Value.IValue;
import com.example.lock_syncronization_mechanism.Model.Value.RefValue;

public class NewStmt implements IStatement{
    private String variableName;
    private IExpression expression;

    public NewStmt(String varName, IExpression exp) {
        this.variableName = varName;
        this.expression = exp;
    }

    @Override
    public ProgramState execute(ProgramState currentState) throws MyException {
        if (!currentState.getSymbolTable().isDefined(this.variableName)) {
            throw new MyException("ERROR: The given variable(" + this.variableName + ") is not defined in the symbol table.");
        }
        IValue variableValue = currentState.getSymbolTable().lookUp(this.variableName);
        if (!(variableValue.getType().equals(new RefType(null)))) {
            throw new MyException("ERROR: The given variable(" + this.variableName + ") is not a reference type.");
        }
        RefValue refValue = (RefValue) variableValue;
        IValue expressionValue = this.expression.eval(currentState.getSymbolTable(), currentState.getHeapTable());
        if (!expressionValue.getType().equals(refValue.getLocationType())) {
            throw new MyException("ERROR: The type of the expression's value does not match with the locationType.");
        }
        int addressUsed = currentState.getHeapTable().addNewHeapEntry(expressionValue);
        currentState.getSymbolTable().addKeyValuePair(this.variableName, new RefValue(addressUsed, refValue.getLocationType()));
        return null;
    }

    @Override
    public IStatement deepCopy() {
        return new NewStmt(this.variableName, this.expression);
    }

    @Override
    public IDictionary<String, IType> typeCheck(IDictionary<String, IType> typeEnv) throws MyException {
        IType typeVar = typeEnv.lookUp(this.variableName);
        IType typeExp = this.expression.typeCheck(typeEnv);
        if (typeVar.equals(new RefType(typeExp))) {
            return typeEnv;
        } else {
            throw new MyException("TYPE CHECK ERROR: Right hand side and left hand side in NEW statement have different types.");
        }
    }

    @Override
    public String toString() {
        return "NewStmt(" + this.variableName + ", " + this.expression.toString() + ")";
    }
}
