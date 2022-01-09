package cz.vse.praguePub.gui;

import com.mongodb.MongoException;
import cz.vse.praguePub.gui.komponenty.AlertBuilder;
import cz.vse.praguePub.gui.obrazovky.HlavniObrazovka;
import cz.vse.praguePub.gui.obrazovky.OblibenePodniky;
import cz.vse.praguePub.gui.obrazovky.Prihlaseni;
import cz.vse.praguePub.gui.obrazovky.UpravitPodnikObrazovka;
import cz.vse.praguePub.logika.Databaze;
import cz.vse.praguePub.logika.Uzivatel;
import cz.vse.praguePub.logika.dbObjekty.Podnik;
import cz.vse.praguePub.util.PraguePubDatabaseException;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Supplier;

import static cz.vse.praguePub.gui.komponenty.Komponenty.zobrazOkno;

public class ObrazovkyController {

    private Databaze databaze = null;

    public ObrazovkyController() {
        this.prihlasHosta();
    }

    /**
     * Přihlásí hosta a inicializuje databázi
     */
    private void prihlasHosta() {
        try {
            this.databaze = Databaze.get(Uzivatel.guest());
        } catch (PraguePubDatabaseException e) {
            this.nepovedenePrihlaseni(e.getMessage());
        }
    }

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

    public void zapniAplikaci(Stage primaryStage) {
        primaryStage.setScene(new HlavniObrazovka(this).getScene());
        primaryStage.show();
    }

    /**
     * Metoda otevírá okno s oblíbenými podniky.<p>
     * () -> List&lt;Podnik&gt; &emsp; <b>ziskejOblibenePodniky</b>: dotaz do databáze - vrací podniky z databáze <br>
     * (Podnik) -> void&emsp;&emsp; <b>odeberPodnik</b>: dotaz do databáze - odebírá podnik z oblíbených podniků uživatele <br>
     * (Podnik) -> void&emsp;&emsp; <b>upravPodnik</b>: dotaz do databáze - otevře okno úpravy podniku <br>
     */
    public void zobrazOblibenePodniky() {
        Supplier<List<Podnik>> ziskejOblibenePodniky = () -> this.databaze.getOblibenePodniky();
        Consumer<Podnik> odeberPodnik = podnik -> this.databaze.odeberZOblibenych(podnik);
        Consumer<Podnik> upravPodnik = this::zobrazUpraveniPodniku;

        Uzivatel instanceUzivatele = this.databaze.getUzivatel();
        if (instanceUzivatele.isGuest() || !instanceUzivatele.isPrihlasen()) {
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
    public void zobrazPrihlaseni() {
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
    public void zobrazUpraveniPodniku(Podnik podnik) {
        zobrazOkno(new UpravitPodnikObrazovka(podnik).getScene());
    }

}
