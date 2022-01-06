package cz.vse.praguePub.gui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.shape.SVGPath;

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
                "vyhledat", TextFieldAplikace("Vyhledat", t -> {
                    HBox.setMargin(t, new Insets(6,0,0,5));
                })
        );
    }

    private void vytvorGUI() {
        this.getPane().setTop(
                HorniPanel(hp -> {
                    BorderPane vrchniBar = new BorderPane();
                    vrchniBar.setLeft(
                            Radek(NadpisOknaLabel("PraguePub"),
                                  this.getMapaInputu().get("vyhledat"),
                                    TlacitkoAplikace("Oblibene podniky", (t) -> {})
                            )
                    );
                    vrchniBar.setRight(Radek(TlacitkoAplikace("Prihlasit se", (t) -> {HBox.setMargin(t, new Insets(6, 8, 0, 0));})));
                    hp.getChildren().add(vrchniBar);
                    hp.setPrefWidth(Integer.MAX_VALUE);
                    vrchniBar.setPrefWidth(Integer.MAX_VALUE);

                       }

                )
        );








    }
}
