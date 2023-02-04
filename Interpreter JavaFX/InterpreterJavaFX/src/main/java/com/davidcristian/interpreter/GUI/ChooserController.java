package com.davidcristian.interpreter.GUI;

import com.davidcristian.interpreter.Controller.GarbageCollectorType;
import com.davidcristian.interpreter.Exceptions.ADTException;
import com.davidcristian.interpreter.Exceptions.InterpreterException;
import com.davidcristian.interpreter.Model.ADT.Dictionary.MyDictionary;
import com.davidcristian.interpreter.Model.ADT.Heap.MyHeap;
import com.davidcristian.interpreter.Model.ADT.List.MyList;
import com.davidcristian.interpreter.Model.ADT.Stack.MyStack;
import com.davidcristian.interpreter.Model.Program.IProgramHandler;
import com.davidcristian.interpreter.Model.Program.ProgramState;
import com.davidcristian.interpreter.Model.Statement.IStatement;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.util.Pair;

import java.io.IOException;
import java.util.*;

public class ChooserController extends Window implements IProgramHandler {
    @FXML
    public ComboBox<GarbageCollectorType> gcComboBox;

    @FXML
    private ListView<IStatement> programsListView;

    @FXML
    private Button executeButton;

    private static final Map<IStatement, List<ProgramState>> STATEMENTS_MAP = new LinkedHashMap<>();
    private static final Map<IStatement, String> BAD_PROGRAMS = new HashMap<>();

    @Override
    public void load() {
        // Initialize garbage collectors
        for (GarbageCollectorType gc : GarbageCollectorType.values()) {
            gcComboBox.getItems().add(gc);
        }

        gcComboBox.getSelectionModel().select(CONTROLLER.getGC());
    }

    @FXML
    public void initialize() {
        // Mark bad programs with the invalid css class
        programsListView.setCellFactory(arg -> new ListCell<>() {
            @Override
            protected void updateItem(IStatement item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null) {
                    setText(null);
                    setGraphic(null);
                    return;
                }

                // Add index to item
                setText((programsListView.getItems().indexOf(item) + 1) + ". " + item.toString());
                if (BAD_PROGRAMS.containsKey(item)) {
                    getStyleClass().add("invalid-program");
                } else {
                    getStyleClass().remove("invalid-program");
                }
            }
        });

        // Load the programs
        for (IStatement program : this.PROGRAMS) {
            ChooserController.STATEMENTS_MAP.put(program, new ArrayList<>());

            try {
                program.typeCheck(new MyDictionary<>());
            } catch (InterpreterException e) {
                BAD_PROGRAMS.put(program, e.getMessage());
            }
        }

        // Display the programs
        programsListView.setItems(FXCollections.observableArrayList(ChooserController.STATEMENTS_MAP.keySet()));
        if (programsListView.getItems().size() > 0) {
            programsListView.getSelectionModel().select(0);
            executeButton.setDisable(false);
        }

        Platform.runLater(() -> executeButton.requestFocus());
    }

    private IStatement getSelectedProgram() {
        return programsListView.getSelectionModel().getSelectedItem();
    }

    private int getSelectedProgramIndex() {
        return programsListView.getSelectionModel().getSelectedIndex();
    }

    private void resetProgram(IStatement statement, List<ProgramState> programStates) {
        programStates.clear();

        ProgramState program = new ProgramState(new MyStack<>(), new MyDictionary<>(), new MyList<>(), new MyDictionary<>(), new MyHeap<>(), statement);
        programStates.add(program);

        CONTROLLER.setProgramList(programStates);
    }

    private void checkProgram(IStatement statement, List<ProgramState> programStates) throws ADTException, IOException {
        // Program was already executed, reset it
        if (CONTROLLER.getUnfinishedPrograms().size() == 0) {
            CONTROLLER.getRepository().emptyLogFile();
            resetProgram(statement, programStates);
            return;
        }

        // program is original
        if (programStates.get(0).isOriginal()) {
            CONTROLLER.getRepository().emptyLogFile();
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Question");
        alert.setHeaderText("Resume program execution?");
        ButtonType yesButton = new ButtonType("Yes", ButtonBar.ButtonData.YES);
        ButtonType noButton = new ButtonType("No", ButtonBar.ButtonData.NO);
        alert.getButtonTypes().setAll(yesButton, noButton);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == noButton) {
            CONTROLLER.getRepository().emptyLogFile();
            resetProgram(statement, programStates);
        }
    }

    private void execute() {
        IStatement statement = this.getSelectedProgram();
        if (statement == null) {
            ShowAlert(Alert.AlertType.ERROR, "Error", "Please select a program to execute");
            return;
        }

        if (BAD_PROGRAMS.containsKey(statement)) {
            ShowAlert(Alert.AlertType.WARNING, "Warning", BAD_PROGRAMS.get(statement));
            return;
        }

        List<ProgramState> programStates = ChooserController.STATEMENTS_MAP.get(statement);

        try {
            CONTROLLER.setProgramList(programStates);

            // Overwrite log file name
            int index = this.getSelectedProgramIndex() + 1;
            CONTROLLER.getRepository().setLogName("log" + index + ".txt");
            checkProgram(statement, programStates);

            thisStage.hide();
            Pair<Stage, Window> runner = WINDOWS.get("runner-view");

            runner.getValue().load();
            runner.getKey().show();
        } catch (ADTException | IOException e) {
            ShowAlert(Alert.AlertType.ERROR, "Error", e.getMessage());
        }
    }

    @FXML
    private void executeButtonPressed(ActionEvent event) {
        execute();
    }

    @FXML
    private void listViewClicked(MouseEvent event) {
        if (event.getButton().equals(MouseButton.PRIMARY) && event.getClickCount() == 2) {
            execute();
        }
    }

    @FXML
    private void listViewKeyReleased(KeyEvent event) {
        if (event.getCode().equals(KeyCode.ENTER)) {
            execute();
        }
    }

    @FXML
    private void gcComboBoxChanged(ActionEvent event) {
        GarbageCollectorType gc = gcComboBox.getSelectionModel().getSelectedItem();
        if (gc == null) {
            return;
        }

        CONTROLLER.setGC(gc);
    }
}
