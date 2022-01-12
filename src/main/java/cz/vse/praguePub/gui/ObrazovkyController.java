package cz.vse.praguePub.gui;

import com.mongodb.MongoException;
import cz.vse.praguePub.gui.komponenty.AlertBuilder;
import cz.vse.praguePub.gui.obrazovky.*;
import cz.vse.praguePub.logika.Databaze;
import cz.vse.praguePub.logika.Uzivatel;
import cz.vse.praguePub.logika.dbObjekty.Podnik;
import cz.vse.praguePub.util.PraguePubDatabaseException;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import lombok.Getter;

import java.util.function.BiFunction;

import static cz.vse.praguePub.gui.komponenty.Komponenty.zobrazOkno;

public class ObrazovkyController {

    @Getter private Databaze databaze = null;

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
        HlavniObrazovka hlObr = new HlavniObrazovka(this);
        Scene hlScene = hlObr.getScene();

        primaryStage.setScene(hlScene);
        primaryStage.setMinWidth(889);
        primaryStage.setMinHeight(817);
        primaryStage.show();
    }

    /**
     * Metoda otevírá okno s oblíbenými podniky.<p>
     * () -> List&lt;Podnik&gt; &emsp; <b>ziskejOblibenePodniky</b>: dotaz do databáze - vrací podniky z databáze <br>
     * (Podnik) -> void&emsp;&emsp; <b>odeberPodnik</b>: dotaz do databáze - odebírá podnik z oblíbených podniků uživatele <br>
     * (Podnik) -> void&emsp;&emsp; <b>upravPodnik</b>: dotaz do databáze - otevře okno úpravy podniku <br>
     */
    public void zobrazOblibenePodniky() {
        Uzivatel instanceUzivatele = this.databaze.getUzivatel();
        if (instanceUzivatele.isGuest() || !instanceUzivatele.isPrihlasen()) {
            this.zobrazPrihlaseni();
            return;
        }
        zobrazOkno(new OblibenePodnikyObrazovka(this).getScene());
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

        prihlaseniStage.setScene(new Prihlaseni(prihlas, hidePozadavek,this).getScene());
        prihlaseniStage.show();
    }

    /**
     * Zobrazí okno pro úpravu informací o podniku.
     * @param podnik podnik, který chce uživatel upravit
     */
    public void zobrazUpraveniPodniku(Podnik podnik) {
        zobrazOkno(new UpravitPodnikObrazovka(podnik).getScene());
    }

    public void zobrazPodnikyVOblasti(final int cisloMeskeCasti) {
        zobrazOkno(new PodnikyVMestskeCastiObrazovka(cisloMeskeCasti, this).getScene());
    }

    public void zobrazVyhledatPodleNazvu(String defaultniDotaz) {
        zobrazOkno(new VyhledaniPodleJmena(this, defaultniDotaz).getScene());
    }

    public void zobrazPridejNovyPodnik() {
        zobrazOkno(new PridejPodnikObrazovka(this.databaze).getScene());
    }

    public void filtruj() {

    }

    public void zobrazVyhledatPodleNazvu() {
        zobrazOkno(new VyhledaniPodleJmena(this).getScene());
    }

    public void zobrazVytvoreniUcet(){
        WebView webView = new WebView();
        WebEngine webEngine = webView.getEngine();
        webEngine.load("http://localhost:3021/");
        zobrazOkno(new Scene(webView));
    }

}
