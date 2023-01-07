package ba.rpr.controllers;

import ba.rpr.domain.Source;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/*Uradi onHide metodu kada se sa X izade*/
public class SourceEditController {
    private Source source;
    private boolean edit;
    public TextField nameTextField;

    public SourceEditController(Source source, boolean edit) {
        this.source = source;
        this.edit = edit;
    }

    @FXML
    public void initialize() {
        if(edit) nameTextField.setText(source.getName());
    }

    public void buttonClicked(ActionEvent actionEvent) {
        source.setName(nameTextField.getText());
        Node source = (Node) actionEvent.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }

}
