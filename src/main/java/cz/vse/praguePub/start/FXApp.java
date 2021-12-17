package cz.vse.praguePub.start;


import cz.vse.praguePub.logika.Databaze;
import cz.vse.praguePub.logika.Uzivatel;

import cz.vse.praguePub.gui.HlavniObrazovka;
import cz.vse.praguePub.gui.ZobrazitSeznamVLokaci;
import cz.vse.praguePub.gui.komponenty.Tabulka;
import cz.vse.praguePub.logika.dbObjekty.Pivo;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.util.List;
import java.util.Map;

public class FXApp extends Application {

    public static void main(String[] args) {
        new Databaze(Uzivatel.guest()).getPodnikyVMestskeCasti(6).forEach(it -> System.out.println(it.getNazev()));
        Application.launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        var bp = new BorderPane();
        Tabulka<Pivo> tab = new Tabulka<>(Map.of("nazev", "nazev"));
        tab.setRadky(List.of(new Pivo("abc","asd", 6, 6, "Asd", "asd", 6, 6)));
        bp.setCenter(tab.getTableView());
        var st = new Stage();
        st.setScene(new Scene(bp, 400, 400));
        st.show();


        List.of(new HlavniObrazovka().getScene(), new ZobrazitSeznamVLokaci().getScene()).forEach(
                (scene) -> {
                    Stage newStage = new Stage();
                    newStage.setScene(scene);
                    newStage.show();
                });
    }
}
