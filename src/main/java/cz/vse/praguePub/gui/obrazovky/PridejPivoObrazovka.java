package cz.vse.praguePub.gui.obrazovky;

import cz.vse.praguePub.gui.ObrazovkyController;
import cz.vse.praguePub.gui.komponenty.AlertBuilder;
import cz.vse.praguePub.gui.obrazovky.abstraktniObrazovky.Obrazovka;
import cz.vse.praguePub.logika.Vysledek;
import cz.vse.praguePub.logika.dbObjekty.Pivo;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import org.apache.commons.lang3.math.NumberUtils;

import java.util.Map;
import java.util.Optional;

import static cz.vse.praguePub.gui.komponenty.AlertBuilder.*;
import static cz.vse.praguePub.gui.komponenty.Komponenty.*;

public class PridejPivoObrazovka extends Obrazovka<BorderPane> {

    private final ObrazovkyController controller;
    private final Runnable zpetCallback;
    private final Runnable odeslatCallback;
    private final Pivo pivo;

    public PridejPivoObrazovka(ObrazovkyController controller, Runnable odeslatCallback, Runnable zpetCallback) {
        super(new BorderPane(), 600, 600, "background");
        this.controller = controller;
        this.zpetCallback = zpetCallback;
        this.odeslatCallback = odeslatCallback;
        this.pivo = new Pivo();

        this.registrujInputy();
        this.vytvorGUI();
    }

    public PridejPivoObrazovka(ObrazovkyController controller, Runnable zpetCallback) {
        this(controller, null, zpetCallback);
    }

    private void registrujInputy() {
        this.getMapaInputu().putAll(
                Map.of(
                        "nazev",            TextFieldAplikace("Název", null),
                        "pivovar",          TextFieldAplikace("Pivovar", null),
                        "stupnovitost",     TextFieldAplikace("Stupňovitost", null),
                        "obsah_alkoholu",   TextFieldAplikace("Obsah alk.", null),
                        "typ",              TextFieldAplikace("Typ", null),
                        "typ_kvaseni",      TextFieldAplikace("Typ kvašení", null)
                )
        );
    }

    private void zobrazChybaPriNastavovani(String chybne) {
        new AlertBuilder(Alert.AlertType.ERROR)
                .setTitle("Prague Pub")
                .setHeaderText("Chyba")
                .setContent(
                        "Prosím, nastav všechny hodnoty piva správně:" + "\n"
                                + chybne
                )
                .getAlert()
                .show();
    }

    private void zobrazOpravduPokracovat(Vysledek<Pivo> vysledek) {
        Optional<ButtonType> alertResultOpt = OpravduPokracovatAlert(vysledek).showAndWait();

        if (alertResultOpt.isEmpty() || alertResultOpt.get() == ButtonType.CANCEL) return;
        if (alertResultOpt.get() == ButtonType.OK) vysledek.pokracuj();
    }

    private boolean nastavHodnoty() {
        StringBuilder chybne = new StringBuilder();

        String nazev = this.getMapaInputu().get("nazev").getText();
        if (nazev != null && !nazev.isBlank()) this.pivo.setNazev(nazev);
        else chybne.append("název, ");

        String pivovar = this.getMapaInputu().get("pivovar").getText();
        if (pivovar != null && !pivovar.isBlank()) this.pivo.setNazevPivovaru(pivovar);
        else chybne.append("pivovar, ");

        double stupnovitost = NumberUtils.toDouble(this.getMapaInputu().get("stupnovitost").getText(), -1);
        if (stupnovitost > 5.0 && stupnovitost < 30.0) this.pivo.setStupnovitost(stupnovitost);
        else chybne.append("stupnovitost, ");

        double obsah_alkoholu = NumberUtils.toDouble(this.getMapaInputu().get("obsah_alkoholu").getText(), -1);
        if (obsah_alkoholu >= 0 && obsah_alkoholu < 25.0) this.pivo.setObsahAlkoholu(obsah_alkoholu);
        else chybne.append("obsah_alkoholu, ");

        String typ = this.getMapaInputu().get("typ").getText();
        String typFinal = nazev != null && !nazev.isBlank() ? typ : "";
        this.pivo.setTyp(typFinal);

        String typ_kvaseni = this.getMapaInputu().get("typ_kvaseni").getText();
        String typKvaseniFinal = nazev != null && !nazev.isBlank() ? typ_kvaseni : "";
        this.pivo.setTypKvaseni(typKvaseniFinal);

        if (chybne.toString().isBlank()) return true;

        this.zobrazChybaPriNastavovani(chybne.delete(chybne.length()-2, chybne.length()).toString());
        return false;
    }

    private void odeslat() {
        if (!this.nastavHodnoty()) return;
        Vysledek<Pivo> vysledek = this.controller.getDatabaze().vytvorNovePivo(this.pivo);
        if (this.odeslatCallback != null) this.odeslatCallback.run();

        switch (vysledek.getTypVysledku()) {
            case DB_CHYBA -> ChybaDatabazeAlert(vysledek.getZprava()).show();
            case OK -> UspesneVlozenObjekt(vysledek.getDotazovany()).show();
            case STEJNY_NAZEV -> this.zobrazOpravduPokracovat(vysledek);
        }
    }

    private void vytvorGUI() {
        HBox horniPanel = HorniPanel(
                hp -> {
                    hp.getChildren().addAll(
                            NadpisOknaLabel("Vytvoř nové pivo"),
                            Spacer(),
                            TlacitkoZpet(mouseEvent -> this.zpetCallback.run(), t -> {})
                    );
                }
        );
        this.getPane().setTop(horniPanel);

        HBox inputy = Radek(
                Spacer(),
                Sloupec(
                        Radek( LabelAplikace("Název:"),         this.getMapaInputu().get("nazev") ),
                        Radek( LabelAplikace("Pivovar:"),       this.getMapaInputu().get("pivovar") ),
                        Radek( LabelAplikace("Stupňovitost:"),  this.getMapaInputu().get("stupnovitost") ),
                        Radek( LabelAplikace("Obsah alk.:"),    this.getMapaInputu().get("obsah_alkoholu") ),
                        Radek( LabelAplikace("Typ:"),           this.getMapaInputu().get("typ") ),
                        Radek( LabelAplikace("Typ kvašení:"),   this.getMapaInputu().get("typ_kvaseni") ),

                        Radek( Spacer(), TlacitkoAplikace("Odeslat", onClickEvent -> this.odeslat(), t -> t.setPrefWidth(150d)), Spacer())
                ),
                Spacer()
        );
        this.getPane().setCenter(inputy);
    }
}
