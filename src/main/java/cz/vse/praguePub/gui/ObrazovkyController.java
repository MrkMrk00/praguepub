package cz.vse.praguePub.gui;

import com.mongodb.MongoException;
import cz.vse.praguePub.gui.komponenty.AlertBuilder;
import cz.vse.praguePub.gui.obrazovky.*;
import cz.vse.praguePub.logika.Databaze;
import cz.vse.praguePub.logika.Uzivatel;
import cz.vse.praguePub.logika.dbObjekty.Pivo;
import cz.vse.praguePub.logika.dbObjekty.Podnik;
import cz.vse.praguePub.util.PraguePubDatabaseException;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.util.function.BiFunction;
import java.util.Map;
import java.util.function.Consumer;


import static cz.vse.praguePub.gui.komponenty.Komponenty.Ikona;
import static cz.vse.praguePub.gui.komponenty.Komponenty.zobrazOkno;

/**
 * Třída, obsahující logiku oken, jejich propojování a zobrazování.
 */
public class ObrazovkyController {
    private static final Logger log = LoggerFactory.getLogger(ObrazovkyController.class);
    private static final String URL_REGISTRACNIHO_SERVERU = "http://localhost:3021/";

    @Getter @Setter private Databaze databaze = null;

    /**
     * Konstruktor přihlásí hosta do databáze a vytvoří instanci databáze
     * (pokud se vytvoření nezdaří, tak vyhodí chybu)
     */
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
        log.error(errorText);
        new AlertBuilder(Alert.AlertType.ERROR)
                .setTitle("PraguePub")
                .setHeaderText("Chyba databáze")
                .setContent(errorText)
                .getAlert()
                .showAndWait();

        System.exit(0);
    }

    public boolean jeUzivatelPrihlasen() {
        boolean vysl = this.getDatabaze().getUzivatel() != null
                && this.getDatabaze().getUzivatel().isPrihlasen()
                && !this.getDatabaze().getUzivatel().isGuest();
        if (!vysl) this.zobrazPrihlaseni();
        return vysl;
    }

    /**
     * Zapne aplikaci - zobrazí hlavní okno
     * @param primaryStage stage, do které se má hlavní obrazovka zobrazit
     */
    public void zapniAplikaci(Stage primaryStage) {
        HlavniObrazovka hlObr = new HlavniObrazovka(this);
        Scene hlScene = hlObr.getScene();

        Image ikona = Ikona();
        if (ikona != null) primaryStage.getIcons().add(ikona);

        primaryStage.setOnCloseRequest(event -> System.exit(0));
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
        Stage stage = new Stage();
        stage.getIcons().add(Ikona());
        stage.setScene(new OblibenePodnikyObrazovka(this, stage).getScene());
        stage.show();
    }

    /**
     * Zobrazí okno s přihlášením.<p>
     * (String, String) -> Boolean &emsp; <b>prihlas</b>: metoda se pokusí vytvořít nového uživatele
     * a přiřadit instanční proměnné databaze novou instanci s přihlášeným uživatelem.<br>
     * () -> void &emsp; <b>hidePozadavek</b>: slouží k zavření okna s přihlášením. Když se
     * uživateli povede úspěšně přihlásit, tak se okno automaticky zavře.
     */
    public void zobrazPrihlaseni() {
        Stage prihlaseniStage = new Stage();

        BiFunction<String, String, Boolean> prihlas = (jmeno, heslo) -> {
            Uzivatel novyUzivatel;
            try {
                novyUzivatel = new Uzivatel(jmeno, heslo);
                this.databaze = Databaze.get(novyUzivatel);
            } catch (MongoException | PraguePubDatabaseException e) {
                return false;
            }

            boolean vysledekPrihlaseni = this.databaze.getUzivatel().isPrihlasen();
            if (!vysledekPrihlaseni) this.prihlasHosta();
            return vysledekPrihlaseni;
        };

        Runnable hidePozadavek = prihlaseniStage::hide;
        prihlaseniStage.setAlwaysOnTop(true);
        prihlaseniStage.setResizable(false);

        prihlaseniStage.getIcons().add(Ikona());

        prihlaseniStage.setScene(new PrihlaseniDialog(prihlas, hidePozadavek,this).getScene());
        prihlaseniStage.showAndWait();
    }

    /**
     * Zobrazí okno pro úpravu informací o podniku.
     * @param podnik podnik, který chce uživatel upravit
     */
    public void zobrazUpraveniPodniku(Podnik podnik) {
        zobrazOkno(new UpravitPodnikObrazovka(this, podnik).getScene());
    }

    /**
     * Zobrazí okno pro úpravu informací o podniku.
     * @param podnik podnik, který chce uživatel upravit
     * @param st stage, do které se má upravení podniku zobrazit
     * @param scene scéna, která se má zobrazit po stisknutí tlačítka zpět
     */
    public void zobrazUpraveniPodniku(Podnik podnik, Stage st, Scene scene) {
        if(databaze.getUzivatel().isGuest()){
            this.zobrazPrihlaseni();
            return;
        }
        st.getIcons().add(Ikona());
        st.setScene(new UpravitPodnikObrazovka(this, podnik, () -> st.setScene(scene)).getScene());
        st.show();
    }

    /**
     * Zobrazí okno s podniky, která jsou v dané městské části
     * @param cisloMeskeCasti číslo městské části
     */
    public void zobrazPodnikyVOblasti(final int cisloMeskeCasti) {
        Stage st = new Stage();
        st.getIcons().add(Ikona());
        st.setScene(new PodnikyVMestskeCastiObrazovka(
                this,
                st,
                cisloMeskeCasti
        ).getScene());
        st.show();
    }

    /**
     * Zobrazí okno s podniky, které byly vyhledány podle názvu
     * @param defaultniDotaz název podniku dotazovaný z hlavního okna aplikace
     */
    public void zobrazVyhledatPodleNazvu(String defaultniDotaz) {
        zobrazOkno(new VyhledaniPodleJmena(
                this,
                defaultniDotaz == null ? "" : defaultniDotaz
        ).getScene());
    }

    /**
     * Zobrazí okno pro přidávání nového podniku do databáze
     */
    public void zobrazPridejNovyPodnik(Integer mestskaCast) {
        zobrazOkno(new PridejPodnikObrazovka(this, mestskaCast).getScene());
    }

    /**
     * Zobrazí okno s filtrem
     * @param atributy atributy, podle kterých se dá objekt vyhledávat
     *                 (jiné u {@link FiltrDialog#FILTR_PODNIKY podniku} a u
     *                 {@link FiltrDialog#FILTR_PIVA piva}, ...)
     * @param callback funkce, která se zavolá po žádosti uživatele na filtrování (tlačítko Odeslat nebo Enter)
     */
    public void zobrazFiltr(Map<String, FiltrDialog.AtributFilteru> atributy, Consumer<Map<String, String>> callback) {
        Stage oknoFiltru = zobrazOkno(new FiltrDialog(atributy, callback).getScene());
        oknoFiltru.setAlwaysOnTop(true);
        oknoFiltru.setResizable(false);
    }

    /**
     * Zobrazí WebView s adresou registračního serveru
     */
    public void zobrazVytvoreniUctu() {
        WebView webView = new WebView();
        WebEngine webEngine = webView.getEngine();
        webEngine.load(URL_REGISTRACNIHO_SERVERU);
        zobrazOkno(new Scene(webView));
    }

    /**
     * Zobrazí okno, které umožňuje uživatli přidat novou recenzi k příslušnému podniku
     * @param podnik podnik, který chce uživatel upravit
     * @param callback funkce, která se zavolá po žádosti uživatele po přidání recenze (tlačítko Odeslat)
     */

    public void zobrazPridejNovouRecenzi(Podnik podnik, Runnable callback) {
        if(databaze.getUzivatel().isGuest()){
            this.zobrazPrihlaseni();
            return;
        }
        Stage st = new Stage();
        st.getIcons().add(Ikona());
        Runnable callbackWrapper = () -> {
            st.hide();
            callback.run();
        };
        st.setScene(new PridaniRecenzce(this, podnik, callbackWrapper).getScene());
        st.show();
    }

    /**
     * Zobrazí okno s tabulkou piv, které je možné následně přidat k příslušnému podniku
     * @param callback funkce, která po kliknutí na příslušné pivo umožní pivo přidat k danému podniku
     */


    public void vyberPivo(Consumer<Pivo> callback) {
        Stage st = new Stage();
        st.getIcons().add(Ikona());
        st.setScene(new VyberPivoDialog(this, st, callback).getScene());
        st.show();
    }

    /**
     * Zobrazí okno podniku s informacemi o daném podniku včetně podrobností.
     * @param podnik podnik, který chce uživatel upravit
     */

    public void zobrazInformaceOPodniku(Podnik podnik) {
        Stage st = new Stage();
        st.getIcons().add(Ikona());
        st.setScene(new ZobrazitPodnikObrazovka(this, podnik, st, st::hide).getScene());
        st.show();
    }

    /**
     * Zobrazí okno podniku s informacemi o daném podniku včetně podrobností.
     * @param podnik podnik, který chce uživatel upravit
     * @param stage stage, o které uživatel dozví podrobnosti
     * @param predchoziScena scéna, která se má zobrazit po stisknutí tlačítka zpět
     */

    public void zobrazInformaceOPodniku(Podnik podnik, Stage stage, Scene predchoziScena) {
        Runnable zpet = () -> stage.setScene(predchoziScena);

        stage.setScene(new ZobrazitPodnikObrazovka(this, podnik, stage, zpet).getScene());
    }

    /**
     * Zobrazí okno podniku s informacemi o daném podniku včetně podrobností.
     * @param pivo pivo, u kterého chceme upravit či doplnit cenu a objem
     * @param callback funkce, která se zavolá po žádosti uživatele na změnu ceny a objemu (Enter)
     */

    public void zadejCenuAObjem(Pivo pivo, Runnable callback) {
        Stage st = new Stage();

        Runnable callbackWrapper = () -> {
            callback.run();
            st.hide();
        };

        st.getIcons().add(Ikona());
        st.setResizable(false);
        st.setScene(new ZadejCenuAObjem(pivo, callbackWrapper).getScene());
        st.show();
    }
    /**
     * Zobrazí okno, které umožňuje uživatli přidat novou recenzi k příslušnému podniku
     * @param stage stage, do které se má upravení podniku zobrazit
     * @param scene scéna, která se má zobrazit po stisknutí tlačítka zpět
     * @param odeslatCallback funkce, která se zavolá po žádosti uživatele tlačítkem zpět při vložení nového pivo do databáze
     */

    public void vytvorNovePivo(Stage stage, Scene scene, Runnable odeslatCallback) {
        if(databaze.getUzivatel().isGuest()){
            this.zobrazPrihlaseni();
            return;
        }
        Runnable zpet = () -> stage.setScene(scene);

        stage.setScene(new PridejPivoObrazovka(this, odeslatCallback, zpet).getScene());
    }

    /**
     * Zobrazí okno, které umožňuje uživatli prohlédnout ceník piv v příslušném podniku.
     * @param podnik podnik, který chce uživatel upravit
     * @param callback funkce, která se zavolá po žádosti uživatele na stisk zpětné šipky, vrátí o obrazovku zpět
     */

    public void ukazCenik(Podnik podnik, Runnable callback){
        Stage st = new Stage();
        st.getIcons().add(Ikona());
        Runnable callbackWrapper = () -> {
            st.hide();
            callback.run();
        };
        st.setScene(new Cenik(this,podnik,callbackWrapper).getScene());
        st.show();

    };
}
