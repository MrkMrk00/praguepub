package cz.vse.praguePub.gui.obrazovky;

import cz.vse.praguePub.gui.obrazovky.abstraktniObrazovky.Obrazovka;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.BorderPane;

import java.util.List;
import java.util.Map;

import static cz.vse.praguePub.gui.komponenty.Komponenty.*;

public class PridaniRecenzce extends Obrazovka<BorderPane> {
    public PridaniRecenzce() {
        super(new BorderPane(), 500, 400, "background");

        this.registrujInputy();
        this.vytvorGUI();
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
                            horniPanel.getChildren().add(NadpisOknaLabel("tady bude nazev podniku"));
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



