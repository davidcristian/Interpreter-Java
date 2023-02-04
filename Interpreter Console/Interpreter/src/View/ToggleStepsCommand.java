package View;

import Controller.Controller;

public class ToggleStepsCommand extends Command {
    private final Controller controller;

    public ToggleStepsCommand(String description, Controller controller) {
        super(description);
        this.controller = controller;
    }

    @Override
    public void execute() {
        this.controller.setShowSteps(!this.controller.getShowSteps());
        System.out.println("Debugging is now " + (controller.getShowSteps() ? "enabled" : "disabled") + ".");
    }
}
