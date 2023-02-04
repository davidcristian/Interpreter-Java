package View;

import Controller.Controller;
import Exceptions.ADTException;
import Exceptions.ExpressionEvaluationException;
import Exceptions.StatementExecutionException;
import Model.ADT.Dictionary.MyDictionary;
import Model.ADT.Heap.MyHeap;
import Model.ADT.List.MyList;
import Model.ADT.Stack.MyStack;
import Model.Program.ProgramState;
import Model.Statement.IStatement;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class RunExampleCommand extends Command {
    private final Controller controller;
    private final IStatement statement;
    private final List<ProgramState> programs;

    private boolean firstStep = true;
    private static final String MOTD = "For help, type \"help\"";
    private static final String PROMPT = "(cmd) ";
    private static final String UNKNOWN = "Undefined command: \"%s\". Try \"help\".";

    private static final String[] COMMANDS = new String[] {
            "c",
            "", // acts as 'c', this is temporary to speed things up
            "b"
    };

    private static final String[] HELP_TEXT = new String[] {
            "help - shows the help text",
            "c - continue execution",
            "b - pauses the execution of the program",
    };

    public RunExampleCommand(Controller controller, IStatement statement) {
        super(statement.toString());
        this.controller = controller;

        programs = new ArrayList<>();
        this.statement = statement;
    }

    private void resetProgram() {
        programs.clear();

        ProgramState program = new ProgramState(new MyStack<>(), new MyDictionary<>(), new MyList<>(), new MyDictionary<>(), new MyHeap<>(), this.statement);
        programs.add(program);

        controller.setProgramList(programs);
    }

    private String getCMD() {
        if (firstStep) {
            System.out.println(MOTD);
            firstStep = false;
        }

        System.out.print(PROMPT);
        String command = scanner.nextLine().toLowerCase().trim();

        if (command.equals("help")) {
            for (String help : HELP_TEXT) {
                System.out.println(help);
            }

            return "null";
        }

        // check if command is in COMMANDS
        for (String cmd : COMMANDS) {
            if (cmd.equals(command)) {
                return command;
            }
        }

        System.out.println(String.format(UNKNOWN, command));
        return "null";
    }

    private String formatOutput(ProgramState program) {
        return "\nOutput: " + program.getOutput().toString().replaceAll("\\{", "").replaceAll("}", "");
    }

    private String runEntireProgram(ProgramState program) throws ADTException, StatementExecutionException, ExpressionEvaluationException, IOException, InterruptedException {
        controller.allSteps();
        return formatOutput(program);
    }

    private boolean handleDebugStep() {
        System.out.println();
        controller.getUnfinishedPrograms().forEach(programState -> {
            System.out.println(programState + "\n");
        });

        String command = "null";
        while (command.equals("null"))
            command = getCMD();

        return command.equals("b");
    }

    private String runOneStep(ProgramState program) throws ADTException, StatementExecutionException, ExpressionEvaluationException, IOException, InterruptedException {
        controller.createExecutor();
        List<ProgramState> programList = controller.getUnfinishedPrograms();

        boolean debugBreak = false;
        while (programList.size() > 0) {
            if (handleDebugStep()) {
                debugBreak = true;
                break;
            }

            try {
                controller.oneStep(programList);
            } catch (InterruptedException e) {
                throw new InterruptedException();
            }

            programList = controller.getUnfinishedPrograms();
        }

        controller.killExecutor();
        controller.setProgramList(programList);

        if (debugBreak)
            return "Program execution paused.";

        return formatOutput(program);
    }

    private void checkPrograms() throws ADTException, IOException {
        // Program was already executed, reset it
        if (controller.getUnfinishedPrograms().size() == 0) {
            controller.getRepository().emptyLogFile();
            resetProgram();
            return;
        }

        // program is original
        if (programs.get(0).isOriginal()) {
            controller.getRepository().emptyLogFile();
            return;
        }

        // else, program is in the middle of execution, ask if we should resume
        String choice = "";

        while (true) {
            System.out.print("\nResume program execution? (y/n) ");
            choice = scanner.nextLine().toLowerCase();

            // empty is also accepted, counts as yes
            if (choice.length() == 0)
                choice = "y";

            if ("yes".startsWith(choice) || "no".startsWith(choice))
                break;

            System.out.println("ERROR: Invalid choice!\n");
        }

        if ("no".startsWith(choice)) {
            controller.getRepository().emptyLogFile();
            resetProgram();
        }
    }

    @Override
    public void execute() {
        try {
            controller.setProgramList(programs);

            // Overwrite log file name
            controller.getRepository().setLogName("log" + this.getKey() + ".txt");

            checkPrograms();
            ProgramState program = programs.get(0);

            String result;
            if (!controller.getShowSteps()) {
                result = this.runEntireProgram(program);
            }
            else {
                result = this.runOneStep(program);
            }

            System.out.println(result);
        } catch (ExpressionEvaluationException | ADTException | StatementExecutionException | IOException | InterruptedException e) {
            System.out.println("ERROR: " + e.getMessage());
        }
    }
}
