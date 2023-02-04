package View;

import Exceptions.*;
import Model.ADT.Stack.*;
import Model.ADT.List.*;
import Model.ADT.Dictionary.*;
import Model.ADT.Heap.*;
import Model.Program.*;
import Model.Expression.*;
import Model.Statement.*;
import Model.Type.*;
import Model.Value.*;
import Controller.Controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class oldView {
    private static final Scanner scanner = new Scanner(System.in);
    private static final String[] MENU = new String[] {
            "0. Exit",
            "1. Toggle program step display",
            "2. Input a program",
            "3. Execute selected program",
    };

    private static final IStatement[] PROGRAMS = new IStatement[] {
            new CompoundStatement(new VariableDeclarationStatement("v",new IntType()),new CompoundStatement(new AssignmentStatement("v",new ValueExpression(new IntValue(2))),new PrintStatement(new VariableExpression("v")))),
            new CompoundStatement(new VariableDeclarationStatement("a",new IntType()),new CompoundStatement(new VariableDeclarationStatement("b",new IntType()),new CompoundStatement(new AssignmentStatement("a",new ArithmeticExpression('+',new ValueExpression(new IntValue(2)),new ArithmeticExpression('*',new ValueExpression(new IntValue(3)),new ValueExpression(new IntValue(5))))),new CompoundStatement(new AssignmentStatement("b",new ArithmeticExpression('+',new VariableExpression("a"),new ValueExpression(new IntValue(1)))),new PrintStatement(new VariableExpression("b")))))),
            new CompoundStatement(new VariableDeclarationStatement("a",new BoolType()),new CompoundStatement(new VariableDeclarationStatement("v",new IntType()),new CompoundStatement(new AssignmentStatement("a",new ValueExpression(new BoolValue(true))),new CompoundStatement(new IfStatement(new VariableExpression("a"),new AssignmentStatement("v",new ValueExpression(new IntValue(2))),new AssignmentStatement("v",new ValueExpression(new IntValue(3)))),new PrintStatement(new VariableExpression("v")))))),
    };
    private IStatement currentProgram = null;
    private final Controller controller;

    public oldView(Controller controller) {
        this.controller = controller;
    }

    public void start() {
        while (true) {
            displayMenu();
            int option = readInteger("Option: ", 0, MENU.length - 1);

            try {
                switch (option) {
                    case 0 -> {
                        System.out.println("INFO: Quitting.");
                        return;
                    }
                    case 1 -> toggleStepDisplay();
                    case 2 -> inputProgram();
                    case 3 -> executeProgram();
                    default -> System.out.println("ERROR: Unknown option.");
                }
            }
            catch (StatementExecutionException | ExpressionEvaluationException | ADTException | IOException | InterruptedException e) {
                System.out.println("ERROR: " + e.getMessage());
            }

            System.out.print("\nPress <ENTER> to continue");
            scanner.nextLine();
        }
    }

    private void displayMenu() {
        for (int i = 0; i < 100; i++) {
            System.out.println();
        }

        for (String entry : MENU) {
            System.out.println(entry);
        }
    }

    private void inputProgram() {
        System.out.println("Input a program:");
        System.out.println("1. int v; v=2; Print(v)");
        System.out.println("2. int a; int b; a=2+3*5; b=a+1; Print(b)");
        System.out.println("3. bool a; int v; a=true; (If a Then v=2 Else v=3); Print(v)");
        System.out.println();

        int option = readInteger("Option: ", 1, 3);
        this.currentProgram = PROGRAMS[option - 1].deepCopy();

        System.out.println("You have selected program " + option + ".");
    }

    private void executeProgram() throws StatementExecutionException, ExpressionEvaluationException, ADTException, java.io.IOException, InterruptedException {
        if (this.currentProgram == null) {
            System.out.println("ERROR: No program selected.");
            return;
        }

        List<ProgramState> programList = new ArrayList<>();
        ProgramState prgState = new ProgramState(new MyStack<>(), new MyDictionary<>(), new MyList<>(), new MyDictionary<>(), new MyHeap<>(), this.currentProgram);

        programList.add(prgState);
        controller.setProgramList(programList);

        if (controller.getShowSteps()) {
            controller.createExecutor();
            programList = controller.getUnfinishedPrograms();

            while (programList.size() > 0) {
                System.out.println();
                controller.getUnfinishedPrograms().forEach(programState -> System.out.println(programState + "\n"));

                try {
                    controller.oneStep(programList);
                } catch (InterruptedException e) {
                    throw new InterruptedException();
                }

                programList = controller.getUnfinishedPrograms();
            }

            controller.killExecutor();
            controller.setProgramList(programList);
        } else {
            controller.allSteps();
        }

        System.out.println("\nOutput: " + prgState.getOutput().toString().replaceAll("\\{", "").replaceAll("}", ""));

        this.currentProgram = null;
    }

    private void toggleStepDisplay() {
        controller.setShowSteps(!controller.getShowSteps());
        System.out.println("Program step display is now " + (controller.getShowSteps() ? "enabled" : "disabled") + ".");
    }

    public static int readInteger(String prompt, int min, int max) {
        while (true) {
            System.out.print(prompt);

            try {
                int value = Integer.parseInt(scanner.nextLine());
                if (value < min || value > max) {
                    throw new NumberFormatException("Value out of range!");
                }

                return value;
            }
            catch (NumberFormatException e) {
                System.out.print("ERROR: Invalid input.\n\n");
            }
        }
    }
}
