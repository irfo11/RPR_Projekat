package ba.rpr.controllers;

import ba.rpr.business.MicronutrientManager;
import ba.rpr.dao.exceptions.DaoException;
import ba.rpr.domain.Micronutrient;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class MicronutrientEditController {
    private final Integer id;
    private MicronutrientModel micronutrientModel = new MicronutrientModel();

    private final MicronutrientManager micronutrientManager = new MicronutrientManager();

    public Scene scene;
    public ToggleGroup micronutrientType;
    public TextField nameTextField;
    public TextArea roleTextArea;
    public Button actionButton;

    public MicronutrientEditController(Integer id) {
        this.id = id;
    }

    @FXML
    public void initialize() {
        try {
            nameTextField.textProperty().bindBidirectional(micronutrientModel.name);
            roleTextArea.textProperty().bindBidirectional(micronutrientModel.role);
            ((RadioButton) micronutrientType.getToggles().get(0)).setOnAction(actionEvent -> {
                micronutrientModel.isVitamin.set(true);
            });
            ((RadioButton) micronutrientType.getToggles().get(1)).setOnAction(actionEvent -> {
                micronutrientModel.isVitamin.set(false);
            });
            if (id != null) {
                micronutrientModel.fromMicronutrient(micronutrientManager.getById(id));
                if (micronutrientModel.isVitamin.get()) {
                    micronutrientType.getToggles().get(0).setSelected(true);
                } else {
                    micronutrientType.getToggles().get(1).setSelected(true);
                }
                actionButton.setText("Update");
            } else actionButton.setText("Add");
        } catch(DaoException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).showAndWait();
        }
    }

    public void save(ActionEvent actionEvent) {
        try {
            if(id != null) micronutrientManager.update(id, micronutrientModel.toMicronutrient());
            else micronutrientManager.add(micronutrientModel.toMicronutrient());
            scene.getWindow().hide();
        } catch (DaoException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).showAndWait();
        }
    }

    public class MicronutrientModel {
        public SimpleStringProperty name = new SimpleStringProperty("");
        public SimpleStringProperty role = new SimpleStringProperty("");
        public SimpleBooleanProperty isVitamin = new SimpleBooleanProperty(true);

        public void fromMicronutrient(Micronutrient micronutrient) {
            name.set(micronutrient.getName());
            role.set(micronutrient.getRole());
            isVitamin.set(micronutrient.isVitamin());
        }

        public Micronutrient toMicronutrient() {
            return new Micronutrient(-1, name.get(), role.get(), isVitamin.get());
        }
    }
}
