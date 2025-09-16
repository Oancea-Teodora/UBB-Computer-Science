package toy_interpreter.lab11_project.Repository;

import toy_interpreter.lab11_project.Model.Exceptions.MyException;
import toy_interpreter.lab11_project.Model.ProgramState.ProgramState;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class Repository implements IRepository {
    private ArrayList<ProgramState> elems;
    private String logFilePath;
    boolean firstTimeWriting;

    public Repository(ProgramState prgState, String logFilePath) {
        this.elems = new ArrayList<ProgramState>();
        this.elems.add(prgState);
        this.logFilePath = logFilePath;
        this.firstTimeWriting = true;
    }

    @Override
    public List<ProgramState> getPrgList() {
        return this.elems;
    }

    @Override
    public void setPrgList(List<ProgramState> newProgramStates) {
        this.elems.clear();
        this.elems.addAll(newProgramStates);
    }

    @Override
    public void addProgramState(ProgramState newProgramState) {
        this.elems.set(0, newProgramState);
    }

    @Override
    public void logPrgStateExec(ProgramState programState) throws MyException {
        PrintWriter logFile;
        try {
            if (this.firstTimeWriting) {
                logFile = new PrintWriter(new BufferedWriter(new FileWriter(this.logFilePath, false)));
                this.firstTimeWriting = false;
            }
            else {
                logFile = new PrintWriter(new BufferedWriter(new FileWriter(this.logFilePath, true)));
            }
        }
        catch (IOException e) {
            throw new MyException("The file cannot be opened/created/doesn't exist.");
        }
        logFile.println(programState.toString());
        logFile.flush();
        logFile.close();
    }

    @Override
    public ProgramState getProgramStateById(int id) throws MyException {
        for (ProgramState programState: this.elems) {
            if (programState.getId() == id) {
                return programState;
            }
        }
        throw new MyException("ERROR: No program state with ID " + id + " in the repository.");
    }
}
