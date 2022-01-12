package cz.vse.praguePub.gui.obrazovky;

import cz.vse.praguePub.gui.ObrazovkyController;
import cz.vse.praguePub.gui.komponenty.Tabulka;
import cz.vse.praguePub.gui.obrazovky.abstraktniObrazovky.Obrazovka;
import cz.vse.praguePub.logika.Databaze;
import cz.vse.praguePub.logika.dbObjekty.Pivo;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

import java.util.List;
import java.util.Map;

import static cz.vse.praguePub.gui.komponenty.Komponenty.*;

/**
 * Třída reprezentuje GUI přidání nového podniku do databáze.
 */
public class PridejPodnikObrazovka extends Obrazovka<BorderPane> {

    private final ObservableList<Pivo> pivaUPodniku;
    private final Databaze db;
    private final ObrazovkyController controller;

    public PridejPodnikObrazovka(ObrazovkyController controller) {
        super(new BorderPane(), 900, 600, "background");

        this.db = controller.getDatabaze();
        this.controller = controller;
        this.pivaUPodniku = FXCollections.observableArrayList();

        this.registrujInputy();
        this.vytvorGUI();
    }

    /**
     * Metoda přidá všechny inputy do jedné mapy pro snažší přístup k vkládaným datům.
     */
    private void registrujInputy() {
        this.getMapaInputu().putAll(
                Map.of(
                        "nazev",    TextFieldAplikace("Název podniku", t -> {}),
                        "mc_cislo", TextFieldAplikace("Číslo městské části", t -> {}),
                        "mc_nazev", TextFieldAplikace("Název městské části", t -> {}),
                        "psc",      TextFieldAplikace("Poštovní směr. číslo", t -> {}),
                        "ulice",    TextFieldAplikace("Ulice", t -> {}),
                        "cp",       TextFieldAplikace("Číslo popisné", t -> {})
                )
        );
    }

    private void vytvorGUI() {
        this.getPane().setTop(
                HorniPanel(
                    horniPanel -> {
                        horniPanel.getChildren().add(NadpisOknaLabel("Přidat nový podnik"));
                        horniPanel.setAlignment(Pos.CENTER);
                    }
                )
        );

        VBox inputy = Sloupec(List.of(
                Radek(LabelAplikace("Název podniku:"), this.getMapaInputu().get("nazev")),
                Radek(LabelAplikace("Adresa:")),
                Radek(
                        Sloupec(List.of(), sloupec -> { sloupec.setFillWidth(true); sloupec.setPrefWidth(20);}),
                        Sloupec(
                                Radek(LabelAplikace("Číslo MČ:"), this.getMapaInputu().get("mc_cislo")),
                                Radek(LabelAplikace("Název MČ:"), this.getMapaInputu().get("mc_nazev")),
                                Radek(LabelAplikace("Ulice:"), this.getMapaInputu().get("ulice")),
                                Radek(LabelAplikace("PSČ:"), this.getMapaInputu().get("psc")),
                                Radek(LabelAplikace("Číslo popisné:"), this.getMapaInputu().get("cp"))
                        )
                ),
                Radek(
                        TlacitkoAplikace("+ Vlož pivo", tlacitko -> tlacitko.setOnMouseClicked(event -> this.vyberPivo()))
                )
                ),
                inputySloupec -> {
                    inputySloupec.setPadding(new Insets(20));
                    inputySloupec.setFillWidth(true);
                }
        );
        this.getPane().setCenter(
                Radek(List.of(inputy), radek -> {
                    radek.widthProperty().add(this.getPane().widthProperty());
                    radek.setAlignment(Pos.CENTER);
        }));

        Tabulka<Pivo> tabulkaPivaUNovehoPodniku = new Tabulka<>(Pivo.PRO_TABULKU);


        tabulkaPivaUNovehoPodniku.getTableView().setItems(this.pivaUPodniku);

        this.getPane().setBottom(
                Sloupec(
                        List.of(
                                LabelAplikace("Piva:"),
                                tabulkaPivaUNovehoPodniku.getTableView()
                        ),
                        sloupec -> {
                            sloupec.setAlignment(Pos.TOP_LEFT);
                        }
                )
        );
    }

    /**
     * Metoda zobrazí dialog pro vybrání piva z databáze, které se do nového podniku přidá.
     */
    private void vyberPivo() {

    }
}
