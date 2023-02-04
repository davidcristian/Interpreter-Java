package View;

import java.util.Scanner;

public abstract class Command {
    protected static final Scanner scanner = new Scanner(System.in);

    private Integer key;
    private final String description;

    public Command(String description) {
        this.description = description;
    }

    public void setKey(Integer key) {
        this.key = key;
    }

    public Integer getKey() {
        return key;
    }

    public String getDescription() {
        return description;
    }

    public abstract void execute();
}
