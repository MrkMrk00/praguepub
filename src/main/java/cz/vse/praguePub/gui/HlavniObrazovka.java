package cz.vse.praguePub.gui;

import javafx.scene.layout.*;

import java.util.Map;

import static cz.vse.praguePub.gui.komponenty.Komponenty.*;

public class HlavniObrazovka extends Obrazovka<BorderPane> {

    public HlavniObrazovka() {
        super(new BorderPane(), 700, 700, "background");
        this.registrujInputy();
        this.vytvorGUI();
    }

    private void registrujInputy() {
        this.getMapaInputu().put(
                "vyhledat", TextFieldAplikace("Vyhledat", t -> {})
        );
    }

    private void vytvorGUI() {
        this.getPane().setTop(
                HorniPanel(
                        (horniPanel) -> horniPanel.getChildren().addAll(
                            NadpisOknaLabel("PraguePub"),
                            this.getMapaInputu().get("vyhledat"),
                            TlacitkoAplikace("Oblibene podniky", (t)->{}),
                            TlacitkoAplikace("Prihlasit se", (t)->{}))
                )
        );
    }
}
