package View;

import Controller.Controller;
import Controller.GarbageCollectorType;

import java.util.LinkedHashMap;
import java.util.Map;

public class ChangeGCCommand extends Command {
    private final Controller controller;

    Map<String, GarbageCollectorType> garbageCollectors;

    public ChangeGCCommand(String description, Controller controller) {
        super(description);
        this.controller = controller;

        garbageCollectors = new LinkedHashMap<>();
        for (GarbageCollectorType gc : GarbageCollectorType.values()) {
            garbageCollectors.put(gc.toString(), gc);
        }
    }

    private void printOptions() {
        int i = 0;
        for (String key : garbageCollectors.keySet()) {
            System.out.println(++i + ". " + key);
        }
    }

    @Override
    public void execute() {
        System.out.println("\nCurrent GC: " + controller.getGC().toString());
        System.out.println("Select new garbage collector: ");
        printOptions();

        int option = 0;
        while (option < 1 || option > garbageCollectors.size()) {
            System.out.print("Option: ");

            try {
                option = Integer.parseInt(scanner.nextLine());
                if (option < 1 || option > garbageCollectors.size())
                    throw new NumberFormatException();
            }
            catch (NumberFormatException e) {
                System.out.println("Invalid option!\n");
            }
        }

        GarbageCollectorType newGC = garbageCollectors.get((String)garbageCollectors.keySet().toArray()[option - 1]);
        controller.setGC(newGC);

        System.out.println("The garbage collector is now " + controller.getGC().toString() + ".");
    }
}
