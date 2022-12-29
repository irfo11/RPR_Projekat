package ba.rpr.controllers;

import ba.rpr.dao.DaoFactory;
import ba.rpr.dao.exceptions.DaoException;
import ba.rpr.domain.Micronutrient;
import ba.rpr.domain.Presence;
import ba.rpr.domain.Source;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;

public class EditController {
    public ComboBox editComboBox;
    public TableView editTableView;
    public Button addButton;
    public Button editButton;
    public Button deleteButton;

    @FXML
    public void initialize() {
        editComboBox.getItems().setAll("Micronutrient", "Source", "Presence");
        addButton.setDisable(true);
        editButton.setDisable(true);
        deleteButton.setDisable(true);
    }

    public void add(ActionEvent actionEvent) {
        try {
            Stage addWindow = openEditWindow(editComboBox.getSelectionModel().getSelectedItem().toString());
        } catch(IOException e) {
            handleException(e.getMessage());
        }
    }

    public void delete(ActionEvent actionEvent) {

    }

    public void edit(ActionEvent actionEvent) {

    }

    public void setupEdit(ActionEvent actionEvent) {
        addButton.setDisable(false);
        editButton.setDisable(false);
        deleteButton.setDisable(false);
        setupTableColumns(editComboBox.getSelectionModel().getSelectedItem().toString());
    }

    public void setupTableColumns(String choice) {
        try {
            if (choice.equals("Source")) {
                TableColumn<Source, Integer> id = new TableColumn<>("Id");
                TableColumn<Source, String> name = new TableColumn<>("Name");
                id.setCellValueFactory(new PropertyValueFactory<Source, Integer>("id"));
                name.setCellValueFactory(new PropertyValueFactory<Source, String>("name"));
                editTableView.getColumns().setAll(id, name);
                editTableView.getItems().setAll(DaoFactory.sourceDao().getAll());
            } else if(choice.equals("Micronutrient")) {
                TableColumn<Micronutrient, Integer> id = new TableColumn<>("Id");
                TableColumn<Micronutrient, String> name = new TableColumn<>("Name");
                TableColumn<Micronutrient, String> type = new TableColumn<>("Type");
                TableColumn<Micronutrient, String> role = new TableColumn<>("Role");
                id.setCellValueFactory(new PropertyValueFactory<Micronutrient, Integer>("id"));
                name.setCellValueFactory(new PropertyValueFactory<Micronutrient, String>("name"));
                type.setCellValueFactory(micronutrient -> {
                    if(micronutrient.getValue().isVitamin()) return new ReadOnlyStringWrapper("Vitamin");
                    return new ReadOnlyStringWrapper("Mineral");
                });
                role.setCellValueFactory(new PropertyValueFactory<>("role"));
                editTableView.getColumns().setAll(id, name, type, role);
                editTableView.getItems().setAll(DaoFactory.micronutrientDao().getAll());
            } else if(choice.equals("Presence")) {
                TableColumn<Presence, Integer> id = new TableColumn<>("Id");
                TableColumn<Presence, String> micronutrient = new TableColumn<>("Micronutrient");
                TableColumn<Presence, String> source = new TableColumn<>("Source");
                TableColumn<Presence, Double> amount = new TableColumn<>("Amount");
                id.setCellValueFactory(new PropertyValueFactory<>("id"));
                micronutrient.setCellValueFactory(presence -> {
                    return new ReadOnlyStringWrapper(presence.getValue().getMicronutrient().getName());
                });
                source.setCellValueFactory(presence -> {
                    return new ReadOnlyStringWrapper(presence.getValue().getSource().getName());
                });
                amount.setCellValueFactory(new PropertyValueFactory<>("amount"));
                editTableView.getColumns().setAll(id, micronutrient, source, amount);
                editTableView.getItems().setAll(DaoFactory.presenceDao().getAll());
            }
            editTableView.refresh();
        } catch(DaoException e) {
            handleException(e.getMessage());
        }
    }

    public Stage openEditWindow(String choice) throws IOException {
        Stage editWindow = new Stage();
        if(choice.equals("Source")) {
            editWindow.setScene(FXMLLoader.load(getClass().getResource("/fxml/sourceEdit.fxml")));
        } else if(choice.equals("Micronutrient")) {
            editWindow.setScene(FXMLLoader.load(getClass().getResource("/fxml/micronutrientEdit.fxml")));
        } else if(choice.equals("Presence")) {
            editWindow.setScene(FXMLLoader.load(getClass().getResource("/fxml/presenceEdit.fxml")));
        }
        editWindow.setResizable(false);
        editWindow.show();
        return editWindow;
    }

    public void handleException(String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Dao Exception");
        alert.setHeaderText("Error");
        alert.setContentText(msg);

        alert.showAndWait();
    }
}
