package com.example.repeat_until.Model.Statement;

import com.example.repeat_until.Model.ADT.IDictionary;
import com.example.repeat_until.Model.Exceptions.MyException;
import com.example.repeat_until.Model.ProgramState.ProgramState;
import com.example.repeat_until.Model.Type.IType;

public interface IStatement {
    ProgramState execute(ProgramState currentState) throws MyException;
    IStatement deepCopy();
    IDictionary<String, IType> typeCheck(IDictionary<String, IType> typeEnv) throws MyException;
}
