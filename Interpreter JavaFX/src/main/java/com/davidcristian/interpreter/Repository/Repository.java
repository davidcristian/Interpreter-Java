package com.davidcristian.interpreter.Repository;

import com.davidcristian.interpreter.Model.Program.ProgramState;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Repository implements IRepository {
    private static final String LOG_ROOT = "logs/";
    private String logName;

    private List<ProgramState> prgStates;

    public Repository() {
        this.prgStates = new ArrayList<>();
        this.setLogName("log.txt");
    }

    public void setLogName(String logName) {
        this.logName = Repository.LOG_ROOT + logName;
    }

    @Override
    public List<ProgramState> getProgramList() {
        return this.prgStates;
    }

    @Override
    public void setProgramList(List<ProgramState> list) {
        this.prgStates = list;
    }

    @Override
    public void addProgram(ProgramState program) {
        this.prgStates.add(program);
    }

    @Override
    public void logProgramStateExecution(ProgramState program) throws IOException {
        PrintWriter logFile;
        try {
            logFile = new PrintWriter(new BufferedWriter(new FileWriter(logName, true)));
        } catch (IOException e) {
            throw new IOException("Could not open log file!");
        }

        logFile.println(program.toString() + "\n");
        logFile.close();
    }

    @Override
    public void emptyLogFile() throws IOException {
        PrintWriter logFile;
        try {
            logFile = new PrintWriter(new BufferedWriter(new FileWriter(logName, false)));
        } catch (IOException e) {
            throw new IOException("Could not empty log file!");
        }

        logFile.close();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        for(ProgramState state : this.prgStates) {
            sb.append(state.toString());
        }

        //sb.append("\n\n");
        return sb.toString();
    }
}
