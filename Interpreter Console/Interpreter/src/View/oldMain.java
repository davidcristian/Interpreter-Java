package View;

import Controller.Controller;
import Repository.*;

public class oldMain {
    public static void main(String[] args) {
        IRepository repository = new Repository();
        Controller controller = new Controller(repository);

        oldView view = new oldView(controller);
        view.start();
    }
}
