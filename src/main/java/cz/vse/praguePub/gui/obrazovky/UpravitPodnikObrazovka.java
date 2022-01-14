package cz.vse.praguePub.gui.obrazovky;

import cz.vse.praguePub.gui.ObrazovkyController;
import cz.vse.praguePub.gui.komponenty.Tabulka;
import cz.vse.praguePub.gui.obrazovky.abstraktniObrazovky.Obrazovka;
import cz.vse.praguePub.logika.Databaze;
import cz.vse.praguePub.logika.dbObjekty.Pivo;
import cz.vse.praguePub.logika.dbObjekty.Podnik;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.layout.*;

import java.util.List;
import java.util.Map;

import static cz.vse.praguePub.gui.komponenty.Komponenty.*;

/**
 * Třída reprezentuje GUI přidání nového podniku do databáze.
 */
public class UpravitPodnikObrazovka extends Obrazovka<BorderPane> {

    protected final Podnik upravovanyPodnik;
    protected final ObrazovkyController controller;
    protected final Databaze db;
    protected final ObservableList<Pivo> pivaUPodniku;

    public UpravitPodnikObrazovka(ObrazovkyController controller, Podnik podnik) {
        super(new BorderPane(), 900, 600, "background");

        this.upravovanyPodnik = podnik;
        this.controller = controller;
        this.db = controller.getDatabaze();
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
                        "nazev",    TextFieldAplikace("Název podniku", t -> {
                            t.setText(this.upravovanyPodnik.getNazev());
                            t.setOnMouseClicked(null);
                        }),
                        "mc_cislo", TextFieldAplikace("Číslo městské části", t -> {
                            t.setText(Integer.toString(this.upravovanyPodnik.getAdresa_mc_cislo()));
                            t.setOnMouseClicked(null);
                        }),
                        "mc_nazev", TextFieldAplikace("Název městské části", t -> {
                            t.setText(this.upravovanyPodnik.getAdresa_mc_nazev());
                            t.setOnMouseClicked(null);
                        }),
                        "psc",      TextFieldAplikace("Poštovní směr. číslo", t -> {
                            t.setText(Integer.toString(this.upravovanyPodnik.getAdresa_psc()));
                            t.setOnMouseClicked(null);
                        }),
                        "ulice",    TextFieldAplikace("Ulice", t -> {
                            t.setText(this.upravovanyPodnik.getAdresa_ulice());
                            t.setOnMouseClicked(null);
                        }),
                        "cp",       TextFieldAplikace("Číslo popisné", t -> {
                            t.setText(this.upravovanyPodnik.getAdresa_cp());
                            t.setOnMouseClicked(null);
                        })
                )
        );
        this.pivaUPodniku.addAll(this.upravovanyPodnik.getPivniListek());
    }

    protected void odeslat() {
        this.controller.getDatabaze().upravPodnik(this.upravovanyPodnik);
    }

    protected HBox horniPanel() {
        return HorniPanel(
                horniPanel -> {
                    horniPanel.getChildren().add(NadpisOknaLabel("Upravit podnik"));
                    horniPanel.setAlignment(Pos.CENTER);
                }
        );
    }

    protected VBox inputy() {
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        return Sloupec(List.of(
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
                                TlacitkoAplikace(
                                        "+ Vlož pivo",
                                        tlacitko -> tlacitko.setOnMouseClicked(
                                                event -> this.controller.vyberPivo(this.pivaUPodniku::add)
                                        )
                                ),
                                spacer,
                                TlacitkoAplikace(
                                        "Odeslat",
                                        tlacitko -> tlacitko.setOnMouseClicked(
                                                event -> this.odeslat()
                                        )
                                )
                        )
                ),
                inputySloupec -> {
                    inputySloupec.setPadding(new Insets(20));
                    inputySloupec.setFillWidth(true);
                }
        );
    }

    protected Tabulka<Pivo> tabulka() {
        Tabulka<Pivo> tabulkaPivaUNovehoPodniku = new Tabulka<>(Pivo.PRO_TABULKU);
        TableView<Pivo> tv = tabulkaPivaUNovehoPodniku.getTableView();
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

        return tabulkaPivaUNovehoPodniku;
    }

    protected VBox spodek() {
        return Sloupec(
                List.of(
                        LabelAplikace("Piva:"),
                        this.tabulka().getTableView()
                ),
                sloupec -> {
                    sloupec.setAlignment(Pos.TOP_LEFT);
                }
        );
    }

    private void vytvorGUI() {
        this.getPane().setTop(this.horniPanel());

        this.getPane().setCenter(
                Radek(
                        List.of(this.inputy()),
                        radek -> {
                            radek.widthProperty().add(this.getPane().widthProperty());
                            radek.setAlignment(Pos.CENTER);
                        }
                )
        );
        this.getPane().setBottom(this.spodek());
    }
}
