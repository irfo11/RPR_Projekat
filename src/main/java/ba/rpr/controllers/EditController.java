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
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;

public class EditController {
    public ComboBox editComboBox;
    public TableView editTableView;
    public Button addButton;
    public Button editButton;
    public Button deleteButton;

    public Source source = null;
    public Micronutrient micronutrient = null;
    public Presence presence = null;
    @FXML
    public void initialize() {
        editComboBox.getItems().setAll("Micronutrient", "Source", "Presence");
        addButton.setDisable(true);
        editButton.setDisable(true);
        deleteButton.setDisable(true);
    }

    public void add(ActionEvent actionEvent) {
        try {
            String choice = editComboBox.getSelectionModel().getSelectedItem().toString();
            if(choice.equals("Source")) {
                source = new Source(-1, ""); //empty source to add
                openEditWindow(choice, false);
                DaoFactory.sourceDao().add(source);
            } else if(choice.equals("Micronutrient")) {
                micronutrient = new Micronutrient(-1, "", "", false);
                openEditWindow(choice, false);
                DaoFactory.micronutrientDao().add(micronutrient);
            }
            setupTableColumns(choice);
        } catch(IOException | DaoException e) {
            handleException(e.getMessage());
        }
    }

    public void delete(ActionEvent actionEvent) {
        String choice = editComboBox.getSelectionModel().getSelectedItem().toString();
        try {
            if (choice.equals("Source")) {
                source = (Source) editTableView.getSelectionModel().getSelectedItem();
                DaoFactory.sourceDao().delete(source.getId());
            } else if(choice.equals("Micronutrient")) {
                micronutrient = (Micronutrient) editTableView.getSelectionModel().getSelectedItem();
                DaoFactory.micronutrientDao().delete(micronutrient.getId());
            }
        } catch(DaoException e) {
            handleException(e.getMessage());
        }
        setupTableColumns(choice);
    }

    public void edit(ActionEvent actionEvent) {
        try{
            String choice = editComboBox.getSelectionModel().getSelectedItem().toString();
            if(choice.equals("Source")) {
                source = (Source) editTableView.getSelectionModel().getSelectedItem();
            } else if(choice.equals("Micronutrient")) {
                micronutrient = (Micronutrient) editTableView.getSelectionModel().getSelectedItem();
            }
            openEditWindow(choice, true);
            if(choice.equals("Source")) {
                DaoFactory.sourceDao().update(source.getId(), source);
            } else if(choice.equals("Micronutrient")) {
                DaoFactory.micronutrientDao().update(micronutrient.getId(), micronutrient);
            }
            setupTableColumns(choice);
        } catch(IOException | DaoException e) {
            handleException(e.getMessage());
        }
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
            handleException(e.getMessage());
        }
    }

    public void openEditWindow(String choice, boolean edit) throws IOException {
        Stage editWindow = new Stage();
        FXMLLoader loader = null;
        if(choice.equals("Source")) {
            loader = new FXMLLoader(getClass().getResource("/fxml/sourceEdit.fxml"));
            loader.setController(new SourceEditController(source, edit));
        } else if(choice.equals("Micronutrient")) {
            loader = new FXMLLoader(getClass().getResource("/fxml/micronutrientEdit.fxml"));
            loader.setController(new MicronutrientEditController(micronutrient, edit));
        } else if(choice.equals("Presence")) {
            editWindow.setScene(FXMLLoader.load(getClass().getResource("/fxml/presenceEdit.fxml")));
        }
        editWindow.setScene(loader.load());
        editWindow.setResizable(false);
        editWindow.showAndWait();
    }

    public void handleException(String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Dao Exception");
        alert.setHeaderText("Error");
        alert.setContentText(msg);

        alert.showAndWait();
    }
}
