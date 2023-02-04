package com.davidcristian.interpreter.GUI;

import com.davidcristian.interpreter.Exceptions.InterpreterException;
import com.davidcristian.interpreter.Model.ADT.Dictionary.IDictionary;
import com.davidcristian.interpreter.Model.ADT.Heap.IHeap;
import com.davidcristian.interpreter.Model.Program.ProgramState;
import com.davidcristian.interpreter.Model.Statement.IStatement;
import com.davidcristian.interpreter.Model.Value.IValue;

import javafx.application.Platform;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.util.Pair;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class RunnerController extends Window {
    @FXML
    private TextField programCountField;

    @FXML
    private ListView<Integer> programsListView;
    private ChangeListener<Integer> programsListViewListener;

    @FXML
    private ListView<String> outputListView;

    @FXML
    private ListView<String> executionStackListView;

    @FXML
    private ListView<String> fileTableListView;

    @FXML
    private TableView<Pair<String, IValue>> symbolTableView;

    @FXML
    private TableColumn<Pair<String, IValue>, String> variableNameColumn;

    @FXML
    private TableColumn<Pair<String, IValue>, String> variableValueColumn;

    @FXML
    private TableView<Pair<Integer, IValue>> heapTableView;

    @FXML
    private TableColumn<Pair<Integer, IValue>, Integer> addressColumn;

    @FXML
    private TableColumn<Pair<Integer, IValue>, String> valueColumn;

    @FXML
    public Button backButton;

    @FXML
    private Button oneStepButton;

    @FXML
    public Button allStepsButton;

    @Override
    public void load() {
        populate();
    }

    @FXML
    public void initialize() {
        // Symbol table column sizing
        variableNameColumn.setMaxWidth(1f * Integer.MAX_VALUE * 50);
        variableValueColumn.setMaxWidth(1f * Integer.MAX_VALUE * 50);

        // Heap table column sizing
        addressColumn.setMaxWidth(1f * Integer.MAX_VALUE * 50);
        valueColumn.setMaxWidth(1f * Integer.MAX_VALUE * 50);

        // Set selection models
//        programsListView.setSelectionModel(new NoSelectionModel<>());
        executionStackListView.setSelectionModel(new NoSelectionModel<>());
        symbolTableView.setSelectionModel(new NoTableViewSelectionModel<>(symbolTableView));
        outputListView.setSelectionModel(new NoSelectionModel<>());
        fileTableListView.setSelectionModel(new NoSelectionModel<>());
        heapTableView.setSelectionModel(new NoTableViewSelectionModel<>(heapTableView));

        // Symbol table
        variableNameColumn.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().getKey()));
        variableValueColumn.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().getValue().toString()));

        // Heap table
        addressColumn.setCellValueFactory(p -> new SimpleIntegerProperty(p.getValue().getKey()).asObject());
        valueColumn.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().getValue().toString()));

        programsListViewListener = (observable, oldValue, newValue) -> {
            ProgramState programState = this.getSelectedProgram();
            if (programState == null)
                return;

            populateExecutionStackListView(programState);
            populateSymbolTableView(programState);
        };

        programsListView.getSelectionModel().selectedItemProperty().addListener(programsListViewListener);
        Platform.runLater(() -> oneStepButton.requestFocus());
    }

    private ProgramState getSelectedProgram() {
        if (CONTROLLER.getProgramList().size() == 0)
            return null;

        int index = programsListView.getSelectionModel().getSelectedIndex();
        return CONTROLLER.getProgramList().get(Math.max(index, 0));
    }

    private void populate() {
        populateProgramsListView();
        ProgramState programState = this.getSelectedProgram();
        if (programState == null)
            return;

        populateExecutionStackListView(programState);
        populateSymbolTableView(programState);
        populateOutputListView(programState);
        populateFileTableListView(programState);
        populateHeapTableView(programState);
    }

    private void clearProgramsListView() {
        programsListView.getSelectionModel().selectedItemProperty().removeListener(programsListViewListener);

        programsListView.getItems().clear();
        updateProgramCount(0);

        programsListView.getSelectionModel().selectedItemProperty().addListener(programsListViewListener);
    }

    private void populateProgramsListView() {
        programsListView.getSelectionModel().selectedItemProperty().removeListener(programsListViewListener);

        List<ProgramState> programStates = CONTROLLER.getProgramList();
        List<Integer> ids = programStates.stream().map(ProgramState::getID).collect(Collectors.toList());

        programsListView.setItems(FXCollections.observableList(ids));
        updateProgramCount(ids.size());

        oneStepButton.setDisable(programStates.size() == 0);
        allStepsButton.setDisable(programStates.size() == 0);
        if (ids.size() > 0 && programsListView.getSelectionModel().getSelectedIndex() < 0) {
            programsListView.getSelectionModel().select(0);
            oneStepButton.requestFocus();
        }

        programsListView.getSelectionModel().selectedItemProperty().addListener(programsListViewListener);
    }

    private void updateProgramCount(int count) {
        programCountField.setText(String.valueOf(count));
    }

    private void populateExecutionStackListView(ProgramState programState) {
        List<String> executionStack = new ArrayList<>();

        for (IStatement statement : programState.getExecutionStack().getElements()) {
            executionStack.add(statement.toString());
        }

        executionStackListView.setItems(FXCollections.observableList(executionStack));
    }

    private void populateSymbolTableView(ProgramState programState) {
        ArrayList<Pair<String, IValue>> symbolTableEntries = new ArrayList<>();
        IDictionary<String, IValue> symbolTable = programState.getSymbolTable();

        for (Map.Entry<String, IValue> entry : symbolTable.getDictionary().entrySet()) {
            symbolTableEntries.add(new Pair<>(entry.getKey(), entry.getValue()));
        }

        symbolTableView.setItems(FXCollections.observableArrayList(symbolTableEntries));
    }

    private void populateOutputListView(ProgramState programState) {
        List<String> output = new ArrayList<>();
        List<IValue> outputList = programState.getOutput().getList();

        for (IValue value : outputList) {
            output.add(value.toString());
        }

        outputListView.setItems(FXCollections.observableArrayList(output));
    }

    private void populateFileTableListView(ProgramState programState) {
        List<String> fileTable = new ArrayList<>(programState.getFileTable().getDictionary().keySet());
        fileTableListView.setItems(FXCollections.observableList(fileTable));
    }

    private void populateHeapTableView(ProgramState programState) {
        ArrayList<Pair<Integer, IValue>> heapTableEntries = new ArrayList<>();
        IHeap<IValue> heapTable = programState.getHeap();

        for(Map.Entry<Integer, IValue> entry : heapTable.getHeap().entrySet()) {
            heapTableEntries.add(new Pair<>(entry.getKey(), entry.getValue()));
        }

        heapTableView.setItems(FXCollections.observableArrayList(heapTableEntries));
    }

    @FXML
    private void runOneStep(ActionEvent event) {
        List<ProgramState> programList = CONTROLLER.getUnfinishedPrograms();
        if (programList.size() == 0) {
            ShowAlert(Alert.AlertType.ERROR, "Error", "No more steps to execute!");
            return;
        }

        try {
            CONTROLLER.createExecutor();
            CONTROLLER.oneStep(programList);

            populate();
            programList = CONTROLLER.getUnfinishedPrograms();

            CONTROLLER.killExecutor();
            CONTROLLER.setProgramList(programList);

            populateProgramsListView();
            if (programList.size() == 0) {
                ShowAlert(Alert.AlertType.INFORMATION, "Information", "Program finished successfully!");
            }
        } catch (InterpreterException | InterruptedException | IOException e) {
            CONTROLLER.killExecutor();
            ShowAlert(Alert.AlertType.ERROR, "Error", e.getMessage());
        }
    }

    private void postAllSteps(ProgramState programState) {
        oneStepButton.setDisable(true);
        allStepsButton.setDisable(true);
        this.clearProgramsListView();

        populateExecutionStackListView(programState);
        populateSymbolTableView(programState);
        populateOutputListView(programState);
        populateFileTableListView(programState);
        populateHeapTableView(programState);
    }

    @FXML
    private void runAllSteps(ActionEvent event) {
        List<ProgramState> programList = CONTROLLER.getUnfinishedPrograms();
        if (programList.size() == 0) {
            ShowAlert(Alert.AlertType.ERROR, "Error", "No more steps to execute!");
            return;
        }

        try {
            CONTROLLER.allSteps();
            this.postAllSteps(programList.get(0));

            ShowAlert(Alert.AlertType.INFORMATION, "Information", "Program finished successfully!");
        } catch (InterpreterException | InterruptedException | IOException e) {
            CONTROLLER.killExecutor();
            this.postAllSteps(programList.get(0));

            ShowAlert(Alert.AlertType.ERROR, "Error", e.getMessage());
        }
    }

    @FXML
    private void goBack(ActionEvent event) {
        thisStage.hide();
        WINDOWS.get("chooser-view").getKey().show();
    }
}
