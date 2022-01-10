package cz.vse.praguePub.start;

import cz.vse.praguePub.gui.ObrazovkyController;
import cz.vse.praguePub.gui.obrazovky.ZobrazitPodnik;
import cz.vse.praguePub.logika.Databaze;
import cz.vse.praguePub.logika.Uzivatel;
import cz.vse.praguePub.util.PraguePubDatabaseException;
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
