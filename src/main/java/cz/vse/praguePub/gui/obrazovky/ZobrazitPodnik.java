package cz.vse.praguePub.gui.obrazovky;

import cz.vse.praguePub.gui.ObrazovkyController;
import cz.vse.praguePub.gui.komponenty.Tabulka;
import cz.vse.praguePub.gui.obrazovky.abstraktniObrazovky.Obrazovka;
import cz.vse.praguePub.logika.Databaze;
import cz.vse.praguePub.logika.dbObjekty.Podnik;
import cz.vse.praguePub.logika.dbObjekty.Recenze;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
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
    private final Runnable tlacitkoZpetCallback;

    private final BooleanProperty jeVOblibenych;

    public ZobrazitPodnik(ObrazovkyController controller, Podnik podnik, Runnable callback) {
        super(new BorderPane(), 700, 700, "background");

        this.controller = controller;
        this.zobrazovanyPodnik = podnik;
        this.tlacitkoZpetCallback = callback;
        this.seznamRecenzi = FXCollections.observableArrayList();
        this.jeVOblibenych = new SimpleBooleanProperty(false);

        this.prenactiTabulku();
        this.vytvorGUI();
    }

    private void zjistiJeVOblibenych() {
        boolean jeVOblibenych = this.controller.getDatabaze().jeVOblibenych(this.zobrazovanyPodnik);
        this.jeVOblibenych.setValue(jeVOblibenych);
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
                                            TlacitkoAplikace(
                                                    "srdicko sem",
                                                    event -> {
                                                        Databaze db = this.controller.getDatabaze();
                                                        if (db.jeVOblibenych(this.zobrazovanyPodnik)) db.odeberZOblibenych(this.zobrazovanyPodnik);
                                                        else db.pridejDoOblibenych(this.zobrazovanyPodnik);
                                                        this.zjistiJeVOblibenych();
                                                    },
                                                    t -> {
                                                        this.jeVOblibenych.addListener(
                                                                (observable, oldValue, newValue) -> {
                                                                    if (newValue) t.setText("Odeber z oblíbených");
                                                                    else t.setText("Přidej do oblíbených");
                                                                }
                                                        );
                                                    }
                                            ),
                                            TlacitkoAplikace(
                                                    "Upravit",
                                                    event -> this.controller.zobrazUpraveniPodniku(this.zobrazovanyPodnik),
                                                    t -> HBox.setMargin(t, new Insets(6,8,0,5))),
                                            TlacitkoZpet(
                                                    event -> this.tlacitkoZpetCallback.run(),
                                                    t -> {}
                                            )
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
