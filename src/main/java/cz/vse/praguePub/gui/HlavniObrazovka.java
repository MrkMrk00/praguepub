package cz.vse.praguePub.gui;

import com.mongodb.MongoException;
import cz.vse.praguePub.logika.Databaze;
import cz.vse.praguePub.logika.Uzivatel;
import cz.vse.praguePub.logika.dbObjekty.Podnik;
import javafx.geometry.Insets;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Supplier;

import static cz.vse.praguePub.gui.komponenty.Komponenty.*;

public class HlavniObrazovka extends Obrazovka<BorderPane> {

    private Databaze databaze = Databaze.get(Uzivatel.guest());

    /*
     * Část kódu, která se stará o zobrazování oken a návaznost oken mezi sebou
     */
    private void zobrazOblibenePodniky() {
        Supplier<List<Podnik>> ziskejOblibenePodniky = () -> this.databaze.getOblibenePodniky();
        Consumer<Podnik> odeberPodnik = podnik -> this.databaze.odeberZOblibenych(podnik);
        Consumer<Podnik> upravPodnik = this::zobrazUpraveniPodniku;

        if (this.databaze.getUzivatel().isGuest()) {
            this.zobrazPrihlaseni();
            return;
        }
        zobrazOkno(new OblibenePodniky(ziskejOblibenePodniky, odeberPodnik, upravPodnik).getScene());
    }

    private void zobrazPrihlaseni() {
        final Stage prihlaseniStage = new Stage();

        BiFunction<String, String, Boolean> prihlas = (jmeno, heslo) -> {
            Uzivatel novyUzivatel;
            try {
                novyUzivatel = new Uzivatel(jmeno, heslo);
            } catch (MongoException e) {
                return false;
            }
            this.databaze = Databaze.get(novyUzivatel);
            return this.databaze.getUzivatel().isPrihlasen();
        };

        Runnable hidePozadavek = prihlaseniStage::hide;

        prihlaseniStage.setScene(new Prihlaseni(prihlas, hidePozadavek).getScene());
        prihlaseniStage.show();
    }

    private void zobrazUpraveniPodniku(Podnik podnik) {
        zobrazOkno(new UpravitPodnikObrazovka(podnik).getScene());
    }

    /*
     * ========================================================================
     */

    public HlavniObrazovka() {
        super(new BorderPane(), 700, 700, "background");

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
                                    TlacitkoAplikace("Oblibene podniky", e -> this.zobrazOblibenePodniky(), null)
                            )
                    );

                    vrchniBar.setRight(
                            Radek(
                                    TlacitkoAplikace(
                                            "Prihlasit se",
                                            mouseEvent -> this.zobrazPrihlaseni(),
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
