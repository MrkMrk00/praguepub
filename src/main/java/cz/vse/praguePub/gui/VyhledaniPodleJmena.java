package cz.vse.praguePub.gui;

import cz.vse.praguePub.gui.komponenty.Tabulka;
import cz.vse.praguePub.logika.dbObjekty.Podnik;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;

import static cz.vse.praguePub.gui.komponenty.Komponenty.*;

    //Chybí dodelat propojeni s databazi.

public class VyhledaniPodleJmena extends Obrazovka<BorderPane> {

    public VyhledaniPodleJmena() {
        super(new BorderPane(), 900,600, "background");
        this.registrujInputy();
        this.vytvorGUI();
    }

    private void registrujInputy() {
        this.getMapaInputu().put(
                "vyhledat", TextFieldAplikace("Vyhledat", t -> {})
        );
    }

    private void vytvorGUI() {
        this.getPane().setTop(
                HorniPanel(
                        (horniPanel) -> horniPanel.getChildren().addAll(
                                NadpisOknaLabel("Vyhledávání podle jména"),
                                this.getMapaInputu().get("vyhledat")
                        )
                )
        );

        this.getPane().setCenter(pripravtabulku());
    }

    private TableView<Podnik> pripravtabulku() {
        Tabulka<Podnik> oblibenePodnikyTabulka = new Tabulka<>(Podnik.PRO_TABULKU);

        return oblibenePodnikyTabulka.getTableView();
    }
}
