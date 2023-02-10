import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;

public class App extends Application {

    public static void main(String[] args) throws ClassNotFoundException{
        Class.forName("ba.rpr.dao.DaoFactory"); //to immediately get connection
        launch();
    }

    @Override
    public void start(Stage stage) throws Exception {
        stage.setScene(FXMLLoader.load(getClass().getResource("/fxml/home.fxml")));
        stage.show();
    }
}
