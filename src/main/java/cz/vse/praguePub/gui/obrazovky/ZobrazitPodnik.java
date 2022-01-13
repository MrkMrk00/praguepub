package cz.vse.praguePub.gui.obrazovky;

import cz.vse.praguePub.gui.ObrazovkyController;
import cz.vse.praguePub.gui.komponenty.Tabulka;
import cz.vse.praguePub.gui.obrazovky.abstraktniObrazovky.Obrazovka;
import cz.vse.praguePub.logika.dbObjekty.Podnik;
import cz.vse.praguePub.logika.dbObjekty.Recenze;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.List;

import static cz.vse.praguePub.gui.komponenty.Komponenty.*;

public class ZobrazitPodnik extends Obrazovka<BorderPane> {
    private final ObrazovkyController controller;
    private final Podnik zobrazovanyPodnik;
    private final ObservableList<Recenze> seznamRecenzi;

    public ZobrazitPodnik(ObrazovkyController controller, Podnik podnik) {
        super(new BorderPane(), 700, 700, "background");

        this.seznamRecenzi = FXCollections.observableArrayList();
        this.zobrazovanyPodnik = podnik;
        this.controller = controller;
        this.prenactiTabulku();

        this.vytvorGUI();
    }

    private void vytvorGUI() {
        this.getPane().setTop(
                HorniPanel(hp -> {
                            BorderPane vrchniBar = new BorderPane();
                            vrchniBar.setLeft(
                                    Radek(
                                            NadpisOknaLabel("Hostinec V Zátiší")

                                            //nějakym stylem udělat hvězdičky recenze
                                    )
                            );

                            vrchniBar.setRight(
                                    Radek(
                                            //tlačítko ve tvaru srdíčka pro přidaní do obl.
                                            TlacitkoAplikace(
                                                    "Upravit",
                                                    event -> this.controller.zobrazUpraveniPodniku(this.zobrazovanyPodnik),
                                                    t -> HBox.setMargin(t, new Insets(6,8,0,5))),
                                            TlacitkoZpet((t) ->{})
                                            //tlačítko pro zpět


                                    )
                            );

                            hp.getChildren().add(vrchniBar);
                            hp.setPrefWidth(Integer.MAX_VALUE);
                            vrchniBar.setPrefWidth(Integer.MAX_VALUE);
                        }
                )
        );

        this.getPane().setLeft(
                Sloupec(List.of(
                                LabelAplikace("Adresa:"),
                                TlacitkoAplikace("Ceník", (t)->{
                                    VBox.setMargin(t, new Insets(10,0,0,20));}),
                                TlacitkoAplikace("Přidat recenzi", (t)->{
                                    VBox.setMargin(t, new Insets(10,0,0,20));
                                })

                        ), sloupec -> {}
                )

        );

        this.getPane().setBottom(
                pripravTabulku()
        );
    }

    private void prenactiTabulku() {
        this.seznamRecenzi.clear();

        for (Recenze recenze : this.zobrazovanyPodnik.getRecenze()) {
            if (recenze.getUzivatelskeJmeno() != null) break;
            String jmeno = this.controller.getDatabaze().getUzivatelskeJmeno(recenze.getUzivatel());
            recenze.setUzivatelskeJmeno(jmeno);
        }
        this.seznamRecenzi.addAll(this.zobrazovanyPodnik.getRecenze());
    }

    private TableView<Recenze> pripravTabulku() {
        Tabulka<Recenze> recenzeTabulka = new Tabulka<>(Recenze.PRO_TABULKU);
        recenzeTabulka.getTableView().setItems(this.seznamRecenzi);


        return recenzeTabulka.getTableView();
    }
}
