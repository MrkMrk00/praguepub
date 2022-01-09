package cz.vse.praguePub.gui;

import com.mongodb.MongoException;
import cz.vse.praguePub.gui.komponenty.AlertBuilder;
import cz.vse.praguePub.logika.Databaze;
import cz.vse.praguePub.logika.Uzivatel;
import cz.vse.praguePub.logika.dbObjekty.Podnik;
import cz.vse.praguePub.util.PraguePubDatabaseException;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import lombok.Data;

import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
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

    private Databaze databaze = null;

    /*
     * Část kódu, která se stará o zobrazování oken a návaznost oken mezi sebou
     */

    /**
     * Metoda zobrazí alert a vypne aplikaci
     * @param errorText text, který se zobrazí v alertu
     */
    private void nepovedenePrihlaseni(String errorText) {
        new AlertBuilder(Alert.AlertType.ERROR)
                .setTitle("PraguePub")
                .setHeaderText("Chyba databáze")
                .setContent(errorText)
                .getAlert()
                .showAndWait();

        System.exit(0);
    }

    /**
     * Metoda otevírá okno s oblíbenými podniky.<p>
     * () -> List&lt;Podnik&gt; &emsp; <b>ziskejOblibenePodniky</b>: dotaz do databáze - vrací podniky z databáze <br>
     * (Podnik) -> void&emsp;&emsp; <b>odeberPodnik</b>: dotaz do databáze - odebírá podnik z oblíbených podniků uživatele <br>
     * (Podnik) -> void&emsp;&emsp; <b>upravPodnik</b>: dotaz do databáze - otevře okno úpravy podniku <br>
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

    /**
     * Zobrazí okno s přihlášením.<p>
     * (String, String) -> Boolean &emsp; <b>prihlas</b>: metoda se pokusí vytvořít nového uživatele
     * a přiřadit instanční proměnné databaze novou instanci s přihlášeným uživatelem.<br>
     * () -> void &emsp; <b>hidePozadavek</b>: slouží k zavření okna s přihlášením. Když se
     * uživateli povede úspěšně přihlásit, tak se okno automaticky zavře.
     */
    private void zobrazPrihlaseni() {
        final Stage prihlaseniStage = new Stage();

        BiFunction<String, String, Boolean> prihlas = (jmeno, heslo) -> {
            Uzivatel novyUzivatel;
            try {
                novyUzivatel = new Uzivatel(jmeno, heslo);
                this.databaze = Databaze.get(novyUzivatel);
            } catch (MongoException | PraguePubDatabaseException e) {
                return false;
            }

            return this.databaze.getUzivatel().isPrihlasen();
        };

        Runnable hidePozadavek = prihlaseniStage::hide;

        prihlaseniStage.setScene(new Prihlaseni(prihlas, hidePozadavek).getScene());
        prihlaseniStage.show();
    }

    /**
     * Zobrazí okno pro úpravu informací o podniku.
     * @param podnik podnik, který chce uživatel upravit
     */
    private void zobrazUpraveniPodniku(Podnik podnik) {
        zobrazOkno(new UpravitPodnikObrazovka(podnik).getScene());
    }

    /*
     * ========================================================================
     */

    public HlavniObrazovka() {
        super(new BorderPane(), 700, 700, "background");

        try {
            this.databaze = Databaze.get(Uzivatel.guest());
        } catch (PraguePubDatabaseException e) {
            this.nepovedenePrihlaseni(e.getMessage());
        }

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

        pane.setCenter(
                new ImageView(new Image(this.getClass().getResourceAsStream("/castiMapy/Praha1.png")))
        );
    }
}
