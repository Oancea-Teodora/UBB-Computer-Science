package com.example.switch_statement.Model.Statement;

import com.example.switch_statement.Model.ADT.IDictionary;
import com.example.switch_statement.Model.Exceptions.MyException;
import com.example.switch_statement.Model.ProgramState.ProgramState;
import com.example.switch_statement.Model.Type.IType;

public interface IStatement {
    ProgramState execute(ProgramState currentState) throws MyException;
    IStatement deepCopy();
    IDictionary<String, IType> typeCheck(IDictionary<String, IType> typeEnv) throws MyException;
}
