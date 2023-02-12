package ba.rpr.controllers;

import ba.rpr.business.MicronutrientManager;
import ba.rpr.business.PresenceManager;
import ba.rpr.business.SourceManager;
import ba.rpr.dao.exceptions.DaoException;
import ba.rpr.domain.Micronutrient;
import ba.rpr.domain.Presence;
import ba.rpr.domain.Source;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.util.converter.NumberStringConverter;

public class PresenceEditController {
    private final Integer id;

    private PresenceModel presenceModel = new PresenceModel();

    private final PresenceManager presenceManager = new PresenceManager();
    private final MicronutrientManager micronutrientManager = new MicronutrientManager();
    private final SourceManager sourceManager = new SourceManager();
    public Scene scene;
    public TextField micronutrientTextField;
    public TextField sourceTextField;
    public TextField amountTextField;
    public Button actionButton;

    public PresenceEditController(Integer id) {
        this.id = id;
    }

    @FXML
    public void initialize() {
        try {
            micronutrientTextField.textProperty().bindBidirectional(presenceModel.micronutrientName);
            sourceTextField.textProperty().bindBidirectional(presenceModel.sourceName);
            amountTextField.textProperty().bindBidirectional(presenceModel.amount, new NumberStringConverter());
            if (id != null) {
                presenceModel.fromPresence(presenceManager.getById(id));
                actionButton.setText("Update");
            } else {
                actionButton.setText("Add");
            }
        } catch(DaoException e) {
            HomeController.handleException(e.getMessage());
        }
    }

    public void save(ActionEvent actionEvent) {
        try {
            presenceModel.micronutrient = micronutrientManager.searchByName(presenceModel.micronutrientName.get());
            presenceModel.source = sourceManager.searchByName(presenceModel.sourceName.get());
            validatePresence();
                if (id != null) presenceManager.update(id, presenceModel.toPresence());
                else presenceManager.add(presenceModel.toPresence());
                scene.getWindow().hide();
        } catch (DaoException | IllegalArgumentException e) {
            HomeController.handleException(e.getMessage());
        }
    }
    private void validatePresence() {
        if(presenceModel.micronutrient == null) {
            micronutrientTextField.requestFocus();
            throw new IllegalArgumentException("Micronutrient with given name does not exist.");
        } else if(presenceModel.source == null) {
            sourceTextField.requestFocus();
            throw new IllegalArgumentException("Source with given name does not exist.");
        } else if(!amountTextField.getText().matches("^-?\\d*\\.{0,1}\\d+$")) { // true if theres is char that is not . or digit
            amountTextField.requestFocus();
            throw new IllegalArgumentException("Amount is not a number.");
        }
    }

    public class PresenceModel {
        public Micronutrient micronutrient = new Micronutrient();
        public Source source = new Source();
        public SimpleStringProperty micronutrientName = new SimpleStringProperty("");
        public SimpleStringProperty sourceName = new SimpleStringProperty("");
        public SimpleDoubleProperty amount = new SimpleDoubleProperty(0.0);

        public void fromPresence(Presence presence) {
            micronutrientName.set(presence.getMicronutrient().getName());
            sourceName.set(presence.getSource().getName());
            amount.set(presence.getAmount());
        }

        public Presence toPresence() throws DaoException{
            return new Presence(-1, micronutrient, source, amount.get());
        }
    }

}
