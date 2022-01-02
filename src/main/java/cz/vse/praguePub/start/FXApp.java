package cz.vse.praguePub.start;

import cz.vse.praguePub.gui.*;
import cz.vse.praguePub.logika.Databaze;
import cz.vse.praguePub.logika.Filtry;
import cz.vse.praguePub.logika.Uzivatel;

import cz.vse.praguePub.gui.komponenty.Tabulka;
import cz.vse.praguePub.logika.dbObjekty.Pivo;

import cz.vse.praguePub.logika.dbObjekty.Podnik;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.bson.Document;

import java.util.List;

import static cz.vse.praguePub.logika.Filtry.*;

public class FXApp extends Application {

    public static void main(String[] args) {
        Databaze.get(Uzivatel.guest()).getPodniky(podnik_mestskaCast(6)).forEach(System.out::println);
        Application.launch(args);
    }

    @Override
    public void start(Stage primaryStage) {

        var bp = new BorderPane();
        Tabulka<Pivo> tab = new Tabulka<>(Pivo.PRO_TABULKU);
        Document found = Uzivatel.guest()
                .getPraguePubDatabaze()
                .getCollection("podniky")
                .find(new Filtry()
                        .pridejFilter(podnik_cenaPiva(20, 30))
                        .pridejFilter(podnik_mestskaCast(6))
                        .finalizuj()
                ).first();
        bp.setCenter(tab.getTableView());

        var bp2 = new BorderPane();
        Tabulka<Podnik> tab2 = new Tabulka<>(Podnik.PRO_TABULKU);
        tab2.setRadky(List.of(Podnik.inicializujZDokumentu(found, Uzivatel.guest().getPraguePubDatabaze().getCollection("piva"))));
        bp2.setCenter(tab2.getTableView());


        List.of(
                new HlavniObrazovka().getScene(),
                new Filtr().getScene(),
                new ZobrazitSeznamVLokaci().getScene(),
                new Prihlaseni().getScene(),
                new Scene(bp, 900, 400),
                new Scene(bp2, 900, 400),
                new PridejPodnik().getScene()

        ).forEach(
                (scene) -> {
                    Stage newStage = new Stage();
                    newStage.setScene(scene);
                    newStage.show();
                });
    }
}
