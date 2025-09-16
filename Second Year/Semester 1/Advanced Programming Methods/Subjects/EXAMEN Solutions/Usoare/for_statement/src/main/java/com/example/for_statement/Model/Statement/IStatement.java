package com.example.for_statement.Model.Statement;

import com.example.for_statement.Model.ADT.IDictionary;
import com.example.for_statement.Model.Exceptions.MyException;
import com.example.for_statement.Model.ProgramState.ProgramState;
import com.example.for_statement.Model.Type.IType;

public interface IStatement {
    ProgramState execute(ProgramState currentState) throws MyException;
    IStatement deepCopy();
    IDictionary<String, IType> typeCheck(IDictionary<String, IType> typeEnv) throws MyException;
}
