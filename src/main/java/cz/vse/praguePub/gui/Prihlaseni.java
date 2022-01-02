package cz.vse.praguePub.gui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static cz.vse.praguePub.gui.komponenty.Komponenty.*;

public class Prihlaseni extends Obrazovka<BorderPane> {
    public Prihlaseni() {
        super(new BorderPane(), 300, 350, "background");

        this.registrujInputy();
        this.vytvorGUI();
    }

    private void registrujInputy() {
        this.getMapaInputu().putAll(
                Map.of(
                        "jmeno", TextFieldAplikace("Jméno", t -> t.setMaxWidth(150d)),
                        "heslo", TextFieldAplikace("Heslo", t -> t.setMaxWidth(150d))
                )
        );
    }

    private void vytvorGUI() {
        Hyperlink link = new Hyperlink("Vytvořit účet");
        link.setFont(Font.font("Helvetica", FontWeight.BOLD, 10));
        link.setAlignment(Pos.BASELINE_CENTER);

        this.getPane().setTop(
                Sloupec(List.of(
                        NadpisOknaLabel("Vítej zpět!"),
                        this.getMapaInputu().get("jmeno"),
                        this.getMapaInputu().get("heslo"),
                        TlacitkoAplikace("Přihlásit se", t -> t.setStyle("-fx-font-weight: bold;"))
                ),
                    textFieldVBox -> {
                        textFieldVBox.setSpacing(20);
                        textFieldVBox.setPadding(new Insets(60,0,20,0));
                        textFieldVBox.setAlignment(Pos.BASELINE_CENTER);
                    }
                )
        );
    }
}



