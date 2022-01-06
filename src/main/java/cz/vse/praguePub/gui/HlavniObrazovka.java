package cz.vse.praguePub.gui;

import javafx.geometry.Pos;
import javafx.scene.layout.*;

import java.util.List;

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

                            Radek(
                                    Sloupec(List.of(), vyhl -> vyhl.setAlignment(Pos.CENTER)),
                                    Sloupec(this.getMapaInputu().get("vyhledat")),
                                    Sloupec(List.of(), oblAPrihl -> oblAPrihl.setAlignment(Pos.CENTER_RIGHT)),
                                    Sloupec(
                                            Radek(TlacitkoAplikace("Oblibene podniky", (t)->{}), TlacitkoAplikace("Prihlasit se", (t)->{})
                                    )
                                    )


                        )
                )
        ));
    }
}
