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
public class UpravitPodnikObrazovka extends Obrazovka<BorderPane> {

    private final Podnik upravovanyPodnik;
    private final ObservableList<Pivo> pivaUPodniku;
    private final Stage oknoProVyberPiva;

    public UpravitPodnikObrazovka(Podnik podnik) {
        super(new BorderPane(), 900, 600, "background");

        this.upravovanyPodnik = podnik;
        this.oknoProVyberPiva = new Stage();
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
                            horniPanel.getChildren().add(NadpisOknaLabel("Upravit podnik"));
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
        Consumer<Pivo> callbackPoVyberuPiva = pivo -> {
            this.pivaUPodniku.add(pivo);
            this.oknoProVyberPiva.hide();
        };

        this.oknoProVyberPiva.setScene(
                new VyberPivoDialog(
                        Databaze.get(Uzivatel.guest()).getPivaCollection(),
                        callbackPoVyberuPiva
                ).getScene()
        );
        this.oknoProVyberPiva.showAndWait();
    }
}
