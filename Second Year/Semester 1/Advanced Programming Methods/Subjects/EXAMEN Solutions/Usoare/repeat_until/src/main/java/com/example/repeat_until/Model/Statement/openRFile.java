package com.example.repeat_until.Model.Statement;

import com.example.repeat_until.Model.ADT.IDictionary;
import com.example.repeat_until.Model.Exceptions.MyException;
import com.example.repeat_until.Model.Expression.IExpression;
import com.example.repeat_until.Model.ProgramState.ProgramState;
import com.example.repeat_until.Model.Type.IType;
import com.example.repeat_until.Model.Type.StringType;
import com.example.repeat_until.Model.Value.IValue;
import com.example.repeat_until.Model.Value.StringValue;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;

public class openRFile implements IStatement{
    private IExpression expression;

    public openRFile(IExpression e) {
        this.expression = e;
    }

    @Override
    public ProgramState execute(ProgramState currentState) throws MyException {
        IValue val = this.expression.eval(currentState.getSymbolTable(), currentState.getHeapTable());
        if (val.getType().equals(new StringType())) {
            StringValue stringValue = (StringValue) val;
            if (currentState.getFileTable().isDefined(stringValue)) {
                throw new MyException("The given string value already is a key in the file table.");
            }
            try {
                BufferedReader fileDescriptor = new BufferedReader(new FileReader(stringValue.getValue()));
                currentState.getFileTable().addKeyValuePair(stringValue, fileDescriptor);
            } catch (FileNotFoundException e) {
                throw new MyException("The given file (" + stringValue.getValue() + ") was not found");
            }
        }
        else {
            throw new MyException("The given expression is not evaluated to a string");
        }
        return null;
    }

    @Override
    public String toString() {
        return "openFile " + this.expression.toString();
    }

    @Override
    public IStatement deepCopy() {
        return new openRFile(this.expression.deepCopy());
    }

    @Override
    public IDictionary<String, IType> typeCheck(IDictionary<String, IType> typeEnv) throws MyException {
        IType typeExp = this.expression.typeCheck(typeEnv);
        if (typeExp.equals(new StringType())) {
            return typeEnv;
        } else {
            throw new MyException("TYPE CHECK ERROR: The given file name is not of type String.");
        }
    }
}
