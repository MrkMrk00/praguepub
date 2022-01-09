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
        Databaze db = null;
        try {
             db = Databaze.get(Uzivatel.guest());
        } catch (PraguePubDatabaseException e) {
            e.printStackTrace();
        }
        if (db == null) {
            System.out.println("dosralo se to");
            return;
        }

        var st = new Stage();
        st.setScene(new ZobrazitPodnik().getScene());
        st.showAndWait();

        new ObrazovkyController().zapniAplikaci(primaryStage);
    }
}
