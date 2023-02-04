package com.davidcristian.interpreter.GUI;

import com.davidcristian.interpreter.Controller.Controller;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import javafx.util.Pair;

import java.util.HashMap;
import java.util.Map;

public abstract class Window {
    protected static Controller CONTROLLER;
    protected static Map<String, Pair<Stage, Window>> WINDOWS = new HashMap<>();

    protected Stage thisStage;

    public void init(String name, Stage stage, Window window) {
        this.thisStage = stage;
        WINDOWS.put(name, new Pair<>(stage, window));
    }

    public abstract void load();

    public static void SetController(Controller controller) {
        Window.CONTROLLER = controller;
    }

    @FXML
    protected static void ShowAlert(Alert.AlertType type, String title, String header) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.showAndWait();
    }
}
