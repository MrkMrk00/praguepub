package cz.vse.praguePub.gui.obrazovky.abstraktniObrazovky;

import cz.vse.praguePub.gui.ObrazovkyController;
import cz.vse.praguePub.gui.komponenty.AlertBuilder;
import cz.vse.praguePub.gui.komponenty.Tabulka;
import cz.vse.praguePub.logika.Databaze;
import cz.vse.praguePub.logika.dbObjekty.Pivo;
import cz.vse.praguePub.logika.dbObjekty.Podnik;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

import static cz.vse.praguePub.gui.komponenty.Komponenty.*;

public abstract class PodnikInfoObrazovka extends Obrazovka<BorderPane> {
    private static final Logger log = LoggerFactory.getLogger(PodnikInfoObrazovka.class);

    private final String horniPanelLabelText;

    protected final ObrazovkyController controller;
    protected final Databaze db;
    protected final Podnik podnik;
    protected final ObservableList<Pivo> pivaUPodniku;
    
    protected PodnikInfoObrazovka(ObrazovkyController controller, Podnik podnik, String horniPanelLabelText) {
        super(new BorderPane(), 700, 600, "background");
        this.controller = controller;
        this.db = controller.getDatabaze();
        this.podnik = podnik;
        this.pivaUPodniku = FXCollections.observableArrayList(podnik.getPivniListek());
        this.horniPanelLabelText = horniPanelLabelText;

        this.registrujInputy();
        this.vytvorGUI();
    }

    protected PodnikInfoObrazovka(ObrazovkyController controller, Integer cisloMC, String horniPanelLabelText) {
        this(controller, new Podnik(), horniPanelLabelText);
        if (cisloMC != null) this.podnik.setAdresa_mc_cislo(cisloMC);
    }

    /**
     * Metoda přidá všechny inputy do jedné mapy pro snažší přístup k vkládaným datům.
     */
    private void registrujInputy() {
        this.getMapaInputu().putAll(
                Map.of(
                        "nazev",    TextFieldAplikace("Název podniku", t -> {
                            t.setText(this.podnik.getNazev());
                            t.setOnMouseClicked(null);
                        }),
                        "mc_cislo", TextFieldAplikace("Číslo městské části", t -> {
                            t.setText(Integer.toString(this.podnik.getAdresa_mc_cislo()));
                            t.setOnMouseClicked(null);
                        }),
                        "mc_nazev", TextFieldAplikace("Název městské části", t -> {
                            t.setText(this.podnik.getAdresa_mc_nazev());
                            t.setOnMouseClicked(null);
                        }),
                        "psc",      TextFieldAplikace("Poštovní směr. číslo", t -> {
                            Integer psc = this.podnik.getAdresa_psc();
                            if (psc != null) t.setText(Integer.toString(psc));
                            t.setOnMouseClicked(null);
                        }),
                        "ulice",    TextFieldAplikace("Ulice", t -> {
                            t.setText(this.podnik.getAdresa_ulice());
                            t.setOnMouseClicked(null);
                        }),
                        "cp",       TextFieldAplikace("Číslo popisné", t -> {
                            t.setText(this.podnik.getAdresa_cp());
                            t.setOnMouseClicked(null);
                        })
                )
        );
        this.pivaUPodniku.addAll(this.podnik.getPivniListek());
    }

    private HBox inputyGUI() {
        Button pridejPivo = TlacitkoAplikace(
                "Přidej pivo",
                tlacitko -> tlacitko.setOnMouseClicked(
                        onClickEvent -> this.controller.vyberPivo(this.pivaUPodniku::add)
                )
        );

        Button odeslat = TlacitkoAplikace(
                "Odeslat",
                tlacitko -> tlacitko.setOnMouseClicked(
                        event -> this.odeslat()
                )
        );

        VBox inputy = Sloupec(
                List.of(
                        Radek( LabelAplikace("Název podniku:"), this.getMapaInputu().get("nazev") ),
                        Radek( LabelAplikace("Adresa:") ),

                        Radek(
                                //odsazení položek adresy
                                Sloupec(List.of(), sloupec -> { sloupec.setFillWidth(true); sloupec.setPrefWidth(20);}),
                                Sloupec(
                                        Radek( LabelAplikace("Číslo MČ:"),      this.getMapaInputu().get("mc_cislo")    ),
                                        Radek( LabelAplikace("Název MČ:"),      this.getMapaInputu().get("mc_nazev")    ),
                                        Radek( LabelAplikace("Ulice:"),         this.getMapaInputu().get("ulice")       ),
                                        Radek( LabelAplikace("PSČ:"),           this.getMapaInputu().get("psc")         ),
                                        Radek( LabelAplikace("Číslo popisné:"), this.getMapaInputu().get("cp")          ),
                                        Radek( pridejPivo, Spacer(), odeslat )
                                )
                        )
                ),
                sloupecRoot -> {}
        );

        return Radek( Spacer(), inputy, Spacer() );
    }

    private Tabulka<Pivo> tabulkaPivGUI() {
        Tabulka<Pivo> tabulkaPiv = new Tabulka<>(Pivo.PRO_TABULKU);
        TableView<Pivo> tv = tabulkaPiv.getTableView();
        tv.setItems(this.pivaUPodniku);
        tv.setMaxHeight(200d);

        //zobrazení výběru ceny a objemu při dvojitém kliknutí na řádek
        tv.setRowFactory(tableView -> {
            TableRow<Pivo> tableRow = new TableRow<>();
            tableRow.setOnMouseClicked(
                    mouseEvent -> {
                        Pivo vybranePivo = tableRow.getItem();
                        if (mouseEvent.getClickCount() == 2 && vybranePivo != null)
                            this.controller.zadejCenuAObjem(vybranePivo, tv::refresh);
                    }
            );
            return tableRow;
        });

        return tabulkaPiv;
    }

    private void vytvorGUI() {
        HBox horniPanel = HorniPanel(
                hp -> {
                    hp.getChildren().add(NadpisOknaLabel(this.horniPanelLabelText));
                    hp.setAlignment(Pos.CENTER);
                }
        );
        this.getPane().setTop(horniPanel);
        this.getPane().setCenter(this.inputyGUI());

        VBox tabulka = Sloupec(
                List.of(
                        LabelAplikace("Piva:"),
                        this.tabulkaPivGUI().getTableView()
                ),
                tabulkaVBox -> tabulkaVBox.setAlignment(Pos.TOP_LEFT)
        );
        this.getPane().setBottom(tabulka);
    }

    protected boolean nastavHodnoty() {
        StringBuilder chybne = new StringBuilder();

        String nazev = this.getMapaInputu().get("nazev").getText();
        if (nazev != null && !nazev.isBlank()) this.podnik.setNazev(nazev);
        else chybne.append("název, ");

        int cisloMC = NumberUtils.toInt(this.getMapaInputu().get("mc_cislo").getText(), -1);
        if (cisloMC > 0 && cisloMC < 23) this.podnik.setAdresa_mc_cislo(cisloMC);
        else chybne.append("číslo MČ, ");

        String nazevMC = this.getMapaInputu().get("mc_nazev").getText();
        if (nazevMC != null && !nazevMC.isBlank()) this.podnik.setAdresa_mc_nazev(nazevMC);
        else chybne.append("název MČ, ");

        String ulice = this.getMapaInputu().get("ulice").getText();
        if (ulice != null && !ulice.isBlank()) this.podnik.setAdresa_ulice(ulice);
        else chybne.append("ulice, ");

        int psc = NumberUtils.toInt(this.getMapaInputu().get("psc").getText());
        if (psc > 10000 && psc < 25229) this.podnik.setAdresa_psc(psc);
        else chybne.append("PSČ, ");

        String cp = this.getMapaInputu().get("cp").getText();
        if (cp != null && !cp.isBlank()) this.podnik.setAdresa_cp(cp);
        else chybne.append("číslo popisné, ");

        boolean doBreakPiva = false;
        for (Pivo pivo : this.pivaUPodniku) {
            if (pivo.getCena() == null || pivo.getCena() < 0.0) {
                chybne.append("ceny piv, ");
                doBreakPiva = true;
            }
            if (pivo.getObjem() == null || pivo.getObjem() < 0.02 || pivo.getObjem() > 5.0) {
                chybne.append("objemy piv, ");
                doBreakPiva = true;
            }

            if (doBreakPiva) break;
        }
        if (!doBreakPiva) {
            this.podnik.getPivniListek().clear();
            this.podnik.getPivniListek().addAll(this.pivaUPodniku);
        }
        log.debug("nastavovane hodnoty podniku: " + this.podnik.toString());

        if (chybne.isEmpty()) return true;

        chybne.delete(chybne.length()-2, chybne.length());
        this.zobrazChybaPriNastavovani(chybne.toString());
        return false;
    }

    private void zobrazChybaPriNastavovani(String chybne) {
        new AlertBuilder(Alert.AlertType.ERROR)
                .setTitle("Prague Pub")
                .setHeaderText("Chyba")
                .setContent(
                        "Prosím, nastav všechny hodnoty podniku správně:" + "\n"
                                + chybne
                )
                .getAlert()
                .show();
    }

    protected abstract void odeslat();
}
