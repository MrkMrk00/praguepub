package cz.vse.praguePub.gui;

import cz.vse.praguePub.logika.Databaze;
import javafx.geometry.Insets;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;

import java.util.Map;

import static cz.vse.praguePub.gui.komponenty.Komponenty.*;

public class HlavniObrazovka extends Obrazovka<BorderPane> {

    private final Databaze databaze;

    private final Runnable zobrazOblibenePodniky = () -> zobrazOkno(new OblibenePodniky().getScene());

    public HlavniObrazovka(Databaze databaze) {
        super(new BorderPane(), 700, 700, "background");
        this.databaze = databaze;

        this.registrujInputy(this.getMapaInputu());
        this.vytvorGUI(this.getPane());
    }

    private void registrujInputy(Map<String, TextField> mapaInputu) {
        mapaInputu.put(
                "vyhledat", TextFieldAplikace("Vyhledat", t -> {
                    HBox.setMargin(t, new Insets(6,0,0,5));
                })
        );
    }

    private void vytvorGUI(BorderPane pane) {
        pane.setTop(
                HorniPanel(hp -> {
                    BorderPane vrchniBar = new BorderPane();

                    vrchniBar.setLeft(
                            Radek(
                                    NadpisOknaLabel("PraguePub"),
                                    this.getMapaInputu().get("vyhledat"),
                                    TlacitkoAplikace("Oblibene podniky", e -> this.zobrazOblibenePodniky.run(), null)
                            )
                    );

                    vrchniBar.setRight(
                            Radek(
                                    TlacitkoAplikace("Prihlasit se",
                                            t -> HBox.setMargin(t, new Insets(6, 8, 0, 0))
                                    )
                            )
                    );

                    hp.getChildren().add(vrchniBar);
                    hp.setPrefWidth(Integer.MAX_VALUE);
                    vrchniBar.setPrefWidth(Integer.MAX_VALUE);

                })
        );








    }
}
