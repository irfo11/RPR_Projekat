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
            if(id != null) presenceManager.update(id, presenceModel.toPresence());
            else presenceManager.add(presenceModel.toPresence());
            scene.getWindow().hide();
        } catch(DaoException e) {
            HomeController.handleException(e.getMessage());
        }
    }

    public class PresenceModel {
        public SimpleStringProperty micronutrientName = new SimpleStringProperty("");
        public SimpleStringProperty sourceName = new SimpleStringProperty("");
        public SimpleDoubleProperty amount = new SimpleDoubleProperty(0.0);

        public void fromPresence(Presence presence) {
            micronutrientName.set(presence.getMicronutrient().getName());
            sourceName.set(presence.getSource().getName());
            amount.set(presence.getAmount());
        }

        public Presence toPresence() throws DaoException{
            Micronutrient micronutrient = micronutrientManager.searchByName(micronutrientName.get());
            if(micronutrient == null) throw new DaoException("Micronutrient with given name does not exist");
            Source source = sourceManager.searchByName(sourceName.get());
            if(source == null) throw new DaoException("Source with given name does not exist");
            return new Presence(-1, micronutrient, source, amount.get());
        }
    }

}
