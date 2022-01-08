package cz.vse.praguePub.gui;

import cz.vse.praguePub.gui.komponenty.Tabulka;
import cz.vse.praguePub.logika.Databaze;
import cz.vse.praguePub.logika.Uzivatel;
import cz.vse.praguePub.logika.dbObjekty.Pivo;
import cz.vse.praguePub.logika.dbObjekty.Podnik;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.function.Consumer;

import static cz.vse.praguePub.gui.komponenty.Komponenty.*;

/**
 * Třída reprezentuje GUI přidání nového podniku do databáze.
 */
public class PridejPodnikObrazovka extends Obrazovka<BorderPane> {
    private final static Logger log = LoggerFactory.getLogger(PridejPodnikObrazovka.class);

    private final ObservableList<Pivo> pivaUPodniku;
    private final Stage oknoProVyberPiva;
    private final Databaze db;

    public PridejPodnikObrazovka(Databaze db) {
        super(new BorderPane(), 900, 600, "background");

        this.oknoProVyberPiva = new Stage();
        this.db = db;
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


    private void parsuj() {
        log.debug(new Podnik(
                null,
                this.getMapaInputu().get("nazev").getText(),
                Integer.parseInt(this.getMapaInputu().get("mc_cislo").getText()),
                this.getMapaInputu().get("mc_nazev").getText(),
                this.getMapaInputu().get("ulice").getText(),
                Integer.parseInt(this.getMapaInputu().get("psc").getText()),
                this.getMapaInputu().get("cp").getText(),
                new ArrayList<>(),
                new ArrayList<>()
        ).toString());
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
                Radek(TlacitkoAplikace("Parsuj", t -> {
                    t.setOnAction(event -> this.parsuj());
                })),
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
        Consumer<Pivo> callbackPoVyberuPiva = pivo -> {
            this.pivaUPodniku.add(pivo);
            this.oknoProVyberPiva.hide();
        };

        this.oknoProVyberPiva.setScene(
                new VyberPivoDialog(
                        this.db.getPivaCollection(),
                        callbackPoVyberuPiva
                ).getScene()
        );
        this.oknoProVyberPiva.showAndWait();
    }
}
