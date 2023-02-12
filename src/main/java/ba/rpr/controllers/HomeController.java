package ba.rpr.controllers;

import ba.rpr.business.MicronutrientManager;
import ba.rpr.business.PresenceManager;
import ba.rpr.business.SourceManager;
import ba.rpr.dao.exceptions.DaoException;
import ba.rpr.domain.Micronutrient;
import ba.rpr.domain.Presence;
import ba.rpr.domain.Source;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.RowConstraints;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
        openWindow("Edit", "/fxml/edit.fxml");
    }

    public void showAbout() throws IOException{
        openWindow("About", "/fxml/about.fxml");
    }

    private void openWindow(String title, String fxmlPath) throws IOException {
        Stage stage = new Stage();
        stage.setScene(FXMLLoader.load(getClass().getResource(fxmlPath)));
        stage.setTitle(title);
        stage.getIcons().add(new Image("/fxml/grape.jpeg"));
        stage.setResizable(false);
        stage.initStyle(StageStyle.UTILITY);
        stage.initModality(Modality.APPLICATION_MODAL); //disables home stage
        stage.show();
    }

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
            presenceTableView.getItems().setAll(presenceList);
            if(presenceList.isEmpty()) {
                presenceTableView.setPlaceholder(new Label("No presence found for given " + selectedRadioButtonText));
            }
            presenceTableView.refresh();
        } catch(DaoException e) {
            handleException(e.getMessage());
        }
    }

    public static void handleException(String msg) {
        new Alert(Alert.AlertType.ERROR, msg).showAndWait();
    }

    private void hideMicronutrientInfo() {
        micronutrientInfoHBox.setVisible(false);
        micronutrientInfoRow.setPrefHeight(0);
    }

    private void showMicronutrientInfo(Micronutrient micronutrient) {
        micronutrientInfoHBox.setVisible(true);
        micronutrientInfoRow.setPrefHeight(USE_COMPUTED_SIZE);
        micronutrientInfoTextArea.setText(micronutrient.getRole());
    }

}



















