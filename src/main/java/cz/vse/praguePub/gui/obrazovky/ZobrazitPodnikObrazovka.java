package cz.vse.praguePub.gui.obrazovky;

import cz.vse.praguePub.gui.ObrazovkyController;
import cz.vse.praguePub.gui.komponenty.AlertBuilder;
import cz.vse.praguePub.gui.komponenty.Tabulka;
import cz.vse.praguePub.gui.obrazovky.abstraktniObrazovky.Obrazovka;
import cz.vse.praguePub.logika.Databaze;
import cz.vse.praguePub.logika.dbObjekty.Podnik;
import cz.vse.praguePub.logika.dbObjekty.Recenze;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.List;
import java.util.Optional;

import static cz.vse.praguePub.gui.komponenty.Komponenty.*;

public class ZobrazitPodnikObrazovka extends Obrazovka<BorderPane> {

    private final ObrazovkyController controller;
    private final Podnik zobrazovanyPodnik;
    private final Runnable tlacitkoZpetCallback;
    private final Stage stage;

    private final BooleanProperty jeVOblibenych;

    private final StringProperty nazevPodniku = new SimpleStringProperty();
    private final StringProperty adresaPodniku = new SimpleStringProperty();
    private final DoubleProperty prumerneHodnoceni = new SimpleDoubleProperty();
    private final ObservableList<Recenze> seznamRecenzi;


    private final Runnable prenacti = () -> {
        this.nactiHodnotyTabulky();
        this.nactiAtributyPodniku();
    };

    public ZobrazitPodnikObrazovka(ObrazovkyController controller, Podnik podnik, Stage stage, Runnable callback) {
        super(new BorderPane(), 800, 600, "background");

        this.controller = controller;
        this.zobrazovanyPodnik = podnik;
        this.tlacitkoZpetCallback = callback;
        this.seznamRecenzi = FXCollections.observableArrayList();
        this.stage = stage;
        this.jeVOblibenych = new SimpleBooleanProperty(false);

        this.nactiAtributyPodniku();
        this.vytvorGUI();
    }

    private void nactiAtributyPodniku() {
        this.nazevPodniku.setValue(this.zobrazovanyPodnik.getNazev());
        this.adresaPodniku.setValue(
                this.zobrazovanyPodnik.getAdresa_ulice() +
                        " " +
                        this.zobrazovanyPodnik.getAdresa_cp() +
                        ", " +
                        this.zobrazovanyPodnik.getAdresa_psc() +
                        " " +
                        this.zobrazovanyPodnik.getAdresa_mc_nazev()
        );
        this.prumerneHodnoceni.setValue(this.zobrazovanyPodnik.getPrumerneHodnoceni());
        this.seznamRecenzi.clear();
        this.seznamRecenzi.addAll(this.zobrazovanyPodnik.getRecenze());
        if(!this.controller.getDatabaze().getUzivatel().isGuest()) {
            boolean jeVOblibenych = this.controller.getDatabaze().jeVOblibenych(this.zobrazovanyPodnik);
            this.jeVOblibenych.setValue(jeVOblibenych);
        }
    }

    private void vytvorGUI() {
        this.getPane().setTop(
                HorniPanel(hp -> {
                            BorderPane vrchniBar = new BorderPane();
                            Label nadpis = NadpisOknaLabel(this.nazevPodniku.toString());
                            nadpis.textProperty().bind(this.nazevPodniku);


                            vrchniBar.setLeft(
                                    Radek(
                                            nadpis,
                                            Radek(LabelAplikace(
                                                    String.valueOf(this.prumerneHodnoceni.get()),
                                                    label -> {
                                                        this.prumerneHodnoceni.addListener(
                                                                (obs, oldValue, newValue) -> {
                                                                    label.setText(newValue.toString());
                                                                }
                                                        );
                                                        HBox.setMargin(label, new Insets( 6,0,0,5));
                                                    }))
                                    )
                            );

                            vrchniBar.setRight(
                                    Radek(
                                            TlacitkoAplikace(
                                                    this.jeVOblibenych.get() ? "Odeber z oblíbených" : "Přidej do oblíbených",
                                                    event -> {
                                                        if(!this.controller.getDatabaze().getUzivatel().isGuest()) {
                                                            Databaze db = this.controller.getDatabaze();
                                                            if (db.jeVOblibenych(this.zobrazovanyPodnik))
                                                                db.odeberZOblibenych(this.zobrazovanyPodnik);
                                                            else db.pridejDoOblibenych(this.zobrazovanyPodnik);
                                                        }
                                                        else {
                                                            this.controller.zobrazPrihlaseni();
                                                        }
                                                        this.nactiAtributyPodniku();
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
                                                    event -> this.controller.zobrazUpraveniPodniku(this.zobrazovanyPodnik, this.stage, this.getScene()),
                                                    t -> HBox.setMargin(t, new Insets(6,0,0,5))),
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
                                Spacer(),Radek(LabelAplikace("Adresa:"),
                                LabelAplikace(this.adresaPodniku.get(), (t)->{t.setPrefWidth(500);} )),
                                TlacitkoAplikace("Ceník", (t)->{
                                    VBox.setMargin(t, new Insets(10,0,0,10));
                                    t.setOnMouseClicked(MouseEvent -> controller.ukazCenik(this.zobrazovanyPodnik,this.prenacti));}),
                                Radek(
                                        TlacitkoAplikace("Přidat recenzi", (t)->{
                                    VBox.setMargin(t, new Insets(10,0,0,10));
                                    t.setOnMouseClicked(MouseEvent -> controller.zobrazPridejNovouRecenzi(this.zobrazovanyPodnik, this.prenacti));
                                }),
                                        TlacitkoAplikace("Odstraň podnik", mouseEvent -> this.odstranPodnik(), null)),
                                Spacer()
                        ), sloupec -> {VBox.setVgrow(sloupec, Priority.ALWAYS);}
                )

        );

        this.getPane().setBottom(
                pripravTabulku()
        );
    }

    private void odstranPodnik() {
        if (!this.controller.jeUzivatelPrihlasen()) return;

        Optional<ButtonType> res = new AlertBuilder(Alert.AlertType.CONFIRMATION)
                .setTitle("Prague Pub")
                .setHeaderText("Opravdu?")
                .setContent("Opravdu chcete smazat podnik " + this.zobrazovanyPodnik.getNazev())
                .getAlert()
                .showAndWait();

        if (res.isEmpty()) return;
        if (res.get() == ButtonType.OK) {
            this.controller.getDatabaze().vymazPodnik(this.zobrazovanyPodnik);
            this.stage.hide();
        }
    }

    private TableView<Recenze> pripravTabulku() {
        Tabulka<Recenze> recenzeTabulka = new Tabulka<>(Recenze.PRO_TABULKU);
        recenzeTabulka.getTableView().setItems(this.seznamRecenzi);
        this.nactiHodnotyTabulky();

        return recenzeTabulka.getTableView();
    }

    private void nactiHodnotyTabulky() {
        this.seznamRecenzi.clear();

        for (Recenze recenze : this.zobrazovanyPodnik.getRecenze()) {
            String jmeno = this.controller.getDatabaze().getUzivatelskeJmeno(recenze.getUzivatel());
            recenze.setUzivatelskeJmeno(jmeno);
        }
        this.seznamRecenzi.addAll(this.zobrazovanyPodnik.getRecenze());
    }
}
