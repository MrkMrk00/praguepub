package cz.vse.praguePub.gui.obrazovky;

import cz.vse.praguePub.gui.ObrazovkyController;
import cz.vse.praguePub.gui.komponenty.AlertBuilder;
import cz.vse.praguePub.gui.obrazovky.abstraktniObrazovky.Obrazovka;
import cz.vse.praguePub.logika.TypVysledku;
import cz.vse.praguePub.logika.Vysledek;
import cz.vse.praguePub.logika.dbObjekty.Podnik;
import cz.vse.praguePub.logika.dbObjekty.Recenze;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import org.apache.commons.lang3.math.NumberUtils;
import org.bson.types.ObjectId;

import java.util.List;
import java.util.Map;

import static cz.vse.praguePub.gui.komponenty.AlertBuilder.ChybaDatabazeAlert;
import static cz.vse.praguePub.gui.komponenty.AlertBuilder.UspesneVlozenObjekt;
import static cz.vse.praguePub.gui.komponenty.Komponenty.*;

public class PridaniRecenzce extends Obrazovka<BorderPane> {

    private final ObrazovkyController controller;
    private final Podnik zobrazovanyPodnik;
    private final Runnable callback;

    public PridaniRecenzce(ObrazovkyController controller, Podnik podnik, Runnable callback) {
        super(new BorderPane(), 500, 400, "background");

        this.controller = controller;
        this.zobrazovanyPodnik = podnik;
        this.callback = callback;

        this.registrujInputy();
        this.vytvorGUI();
    }

    private void registrujInputy() {
        this.getMapaInputu().putAll(
                Map.of(
                        "recenze", TextFieldAplikace("Začněte psát", t -> t.setMaxWidth(300d)),
                        "hodnoceni", TextFieldAplikace("Zadejte hodnoceni", t -> t.setMaxWidth(300d))
                )
        );
    }

    private void odeslat() {
        boolean chybne = false;

        String textRecenze = this.getMapaInputu().get("recenze").getText();
        if (textRecenze == null || textRecenze.isBlank()) chybne = true;

        double hodnoceni = NumberUtils.toDouble(this.getMapaInputu().get("hodnoceni").getText(), -1);
        if (hodnoceni < 0 || hodnoceni > 5) chybne = true;

        if (!chybne) {
            ObjectId uzivatelID = this.controller.getDatabaze().getUzivatel().get_id();
            Recenze novaRecenze = new Recenze(
                    uzivatelID,
                    textRecenze,
                    hodnoceni
            );

            this.zobrazovanyPodnik.getRecenze().add(novaRecenze);
            Vysledek<Podnik> vysledek = this.controller.getDatabaze().upravPodnik(this.zobrazovanyPodnik);

            if (vysledek.getTypVysledku() == TypVysledku.OK) {
                UspesneVlozenObjekt(novaRecenze);
                this.callback.run();
            }
            else if (vysledek.getTypVysledku() == TypVysledku.DB_CHYBA) ChybaDatabazeAlert("Chyba databáze");
            return;
        }

        new AlertBuilder(Alert.AlertType.ERROR)
                .setTitle("Prague Pub")
                .setHeaderText("Chyba")
                .setContent("Zadej prosím všechny hodnoty správně")
                .getAlert().show();
    }

    private void vytvorGUI() {
        this.getPane().setTop(
                HorniPanel(
                        horniPanel -> {
                            Label nadpis =  NadpisOknaLabel(this.zobrazovanyPodnik.getNazev());
                            horniPanel.getChildren().add(nadpis);
                            horniPanel.setAlignment(Pos.BASELINE_CENTER);

                        }
                )
        );

        this.getPane().setCenter(
                Sloupec(List.of(
                        NadpisOknaLabel("Recenze:"),
                        this.getMapaInputu().get("recenze"),
                        LabelAplikace("Hodnocení:"),
                        this.getMapaInputu().get("hodnoceni"),
                        TlacitkoAplikace("Odeslat", mEvent -> this.odeslat(), t -> t.setStyle("-fx-font-weight: bold;"))
                        ),
                        textFieldVBox -> {
                            textFieldVBox.setSpacing(15);
                            textFieldVBox.setPadding(new Insets(60,0,20,0));
                            textFieldVBox.setAlignment(Pos.BASELINE_CENTER);
                        }
                )
        );
    }
}



