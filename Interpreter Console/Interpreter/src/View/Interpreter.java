package View;

import Exceptions.ADTException;
import Exceptions.ExpressionEvaluationException;
import Exceptions.StatementExecutionException;
import Model.ADT.Dictionary.MyDictionary;
import Model.Program.IProgramHandler;
import Model.Statement.*;
import Controller.Controller;
import Repository.IRepository;
import Repository.Repository;

import java.util.*;

public class Interpreter implements IProgramHandler {
    public static void main(String[] args) {
        IRepository repository = new Repository();
        Controller controller = new Controller(repository);

        TextMenu menu = new TextMenu();
        menu.addCommand(new ExitCommand("Exit"));
        menu.addCommand(new ToggleStepsCommand("Toggle debugging", controller));
        menu.addCommand(new ChangeGCCommand("Switch garbage collector", controller));

        List<IStatement> goodPrograms = new ArrayList<>();
        Map<IStatement, String> badPrograms = new LinkedHashMap<>();

        for (IStatement statement : PROGRAMS) {
            try {
                statement.typeCheck(new MyDictionary<>());
                goodPrograms.add(statement);
            }
            catch (ADTException | ExpressionEvaluationException | StatementExecutionException e) {
                badPrograms.put(statement, e.getMessage());
            }
        }

        // Preload the programs
        for (IStatement statement : goodPrograms) {
            menu.addCommand(new RunExampleCommand(controller, statement));
        }

        // Display the bad programs
        int current = 1;
        System.out.println("The following programs were omitted from the menu for failing the type checker:");
        for (Map.Entry<IStatement, String> entry : badPrograms.entrySet()) {
            System.out.println("[" + current++ +"] " + entry.getKey());
            System.out.println("Exception: " + entry.getValue() + "\n");
        }

        menu.show();
    }
}
