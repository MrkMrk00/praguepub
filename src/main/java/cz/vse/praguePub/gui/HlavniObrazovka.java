package cz.vse.praguePub.gui;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import static cz.vse.praguePub.gui.komponenty.Komponenty.*;

public class HlavniObrazovka extends Obrazovka<BorderPane> {

    public HlavniObrazovka() {
        super(new BorderPane(), 700, 700, "background");
        this.nastaveni();
    }

    private void nastaveni() {
        this.getPane().setTop(
                HorniPanel((horniPanel) -> {
                    Button prihlasitSe = TlacitkoAplikace("Prihlasit se", (t)->{});
                    Button oblibenePodniky = TlacitkoAplikace("Oblibene podniky", (t)->{});

                    TextField vyhledavani = new TextField("Vyhledat");
                    vyhledavani.getStyleClass().add("tlacitkoAplikace");
                    vyhledavani.autosize();

                    Label nazevLabel = new Label("Prague Pub");
                    nazevLabel.setFont(Font.font("Helvetica", FontWeight.BOLD, 30));
                    nazevLabel.setAlignment(Pos.BASELINE_LEFT);

                    horniPanel.getChildren().addAll(nazevLabel, vyhledavani, oblibenePodniky, prihlasitSe);
                })
        );
    }
}
