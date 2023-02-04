package Repository;

import java.io.IOException;
import java.util.List;

import Exceptions.ADTException;
import Model.Program.ProgramState;

public interface IRepository {
    List<ProgramState> getProgramList();
    void setProgramList(List<ProgramState> list);
    void addProgram(ProgramState state);

    void setLogName(String logName);
    void logProgramStateExecution(ProgramState programState) throws IOException, ADTException;
    void emptyLogFile() throws IOException;
}
