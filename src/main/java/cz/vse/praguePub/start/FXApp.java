package cz.vse.praguePub.start;

import cz.vse.praguePub.gui.HlavniObrazovka;
import cz.vse.praguePub.gui.ZobrazitSeznamVLokaci;
import javafx.application.Application;
import javafx.stage.Stage;

import java.util.List;

public class FXApp extends Application {

    public static void main(String[] args) {
        Application.launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        List.of(new HlavniObrazovka().getScene(), new ZobrazitSeznamVLokaci().getScene()).forEach(
                (scene) -> {
                    Stage newStage = new Stage();
                    newStage.setScene(scene);
                    newStage.show();
                });
    }
}
