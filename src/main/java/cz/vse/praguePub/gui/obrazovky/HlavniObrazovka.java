package cz.vse.praguePub.gui.obrazovky;

import cz.vse.praguePub.gui.ObrazovkyController;
import cz.vse.praguePub.gui.obrazovky.abstraktniObrazovky.Obrazovka;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;

import java.io.InputStream;
import java.util.Map;

import static cz.vse.praguePub.gui.komponenty.Komponenty.*;

public class HlavniObrazovka extends Obrazovka<BorderPane> {

    private final ObrazovkyController controller;

    public HlavniObrazovka(ObrazovkyController controller) {
        super(new BorderPane(), 680, 600, "background");

        this.controller = controller;
        this.registrujInputy(this.getMapaInputu());
        this.vytvorGUI(this.getPane());
    }

    /**
     * Zaregistruje textové vstupy do jedné mapy pro jednodušší přístup
     * @param mapaInputu mapa, do které se instance TextField přidají
     */
    private void registrujInputy(Map<String, TextField> mapaInputu) {
        mapaInputu.put(
                "vyhledat", TextFieldAplikace("Vyhledat", t -> {
                    HBox.setMargin(t, new Insets(6,0,0,5));

                    t.setOnKeyPressed(
                            keyEvent -> {
                                if (keyEvent.getCode() == KeyCode.ENTER) {
                                    this.controller.zobrazVyhledatPodleNazvu(t.getText());
                                }
                            }
                    );
                })
        );
    }

    private StackPane pripravMapu() {
        StackPane mapaPane = new StackPane();

        for (int i = 1; i <= 22; i++) {
            final InputStream obrIS = this.getClass().getResourceAsStream("/castiMapy/Praha" + i + ".png");
            if (obrIS == null) continue;

            ImageView imageView = new ImageView(
                    new Image(obrIS, 889d, 817d, false, true)
            );

            final int iFinal = i;

            imageView.setOnMouseClicked(
                    event -> {
                        this.controller.zobrazPodnikyVOblasti(iFinal);
                    }
            );

            mapaPane.getChildren().add(imageView);
        }

        return mapaPane;
    }

    /**
     * Metoda obsahuje tvorbu GUI.
     * @param pane hlavní Parent okna
     */
    private void vytvorGUI(BorderPane pane) {
        pane.setTop(
                HorniPanel(hp -> {
                    Label nadpisOkna = NadpisOknaLabel("PraguePub");
                    TextField inputVyhledat = this.getMapaInputu().get("vyhledat");
                    Button oblibenePodniky = TlacitkoAplikace(
                            "Oblibene podniky",
                            e -> this.controller.zobrazOblibenePodniky(),
                            null
                    );

                    Button prihlasitSe = TlacitkoAplikace(
                            "Prihlasit se",
                            mouseEvent -> this.controller.zobrazPrihlaseni(),
                            t -> HBox.setMargin(t, new Insets(6, 8, 0, 0))
                    );

                    Region separator = new Region();
                        HBox.setHgrow(separator, Priority.ALWAYS);

                    VBox tlacitkoVBox = Sloupec(prihlasitSe);
                    tlacitkoVBox.setAlignment(Pos.CENTER_LEFT);

                    hp.getChildren().addAll(
                            Radek(nadpisOkna, inputVyhledat, oblibenePodniky), separator, Radek(prihlasitSe)
                    );

                    hp.setPrefWidth(Integer.MAX_VALUE);
                })
        );

        pane.setCenter(this.pripravMapu());


    }
}
