package com.davidcristian.interpreter.Repository;

import java.io.IOException;
import java.util.List;

import com.davidcristian.interpreter.Exceptions.ADTException;
import com.davidcristian.interpreter.Model.Program.ProgramState;

public interface IRepository {
    List<ProgramState> getProgramList();
    void setProgramList(List<ProgramState> list);
    void addProgram(ProgramState state);

    void setLogName(String logName);
    void logProgramStateExecution(ProgramState programState) throws IOException, ADTException;
    void emptyLogFile() throws IOException;
}
