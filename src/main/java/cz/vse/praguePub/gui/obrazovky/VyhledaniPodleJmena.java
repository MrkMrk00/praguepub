package cz.vse.praguePub.gui.obrazovky;

import cz.vse.praguePub.gui.ObrazovkyController;
import cz.vse.praguePub.gui.komponenty.Tabulka;
import cz.vse.praguePub.gui.obrazovky.abstraktniObrazovky.Obrazovka;
import cz.vse.praguePub.logika.Databaze;
import cz.vse.praguePub.logika.dbObjekty.Podnik;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.TableView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;

import static cz.vse.praguePub.gui.komponenty.Komponenty.*;

public class VyhledaniPodleJmena extends Obrazovka<BorderPane> {

    private final ObservableList<Podnik> seznamPodnikuProTabulku;
    private final Databaze db;


    public VyhledaniPodleJmena(ObrazovkyController controller) {
        super(new BorderPane(), 900,600, "background");

        this.seznamPodnikuProTabulku = FXCollections.observableArrayList();
        this.db = controller.getDatabaze();

        this.registrujInputy();
        this.vytvorGUI();
        this.vyfiltrujPodniky();
    }

    public VyhledaniPodleJmena(ObrazovkyController controller, String defaultniDotaz) {
        super(new BorderPane(), 900,600, "background");

        this.seznamPodnikuProTabulku = FXCollections.observableArrayList();
        this.db = controller.getDatabaze();

        this.registrujInputy();
        this.vytvorGUI();

        this.getMapaInputu().get("vyhledat").setText(defaultniDotaz);
        this.vyfiltrujPodniky();
    }

    private void registrujInputy() {
        this.getMapaInputu().put(
                "vyhledat",         TextFieldAplikace(
                        "",
                        t -> {
                            t.setOnMouseClicked(mouseEvent -> t.clear());
                            t.setOnKeyPressed(
                                    keyEvent -> {
                                        if (keyEvent.getCode() == KeyCode.ENTER) this.vyfiltrujPodniky();
                                    }
                            );

                            HBox.setMargin(t, new Insets(0,0,0,5));
                        }
                )
        );
    }

    private void vytvorGUI() {
        this.getPane().setTop(
                HorniPanel(
                        horniPanel -> horniPanel.getChildren().addAll(
                                NadpisOknaLabel("Vyhledávání podle jména"),
                                this.getMapaInputu().get("vyhledat"),
                                TlacitkoAplikace("Vyhledej",
                                        t -> {
                                            t.setOnMouseClicked(mouseEvent -> this.vyfiltrujPodniky());
                                            HBox.setMargin(t, new Insets(0,0,0,5));
                                        }

                                )
                        )
                )
        );

        this.getPane().setCenter(pripravtabulku());
    }

    private void vyfiltrujPodniky() {
        String pozadovanyNazev = this.getMapaInputu().get("vyhledat").getText();

        this.seznamPodnikuProTabulku.clear();
        this.seznamPodnikuProTabulku.addAll(
                this.db.getPodnikFiltrBuilder()
                    .nazev(pozadovanyNazev)
                    .finalizuj()
        );
    }

    private TableView<Podnik> pripravtabulku() {
        Tabulka<Podnik> oblibenePodnikyTabulka = new Tabulka<>(Podnik.PRO_TABULKU);
        oblibenePodnikyTabulka.getTableView().setItems(this.seznamPodnikuProTabulku);

        return oblibenePodnikyTabulka.getTableView();
    }
}
