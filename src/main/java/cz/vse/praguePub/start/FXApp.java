package cz.vse.praguePub.start;


import cz.vse.praguePub.logika.Databaze;
import cz.vse.praguePub.logika.Uzivatel;

import cz.vse.praguePub.gui.komponenty.Tabulka;
import cz.vse.praguePub.logika.dbObjekty.Pivo;

import cz.vse.praguePub.logika.dbObjekty.Podnik;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.util.List;

public class FXApp extends Application {

    public static void main(String[] args) {
        Databaze.get(Uzivatel.guest()).getPodnikyPodleCenyPiva(0, 30).forEach(it -> System.out.println(it.toString()));
        Application.launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        var bp = new BorderPane();
        Tabulka<Pivo> tab = new Tabulka<>(Pivo.PRO_TABULKU);
        tab.setRadky(
                List.of(Pivo.inicializujZDokumentu(Uzivatel.guest().getPraguePubDatabaze().getCollection("piva").find().first(), 31d, 20d)));
        bp.setCenter(tab.getTableView());
        var st = new Stage();
        st.setScene(new Scene(bp, 900, 400));
        st.show();

        var bp2 = new BorderPane();
        Tabulka<Podnik> tab2 = new Tabulka<>(Podnik.PRO_TABULKU);
        tab2.setRadky(Databaze.get(Uzivatel.guest()).getPodnikyVMestskeCasti(6).stream().toList());
        bp2.setCenter(tab2.getTableView());
        var st2 = new Stage();
        st2.setScene(new Scene(bp2, 900, 400));
        st2.show();


        /*List.of(new HlavniObrazovka().getScene(), new ZobrazitSeznamVLokaci().getScene()).forEach(
                (scene) -> {
                    Stage newStage = new Stage();
                    newStage.setScene(scene);
                    newStage.show();
                });*/
    }
}
