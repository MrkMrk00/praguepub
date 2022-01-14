package cz.vse.praguePub.start;

import cz.vse.praguePub.gui.ObrazovkyController;
import cz.vse.praguePub.gui.obrazovky.PridejPodnikObrazovka;
import cz.vse.praguePub.logika.Databaze;
import cz.vse.praguePub.logika.Uzivatel;
import javafx.application.Application;
import javafx.stage.Stage;

public class FXApp extends Application {

    public static void main(String[] args) {
        Application.launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        ObrazovkyController con = new ObrazovkyController();
        try {
            con.setDatabaze(Databaze.get(new Uzivatel("marek", "123456")));
        } catch (Exception e) {
            e.printStackTrace();
        }
        Stage st = new Stage();
        st.setScene(new PridejPodnikObrazovka(con).getScene());
        st.show();
        //con.zapniAplikaci(primaryStage);
    }
}
