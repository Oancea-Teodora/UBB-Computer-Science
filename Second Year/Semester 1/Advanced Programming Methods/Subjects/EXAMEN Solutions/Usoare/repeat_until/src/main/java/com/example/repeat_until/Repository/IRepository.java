package com.example.repeat_until.Repository;

import com.example.repeat_until.Model.Exceptions.MyException;
import com.example.repeat_until.Model.ProgramState.ProgramState;

import java.util.List;

public interface IRepository {
    List<ProgramState> getPrgList();
    void setPrgList(List<ProgramState> newProgramStates);
    void addProgramState(ProgramState newProgramState);
    void logPrgStateExec(ProgramState programState) throws MyException;
    ProgramState getProgramStateById(int id) throws MyException;
}
