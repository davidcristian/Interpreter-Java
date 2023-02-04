package com.davidcristian.interpreter.GUI;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableView;

public class NoTableViewSelectionModel<S> extends TableView.TableViewSelectionModel<S> {
    public NoTableViewSelectionModel(TableView<S> tableView) {
        super(tableView);
    }

    @Override
    public ObservableList<Integer> getSelectedIndices() {
        return FXCollections.emptyObservableList();
    }

    @Override
    public ObservableList<S> getSelectedItems() {
        return FXCollections.emptyObservableList();
    }

    @Override
    public ObservableList<TablePosition> getSelectedCells() {
        return FXCollections.emptyObservableList();
    }

    @Override
    public boolean isSelected(int index) {
        return false;
    }

    @Override
    public boolean isSelected(int row, TableColumn<S, ?> column) {
        return false;
    }

    @Override
    public boolean isEmpty() {
        return true;
    }

    @Override
    public void selectLeftCell() {

    }

    @Override
    public void selectRightCell() {

    }

    @Override
    public void selectAboveCell() {

    }

    @Override
    public void selectBelowCell() {

    }

    @Override
    public void selectNext() {

    }

    @Override
    public void selectPrevious() {

    }

    @Override
    public void selectFirst() {

    }

    @Override
    public void selectLast() {

    }

    @Override
    public void selectAll() {

    }

    @Override
    public void selectIndices(int row, int... rows) {

    }

    @Override
    public void select(int row, TableColumn<S, ?> column) {

    }

    @Override
    public void select(int row) {

    }

    @Override
    public void select(Object obj) {

    }

    @Override
    public void clearAndSelect(int row) {

    }

    @Override
    public void clearAndSelect(int row, TableColumn<S, ?> column) {

    }

    @Override
    public void clearSelection(int index) {

    }

    @Override
    public void clearSelection(int row, TableColumn<S, ?> column) {

    }

    @Override
    public void clearSelection() {

    }
}
