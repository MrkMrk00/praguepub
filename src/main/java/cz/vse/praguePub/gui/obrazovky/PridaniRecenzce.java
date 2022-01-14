package cz.vse.praguePub.gui.obrazovky;

import cz.vse.praguePub.gui.ObrazovkyController;
import cz.vse.praguePub.gui.obrazovky.abstraktniObrazovky.Obrazovka;
import cz.vse.praguePub.logika.dbObjekty.Podnik;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;

import java.util.List;
import java.util.Map;

import static cz.vse.praguePub.gui.komponenty.Komponenty.*;

public class PridaniRecenzce extends Obrazovka<BorderPane> {

    private final ObrazovkyController controller;
    private final Podnik zobrazovanyPodnik;

    private final StringProperty nazevPodniku = new SimpleStringProperty();

    public PridaniRecenzce(ObrazovkyController controller, Podnik podnik) {
        super(new BorderPane(), 500, 400, "background");

        this.controller = controller;
        this.zobrazovanyPodnik = podnik;


        this.nactiAtributyPodniku();
        this.registrujInputy();
        this.vytvorGUI();
    }

    private void nactiAtributyPodniku() {
        this.nazevPodniku.setValue(this.zobrazovanyPodnik.getNazev());

    }

    private void registrujInputy() {
        this.getMapaInputu().putAll(
                Map.of(
                        "recenze", TextFieldAplikace("Začněte psát", t -> t.setMaxWidth(300d)
                )
        ));
    }

    private void vytvorGUI() {
        this.getPane().setTop(
                HorniPanel(
                        horniPanel -> {
                            Label nadpis =  NadpisOknaLabel(this.nazevPodniku.toString());
                            nadpis.textProperty().bind(this.nazevPodniku);
                            horniPanel.getChildren().add(nadpis);
                            horniPanel.setAlignment(Pos.BASELINE_CENTER);

                        }
                )
        );

        this.getPane().setCenter(
                Sloupec(List.of(
                        NadpisOknaLabel("Recenze:"),
                        this.getMapaInputu().get("recenze"),
                        TlacitkoAplikace("Odeslat", t -> t.setStyle("-fx-font-weight: bold;"))
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



