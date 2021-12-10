package cz.vse.praguePub.start;

import javafx.application.Application;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import cz.vse.praguePub.util.AlertBuilder;

import static cz.vse.praguePub.util.Util.utf8encode;

public class FXApp extends Application {

    public static void main(String[] args) {
        Application.launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        new AlertBuilder(Alert.AlertType.INFORMATION)
                .setHeaderText(utf8encode("Zdar!"))
                .setContent(utf8encode("Týmová semestrální práce"))
                .getAlert()
                .showAndWait();
    }
}
