package ba.rpr.controllers;

import ba.rpr.business.Manager;
import ba.rpr.business.MicronutrientManager;
import ba.rpr.business.PresenceManager;
import ba.rpr.business.SourceManager;
import ba.rpr.domain.Idable;
import ba.rpr.domain.Micronutrient;
import ba.rpr.domain.Presence;
import ba.rpr.domain.Source;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;

public class EditController {
    public ComboBox editComboBox;
    public TableView editTableView;
    public Button addButton;
    public Button editButton;
    public Button deleteButton;

    private final Map<String, Manager> managers = new TreeMap<>();

    public EditController() {
        managers.put("Source", new SourceManager());
        managers.put("Micronutrient", new MicronutrientManager());
        managers.put("Presence", new PresenceManager());
    }

    @FXML
    public void initialize() {
        editComboBox.getItems().setAll("Micronutrient", "Source", "Presence");
        addButton.setDisable(true);
        editButton.setDisable(true);
        deleteButton.setDisable(true);
    }

    public void add(ActionEvent actionEvent) {
        String choice = editComboBox.getSelectionModel().getSelectedItem().toString();
        openEditWindow(choice, null);
        setupTableColumns(choice);
    }

    public void delete(ActionEvent actionEvent) {
        String choice = editComboBox.getSelectionModel().getSelectedItem().toString();
        try {
            managers.get(choice).delete(((Idable) editTableView.getSelectionModel().getSelectedItem()).getId());
            setupTableColumns(choice);
        } catch(Exception e) {
            HomeController.handleException(e.getMessage());
        }
    }

    public void edit(ActionEvent actionEvent) {
        String choice = editComboBox.getSelectionModel().getSelectedItem().toString();
        openEditWindow(choice, ((Idable) editTableView.getSelectionModel().getSelectedItem()).getId());
        setupTableColumns(choice);
    }

    public void setupEdit(ActionEvent actionEvent) {
        addButton.setDisable(false);
        editButton.setDisable(false);
        deleteButton.setDisable(false);
        setupTableColumns(editComboBox.getSelectionModel().getSelectedItem().toString());
    }

    private void setupSourceTableColumns() {
        TableColumn<Source, Integer> id = new TableColumn<>("Id");
        TableColumn<Source, String> name = new TableColumn<>("Name");
        id.setCellValueFactory(new PropertyValueFactory<Source, Integer>("id"));
        name.setCellValueFactory(new PropertyValueFactory<Source, String>("name"));
        editTableView.getColumns().setAll(id, name);
    }

    private void setupMicronutrientTableColumns() {
        TableColumn<Micronutrient, Integer> id = new TableColumn<>("Id");
        TableColumn<Micronutrient, String> name = new TableColumn<>("Name");
        TableColumn<Micronutrient, String> type = new TableColumn<>("Type");
        TableColumn<Micronutrient, Text> role = new TableColumn<>("Role"); //
        id.setCellValueFactory(new PropertyValueFactory<Micronutrient, Integer>("id"));
        name.setCellValueFactory(new PropertyValueFactory<Micronutrient, String>("name"));
        type.setCellValueFactory(micronutrient -> {
            if(micronutrient.getValue().isVitamin()) return new ReadOnlyStringWrapper("Vitamin");
            return new ReadOnlyStringWrapper("Mineral");
        });
        role.setCellValueFactory(new PropertyValueFactory<>("role"));
        editTableView.getColumns().setAll(id, name, type, role);
        role.prefWidthProperty().bind( //so that the last column get all the leftover space
                editTableView.widthProperty()
                        .subtract(id.widthProperty())
                        .subtract(name.widthProperty())
                        .subtract(type.widthProperty())
        );
    }

    private void setupPresenceTableColumns() {
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
    }

    public void setupTableColumns(String choice) {
        try {
            getClass().getDeclaredMethod("setup"+choice+"TableColumns").invoke(this);
            editTableView.getItems().setAll(managers.get(choice).getAll());
            editTableView.refresh();
        } catch(Exception e) {
            HomeController.handleException(e.getMessage());
        }
    }

    public void openEditWindow(String choice, Integer id) {
        Stage editWindow = new Stage();
        try {
            //loads fxml based on choice
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/"+choice.toLowerCase()+"Edit.fxml"));
            //finds controller based on choice, if new controllers get created they should follow same naming template
            loader.setController(
                Class.forName("ba.rpr.controllers."+choice+"EditController").
                        getConstructor(Integer.class).newInstance(id));
            editWindow.setOnCloseRequest(actionEvent -> { //won't get triggered by hide method, get triggered by x button
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION,
                        "Are you sure you want to close this window, updates won't apply",
                        ButtonType.YES, ButtonType.NO);
                Optional<ButtonType> selection = alert.showAndWait();
                if (selection.isPresent() && selection.get() == ButtonType.NO)
                    actionEvent.consume(); //to continue editing
                else editWindow.close();
            });
            editWindow.setScene(loader.load());
            editWindow.setResizable(false);
            editWindow.initModality(Modality.APPLICATION_MODAL);
            editWindow.showAndWait();
        } catch(Exception e) {
            HomeController.handleException(e.getMessage());
        }
    }
}
