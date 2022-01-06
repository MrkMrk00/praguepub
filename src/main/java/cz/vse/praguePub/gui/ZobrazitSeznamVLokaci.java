package cz.vse.praguePub.gui;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import static cz.vse.praguePub.gui.komponenty.Komponenty.HorniPanel;
import static cz.vse.praguePub.gui.komponenty.Komponenty.TlacitkoAplikace;

public class ZobrazitSeznamVLokaci extends Obrazovka<BorderPane> {

    public ZobrazitSeznamVLokaci() {
        super(new BorderPane(), 700, 700, "background");
        this.nastaveni();
    }

    private void nastaveni() {
        this.getPane().setTop(
                HorniPanel((horniPanel) -> {
                    Label nazevLokace = new Label("Praha 10");
                    nazevLokace.setFont(Font.font("Helvetica", FontWeight.BOLD, 30));
                    nazevLokace.setAlignment(Pos.BASELINE_LEFT);

                    Button pridatNovyPodnik = TlacitkoAplikace("Pridat novy podnik", (t)->{});
                    Button filtrovat = TlacitkoAplikace("Filtrovat", (t)->{});

;                    horniPanel.getChildren().addAll(nazevLokace, pridatNovyPodnik, filtrovat);
                })
        );
    }

}
