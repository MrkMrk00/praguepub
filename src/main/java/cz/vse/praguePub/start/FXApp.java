package cz.vse.praguePub.start;

import cz.vse.praguePub.gui.ZobrazitSeznamVLokaci;
import javafx.application.Application;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import cz.vse.praguePub.util.AlertBuilder;

public class FXApp extends Application {

    public static void main(String[] args) {
        Application.launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        //new Prihlaseni(new BorderPane());

        //new HlavniObrazovka();
        new ZobrazitSeznamVLokaci();

        new AlertBuilder(Alert.AlertType.INFORMATION)
                .setHeaderText("Zdar!")
                .setContent("Týmová semestrální práce")
                .getAlert()
                .showAndWait();
    }
}
