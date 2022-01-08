package cz.vse.praguePub.start;

import com.mongodb.client.MongoCollection;
import cz.vse.praguePub.gui.*;
import cz.vse.praguePub.logika.Databaze;
import cz.vse.praguePub.logika.Uzivatel;

import cz.vse.praguePub.logika.Vysledek;
import cz.vse.praguePub.logika.dbObjekty.Pivo;
import cz.vse.praguePub.logika.dbObjekty.Podnik;
import javafx.application.Application;
import javafx.stage.Stage;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;


public class FXApp extends Application {
    private static final Logger log = LoggerFactory.getLogger(FXApp.class);

    public static void main(String[] args) {
        Application.launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        Databaze db = Databaze.get(Uzivatel.guest());

        List.of(
                new HlavniObrazovka(db).getScene(),
                new Filtr(null).getScene(),
                new ZobrazitSeznamVLokaci().getScene(),
                new Prihlaseni().getScene(),
                new PridejPodnikObrazovka(db).getScene(),
                new VyberPivoDialog(db.getPivaCollection(),null).getScene(),
                new OblibenePodniky().getScene(),
                new ZobrazitSeznamVLokaci().getScene(),
                new ZobrazitPodnik().getScene()
        ).forEach(
                (scene) -> {
                    Stage newStage = new Stage();
                    newStage.setScene(scene);
                    newStage.show();
                    newStage.setResizable(false);
                });
    }
}
