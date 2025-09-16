package com.example.for_statement.Repository;

import com.example.for_statement.Model.Exceptions.MyException;
import com.example.for_statement.Model.ProgramState.ProgramState;

import java.util.List;

public interface IRepository {
    List<ProgramState> getPrgList();
    void setPrgList(List<ProgramState> newProgramStates);
    void addProgramState(ProgramState newProgramState);
    void logPrgStateExec(ProgramState programState) throws MyException;
    ProgramState getProgramStateById(int id) throws MyException;
}
