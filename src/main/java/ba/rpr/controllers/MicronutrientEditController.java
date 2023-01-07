package ba.rpr.controllers;

import ba.rpr.domain.Micronutrient;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;

public class MicronutrientEditController {
    public ToggleGroup micronutrientType;
    public TextField nameTextField;
    public TextArea roleTextArea;
    public Micronutrient micronutrient;
    public boolean edit;

    public MicronutrientEditController(Micronutrient micronutrient, boolean edit) {
        this.micronutrient = micronutrient;
        this.edit = edit;
    }

    @FXML
    public void initialize() {
        if(edit) {
            nameTextField.setText(micronutrient.getName());
            roleTextArea.setText(micronutrient.getRole());
            if(micronutrient.isVitamin()) {
                micronutrientType.getToggles().get(0).setSelected(true);
            } else {
                micronutrientType.getToggles().get(1).setSelected(true);
            }
        }
    }

    public void buttonClicked(ActionEvent actionEvent) {
        micronutrient.setName(nameTextField.getText());
        micronutrient.setRole(roleTextArea.getText());
        if(micronutrientType.getSelectedToggle().toString().contains("Vitamin")) micronutrient.setVitamin(true);
        else micronutrient.setVitamin(false);
        Node node = (Node) actionEvent.getSource();
        Stage stage = (Stage) node.getScene().getWindow();
        stage.close();
    }
}
