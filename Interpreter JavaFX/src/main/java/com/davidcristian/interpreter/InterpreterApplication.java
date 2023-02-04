package com.davidcristian.interpreter;

import com.davidcristian.interpreter.Controller.Controller;
import com.davidcristian.interpreter.GUI.Window;
import com.davidcristian.interpreter.Repository.IRepository;
import com.davidcristian.interpreter.Repository.Repository;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

public class InterpreterApplication extends Application {
    private static final Map<String, String> VIEWS = new LinkedHashMap<>() {{
            put("chooser-view", "Program Chooser");
            put("runner-view", "Program Runner");
    }};

    private static final String[] STYLESHEETS = {
            "style.css",
    };

    private static final int WIDTH = 800;
    private static final int HEIGHT = 600;

    @Override
    public void start(Stage stage) throws IOException {
        stage = null;

        // init controller
        IRepository repository = new Repository();
        Controller controller = new Controller(repository);
        Window.SetController(controller);

        for (String view : VIEWS.keySet()) {
            FXMLLoader loader = new FXMLLoader(this.getClass().getResource(view + ".fxml"));
            Scene scene = new Scene(loader.load(), WIDTH, HEIGHT);

            for (String stylesheet : STYLESHEETS) {
                scene.getStylesheets().add(Objects.requireNonNull(this.getClass().getResource(stylesheet)).toExternalForm());
            }

            Stage windowStage = new Stage();
            windowStage.setTitle(VIEWS.get(view));
            windowStage.setScene(scene);

            Window windowController = loader.getController();
            windowController.init(view, windowStage, windowController);

            if (stage == null) {
                stage = windowStage;
                windowController.load();
            }
        }

        // start
        assert stage != null;
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
