package cz.vse.praguePub.gui;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;

import java.util.HashMap;
import java.util.Map;

import static cz.vse.praguePub.gui.komponenty.Komponenty.*;

public class HlavniObrazovka extends Obrazovka<BorderPane> {
    private final Map<String, TextField> mapaInputu;

    public HlavniObrazovka() {
        super(new BorderPane(), 700, 700, "background");
        this.mapaInputu = new HashMap<>();

        this.registrujInputy();
        this.vytvorGUI();
    }

    private void registrujInputy() {
        this.mapaInputu.putAll(
                Map.of(
                        "vyhledat", TextFieldAplikace("Vyhledat", t -> {})
                )
        );
    }

    private void vytvorGUI() {
        this.getPane().setTop(
                HorniPanel(
                        (horniPanel) -> horniPanel.getChildren().addAll(
                            NadpisOknaLabel("PraguePub"),
                            this.mapaInputu.get("vyhledat"),
                            TlacitkoAplikace("Oblibene podniky", (t)->{}),
                            TlacitkoAplikace("Prihlasit se", (t)->{}))
                )
        );
    }
}
