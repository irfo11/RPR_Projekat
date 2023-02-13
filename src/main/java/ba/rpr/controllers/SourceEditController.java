package ba.rpr.controllers;

import ba.rpr.business.SourceManager;
import ba.rpr.dao.exceptions.DaoException;
import ba.rpr.domain.Source;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

/**
 * JavaFX controller for source editing window
 */
public class SourceEditController {
    private final Integer id;

    private SourceModel sourceModel = new SourceModel();

    private final SourceManager sourceManager = new SourceManager();

    public Scene scene;
    public TextField nameTextField;
    public Button actionButton;


    public SourceEditController(Integer id) {
        this.id = id;
    }

    @FXML
    public void initialize() {
        try {
            nameTextField.textProperty().bindBidirectional(sourceModel.name);
            if (id != null) {
                sourceModel.fromSource(sourceManager.getById(id));
                actionButton.setText("Update");
            } else actionButton.setText("Add");
        } catch(DaoException e) {
            HomeController.handleException(e.getMessage());
        }
    }

    /**
     * Action button event handler
     */
    public void save(ActionEvent actionEvent) {
        try {
            if(id != null) sourceManager.update(id, sourceModel.toSource());
            else sourceManager.add(sourceModel.toSource());
            scene.getWindow().hide();
        } catch(DaoException e) {
            HomeController.handleException(e.getMessage());
        }
    }

    /**
     * Source model class for 2 way binding
     */
    public class SourceModel {
        public SimpleStringProperty name = new SimpleStringProperty("");

        public void fromSource(Source source) {
            name.set(source.getName());
        }

        public Source toSource() {
            return new Source(-1, name.get());
        }
    }
}
