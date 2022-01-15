package cz.vse.praguePub.start;

import cz.vse.praguePub.gui.ObrazovkyController;
import javafx.application.Application;
import javafx.stage.Stage;

public class FXApp extends Application {

    public static void main(String[] args) {
        Application.launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        ObrazovkyController con = new ObrazovkyController();
        con.zapniAplikaci(primaryStage);
    }
}
