package cz.vse.praguePub.start;

import com.mongodb.client.MongoCollection;
import cz.vse.praguePub.gui.*;
import cz.vse.praguePub.logika.Databaze;
import cz.vse.praguePub.logika.PivoFiltrBuilder;
import cz.vse.praguePub.logika.PodnikFiltrBuilder;
import cz.vse.praguePub.logika.Uzivatel;

import cz.vse.praguePub.logika.dbObjekty.Pivo;
import cz.vse.praguePub.logika.dbObjekty.Podnik;
import javafx.application.Application;
import javafx.stage.Stage;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;


public class FXApp extends Application {
    private static final Logger log = LoggerFactory.getLogger(FXApp.class);

    public static void main(String[] args) {
        Databaze db = Databaze.get(Uzivatel.guest());

        MongoCollection<Document> pivaKolekce = db.getPivaCollection();
        Pivo pivo = new PivoFiltrBuilder(pivaKolekce)
                .pivovar("Staropramen")
                .nazev("Staropra")
                .finalizuj().get(0);
        log.info(pivo.toString());

        MongoCollection<Document> kolekcePodniku = db.getPodnikyCollection();
        Podnik podnik = new PodnikFiltrBuilder(kolekcePodniku)
                .cenaPiva(5, 30)
                .cisloMestskeCasti(6)
                .prumerneHodnoceni(3, 5)
                .finalizuj(pivaKolekce)
                .get(0);
        log.info(podnik.toString());

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
