package cz.vse.praguePub.start;

import cz.vse.praguePub.gui.*;
import cz.vse.praguePub.logika.Databaze;
import cz.vse.praguePub.logika.Uzivatel;

import javafx.application.Application;
import javafx.stage.Stage;

import java.util.List;

import static cz.vse.praguePub.logika.Filtry.*;

public class FXApp extends Application {

    public static void main(String[] args) {
        Databaze.get(Uzivatel.guest()).getPodniky(podnik_mestskaCast(6)).forEach(System.out::println);
        Application.launch(args);
    }

    @Override
    public void start(Stage primaryStage) {

        List.of(
                /*new HlavniObrazovka().getScene(),
                new Filtr().getScene(),
                new ZobrazitSeznamVLokaci().getScene(),*/
                new Prihlaseni().getScene(),
                new PridejPodnikObrazovka().getScene()

        ).forEach(
                (scene) -> {
                    Stage newStage = new Stage();
                    newStage.setScene(scene);
                    newStage.show();
                });
    }
}
