package cz.vse.praguePub.gui.obrazovky.abstraktniObrazovky;

import cz.vse.praguePub.gui.ObrazovkyController;
import cz.vse.praguePub.gui.komponenty.Tabulka;
import cz.vse.praguePub.logika.Databaze;
import cz.vse.praguePub.logika.dbObjekty.Pivo;
import cz.vse.praguePub.logika.dbObjekty.Podnik;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.layout.*;
import lombok.AccessLevel;
import lombok.Getter;

import java.util.List;
import java.util.Map;

import static cz.vse.praguePub.gui.komponenty.Komponenty.*;

public abstract class PodnikInfoObrazovka extends Obrazovka<BorderPane> {

    private final String horniPanelLabelText;

    @Getter(AccessLevel.PROTECTED) private final ObrazovkyController controller;
    @Getter(AccessLevel.PROTECTED) private final Databaze db;
    @Getter(AccessLevel.PROTECTED) private final Podnik podnik;
    @Getter(AccessLevel.PROTECTED) private final ObservableList<Pivo> pivaUPodniku;
    
    protected PodnikInfoObrazovka(ObrazovkyController controller, Podnik podnik, String horniPanelLabelText) {
        super(new BorderPane(), 700, 700, "background");
        this.controller = controller;
        this.db = controller.getDatabaze();
        this.podnik = podnik;
        this.pivaUPodniku = FXCollections.observableArrayList(podnik.getPivniListek());
        this.horniPanelLabelText = horniPanelLabelText;

        this.registrujInputy();
        this.vytvorGUI();
    }

    protected PodnikInfoObrazovka(ObrazovkyController controller, int cisloMC, String horniPanelLabelText) {
        this(controller, new Podnik(), horniPanelLabelText);
        this.podnik.setAdresa_mc_cislo(cisloMC);
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
                            t.setText(Integer.toString(this.podnik.getAdresa_psc()));
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

    private VBox inputyGUI() {
        Button vlozPivo = TlacitkoAplikace(
                "+ Vlož pivo",
                tlacitko -> tlacitko.setOnMouseClicked(
                        onClickEvent -> this.controller.vyberPivo(this.pivaUPodniku::add)
                )
        );

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button odeslat = TlacitkoAplikace(
                "Odeslat",
                tlacitko -> tlacitko.setOnMouseClicked(
                        event -> this.odeslat()
                )
        );

        return Sloupec(
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
                                        Radek( LabelAplikace("Číslo popisné:"), this.getMapaInputu().get("cp")          )
                                ),

                                Radek( vlozPivo, spacer, odeslat )
                        )
                ),
                sloupecRoot -> {}
        );
    }

    private Tabulka<Pivo> tabulkaPivGUI() {
        Tabulka<Pivo> tabulkaPiv = new Tabulka<>(Pivo.PRO_TABULKU);
        TableView<Pivo> tv = tabulkaPiv.getTableView();
        tv.setItems(this.pivaUPodniku);

        //zobrazení výběru ceny a objemu při dvojitém kliknutí na řádek
        tv.setRowFactory(tableView -> {
            TableRow<Pivo> tableRow = new TableRow<>();
            tableRow.setOnMouseClicked(
                    mouseEvent -> {
                        Pivo vybranePivo = tableRow.getItem();
                        if (mouseEvent.getClickCount() == 2 && vybranePivo != null) this.controller.zadejCenuAObjem(vybranePivo);
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
                tabulkaVBox -> tabulkaVBox.setAlignment(Pos.BASELINE_LEFT)
        );
        this.getPane().setBottom(tabulka);
    }

    protected abstract void odeslat();
}
