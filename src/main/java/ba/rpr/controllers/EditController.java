package ba.rpr.controllers;

import ba.rpr.business.MicronutrientManager;
import ba.rpr.business.PresenceManager;
import ba.rpr.business.SourceManager;
import ba.rpr.dao.DaoFactory;
import ba.rpr.dao.exceptions.DaoException;
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

import java.io.IOException;
import java.util.Optional;

public class EditController {
    public ComboBox editComboBox;
    public TableView editTableView;
    public Button addButton;
    public Button editButton;
    public Button deleteButton;

    private final SourceManager sourceManager = new SourceManager();
    private final MicronutrientManager micronutrientManager = new MicronutrientManager();
    private final PresenceManager presenceManager = new PresenceManager();

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
            if (choice.equals("Source")) {
                sourceManager.delete(((Source) editTableView.getSelectionModel().getSelectedItem()).getId());
            } else if(choice.equals("Micronutrient")) {
                micronutrientManager.delete(((Micronutrient) editTableView.getSelectionModel().getSelectedItem()).getId());
            } else if(choice.equals("Presence")) {
                presenceManager.delete(((Presence) editTableView.getSelectionModel().getSelectedItem()).getId());
            }
            setupTableColumns(choice);
        } catch(DaoException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).showAndWait();
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
                TableColumn<Micronutrient, Text> role = new TableColumn<>("Role"); //
                id.setCellValueFactory(new PropertyValueFactory<Micronutrient, Integer>("id"));
                name.setCellValueFactory(new PropertyValueFactory<Micronutrient, String>("name"));
                type.setCellValueFactory(micronutrient -> {
                    if(micronutrient.getValue().isVitamin()) return new ReadOnlyStringWrapper("Vitamin");
                    return new ReadOnlyStringWrapper("Mineral");
                });
                role.setCellValueFactory(new PropertyValueFactory<>("role"));
                editTableView.getColumns().setAll(id, name, type, role);
                editTableView.getItems().setAll(DaoFactory.micronutrientDao().getAll());
                role.prefWidthProperty().bind( //so that the last column get all the leftover space
                        editTableView.widthProperty()
                                .subtract(id.widthProperty())
                                .subtract(name.widthProperty())
                                .subtract(type.widthProperty())
                );
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
            new Alert(Alert.AlertType.ERROR, e.getMessage()).showAndWait();
        }
    }

    public void openEditWindow(String choice, Integer id) {
        Stage editWindow = new Stage();
        FXMLLoader loader = null;
        try {
            if (choice.equals("Source")) {
                loader = new FXMLLoader(getClass().getResource("/fxml/sourceEdit.fxml"));
                loader.setController(new SourceEditController(id));
            } else if (choice.equals("Micronutrient")) {
                loader = new FXMLLoader(getClass().getResource("/fxml/micronutrientEdit.fxml"));
                loader.setController(new MicronutrientEditController(id));
            } else if (choice.equals("Presence")) {
                loader = new FXMLLoader(getClass().getResource("/fxml/presenceEdit.fxml"));
                loader.setController(new PresenceEditController(id));
            }
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
        } catch(IOException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).showAndWait();
        }
    }
}
