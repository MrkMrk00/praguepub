package cz.vse.praguePub.start;

import cz.vse.praguePub.gui.HlavniObrazovka;
import cz.vse.praguePub.logic.User;
import javafx.application.Application;
import javafx.scene.control.Alert;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import cz.vse.praguePub.util.AlertBuilder;

import static cz.vse.praguePub.util.Util.utf8encode;

public class FXApp extends Application {

    public static void main(String[] args) {
        Application.launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        new HlavniObrazovka();

    }
}
