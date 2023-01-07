package ba.rpr.controllers;

import ba.rpr.dao.DaoFactory;
import ba.rpr.dao.exceptions.DaoException;
import ba.rpr.domain.Presence;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class PresenceEditController {
    public TextField micronutrientTextField;
    public TextField sourceTextField;
    public TextField amountTextField;
    public Presence presence;
    public boolean edit;

    public PresenceEditController(Presence presence, boolean edit) {
        this.presence = presence;
        this.edit = edit;
    }

    @FXML
    public void initialize() {
        if(edit) {
            micronutrientTextField.setText(presence.getMicronutrient().getName());
            sourceTextField.setText(presence.getSource().getName());
            amountTextField.setText(Double.toString(presence.getAmount()));
        }
    }

    public void buttonClicked(ActionEvent actionEvent) throws DaoException {
        presence.setMicronutrient(DaoFactory.micronutrientDao().searchByName(micronutrientTextField.getText()));
        presence.setSource(DaoFactory.sourceDao().searchByName(sourceTextField.getText()));
        presence.setAmount(Double.parseDouble(amountTextField.getText()));
        Node node = (Node) actionEvent.getSource();
        Stage stage = (Stage) node.getScene().getWindow();
        stage.close();
    }

}
