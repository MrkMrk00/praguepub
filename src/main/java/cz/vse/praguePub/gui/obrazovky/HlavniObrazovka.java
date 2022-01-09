package cz.vse.praguePub.gui.obrazovky;

import com.mongodb.MongoException;
import cz.vse.praguePub.gui.ObrazovkyController;
import cz.vse.praguePub.gui.komponenty.AlertBuilder;
import cz.vse.praguePub.logika.Databaze;
import cz.vse.praguePub.logika.Uzivatel;
import cz.vse.praguePub.logika.dbObjekty.Podnik;
import cz.vse.praguePub.util.PraguePubDatabaseException;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Supplier;

import static cz.vse.praguePub.gui.komponenty.Komponenty.*;

/**
 * Třída hlavní obrazovky, která v sobě zároveň obsahuje logiku otevírání oken a
 * návaznosti obrazovek na sebe.<br>
 * Návaznost je řešena <b>funkcionálně</b>.
 */
public class HlavniObrazovka extends Obrazovka<BorderPane> {

    private final ObrazovkyController controller;

    public HlavniObrazovka(ObrazovkyController controller) {
        super(new BorderPane(), 700, 700, "background");

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
                })
        );
    }

    /**
     * Metoda obsahuje tvorbu GUI.
     * @param pane hlavní Parent okna
     */
    private void vytvorGUI(BorderPane pane) {
        pane.setTop(
                HorniPanel(hp -> {
                    BorderPane vrchniBar = new BorderPane();

                    vrchniBar.setLeft(
                            Radek(
                                    NadpisOknaLabel("PraguePub"),
                                    this.getMapaInputu().get("vyhledat"),
                                    TlacitkoAplikace("Oblibene podniky", e -> this.controller.zobrazOblibenePodniky(), null)
                            )
                    );

                    vrchniBar.setRight(
                            Radek(
                                    TlacitkoAplikace(
                                            "Prihlasit se",
                                            mouseEvent -> this.controller.zobrazPrihlaseni(),
                                            t -> HBox.setMargin(t, new Insets(6, 8, 0, 0))
                                    )
                            )
                    );

                    hp.getChildren().add(vrchniBar);
                    hp.setPrefWidth(Integer.MAX_VALUE);
                    vrchniBar.setPrefWidth(Integer.MAX_VALUE);

                })
        );

        pane.setCenter(
                new ImageView(new Image(this.getClass().getResourceAsStream("/castiMapy/Praha5.png")))
        );
    }
}
