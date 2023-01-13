package ba.rpr.controllers;

import ba.rpr.business.MicronutrientManager;
import ba.rpr.business.PresenceManager;
import ba.rpr.business.SourceManager;
import ba.rpr.dao.exceptions.DaoException;
import ba.rpr.domain.Source;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.RowConstraints;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import ba.rpr.domain.Presence;
import ba.rpr.domain.Micronutrient;
import javafx.util.Callback;

import static javafx.scene.layout.Region.USE_COMPUTED_SIZE;

public class HomeController {

    public MenuItem editMenuItem;
    public MenuItem restoreMenuItem;
    public MenuItem aboutMenuItem;
    public TextField nameTextField;
    public ToggleGroup searchGroup;
    public TableView presenceTableView;
    public TableColumn nameTableColumn;
    public TableColumn amountTableColumn;
    public RowConstraints micronutrientInfoRow;
    public HBox micronutrientInfoHBox;
    public TextArea micronutrientInfoTextArea;

    private final PresenceManager presenceManager = new PresenceManager();
    private final MicronutrientManager micronutrientManager = new MicronutrientManager();
    private final SourceManager sourceManager = new SourceManager();

    @FXML
    public void initialize() {
        hideMicronutrientInfo();
        presenceTableView.setPlaceholder(new Label("Please enter a name to see presences"));
    }

    public void openEditWindow(ActionEvent actionEvent) throws IOException {
        Stage editWindow = new Stage();
        editWindow.setTitle("Edit");
        editWindow.setScene(FXMLLoader.load(getClass().getResource("/fxml/edit.fxml")));
        editWindow.setResizable(false);
        editWindow.initModality(Modality.APPLICATION_MODAL); //disables home stage
        editWindow.show();
    }

    public void restoreDefaultValues() {}
    public void showAbout() {}
    public void searchPresence() {
        try {
            hideMicronutrientInfo();
            String selectedRadioButtonText = ((RadioButton) searchGroup.getSelectedToggle()).getText();
            List<Presence> presenceList = new ArrayList<>(); //if micronutrient or source is null, empty list will be returned
            if (selectedRadioButtonText.equals("Source")) {
                nameTableColumn.setCellValueFactory(
                        (Callback<TableColumn.CellDataFeatures<Presence, String>, ObservableValue<String>>) presenceM ->
                                new ReadOnlyStringWrapper(presenceM.getValue().getMicronutrient().getName()));
                Source source = sourceManager.searchByName(nameTextField.getText());
                if(source != null) presenceList = presenceManager.micronutrientsInSource(source);
            } else if (selectedRadioButtonText.equals("Micronutrient")) {
                Micronutrient micronutrient = micronutrientManager.searchByName(nameTextField.getText());
                nameTableColumn.setCellValueFactory(
                        (Callback<TableColumn.CellDataFeatures<Presence, String>, ObservableValue<String>>) presenceS ->
                                new ReadOnlyStringWrapper(presenceS.getValue().getSource().getName()));
                if(micronutrient != null) {
                    showMicronutrientInfo(micronutrient);
                    presenceList = presenceManager.sourcesOfMicronutrient(micronutrient);
                }
            }
            amountTableColumn.setCellValueFactory(new PropertyValueFactory<Presence, Double>("amount"));
            if(!presenceList.isEmpty()) presenceTableView.getItems().setAll(presenceList);
            else presenceTableView.setPlaceholder(new Label("No presence found for given " + selectedRadioButtonText));
            presenceTableView.refresh();
        } catch(DaoException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Dao error");
            alert.setHeaderText("Error");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }

    public void hideMicronutrientInfo() {
        micronutrientInfoHBox.setVisible(false);
        micronutrientInfoRow.setPrefHeight(0);
    }

    public void showMicronutrientInfo(Micronutrient micronutrient) {
        micronutrientInfoHBox.setVisible(true);
        micronutrientInfoRow.setPrefHeight(USE_COMPUTED_SIZE);
        micronutrientInfoTextArea.setText(micronutrient.getRole());
    }

}



















