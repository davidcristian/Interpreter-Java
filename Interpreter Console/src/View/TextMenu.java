package View;

import java.util.Scanner;
import java.util.HashMap;
import java.util.Map;

public class TextMenu {
    private static final Scanner scanner = new Scanner(System.in);
    private final Map<Integer, Command> commands;
    private int lastKey = 0;

    private boolean firstDisplay = true;

    public TextMenu() {
        this.commands = new HashMap<>();
    }

    public void addCommand(Command command) {
        command.setKey(this.lastKey);
        this.commands.put(this.lastKey, command);

        this.lastKey++;
    }

    public void show() {
        while (true) {
            this.printMenu();
            System.out.print("Option: ");
            String key = scanner.nextLine();
            Command command = null;

            try {
                int option = Integer.parseInt(key);
                command = this.commands.get(option);
            }
            catch (NumberFormatException ignored) { }

            if (command == null) {
                System.out.println("Invalid option!");
            }
            else {
                command.execute();
            }

            System.out.print("\nPress <ENTER> to continue");
            scanner.nextLine();
        }
    }

    private void printMenu() {
        if (this.firstDisplay) {
            this.firstDisplay = false;
        }
        else {
            for (int i = 0; i < 100; i++) {
                System.out.println();
            }
        }

        for (Map.Entry<Integer, Command> entry: this.commands.entrySet()) {
            System.out.println(entry.getKey() + ". " + entry.getValue().getDescription());
        }
    }
}
