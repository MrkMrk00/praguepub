package cz.vse.praguePub.start;

import cz.vse.praguePub.gui.ObrazovkyController;
<<<<<<< HEAD
=======
import cz.vse.praguePub.gui.obrazovky.Cenik;
>>>>>>> gui
import javafx.application.Application;
import javafx.stage.Stage;

public class FXApp extends Application {

    public static void main(String[] args) {
        Application.launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        new ObrazovkyController().zapniAplikaci(primaryStage);
    }
}
