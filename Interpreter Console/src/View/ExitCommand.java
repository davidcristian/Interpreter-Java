package View;

public class ExitCommand extends Command {
    public ExitCommand(String description) {
        super(description);
    }

    @Override
    public void execute() {
        System.exit(0);
    }
}
