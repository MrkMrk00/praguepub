package cz.vse.praguePub.gui;

import cz.vse.praguePub.gui.komponenty.Tabulka;
import cz.vse.praguePub.logika.dbObjekty.Podnik;
import javafx.geometry.Pos;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;


import static cz.vse.praguePub.gui.komponenty.Komponenty.HorniPanel;
import static cz.vse.praguePub.gui.komponenty.Komponenty.NadpisOknaLabel;

public class OblibenePodniky extends Obrazovka<BorderPane> {

    //Chybí propojit s databází oblibených podniků. Okno vypadá, jak má.

    public OblibenePodniky() {
        super(new BorderPane(),  900, 600, "background");

        this.vytvorGui();
    }

    /**
     * Metoda, která vytváří grafické rozhraní.
     */
    private void vytvorGui(){
        this.getPane().setTop(
                HorniPanel(
                        horniPanel -> {
                            horniPanel.getChildren().add(NadpisOknaLabel("Oblíbené podniky"));
                            horniPanel.setAlignment(Pos.BASELINE_LEFT);
                        }
                )
        );
        this.getPane().setCenter(this.pripravtabulku());
    }

    private TableView<Podnik> pripravtabulku() {
        Tabulka<Podnik> oblibenePodnikyTabulka = new Tabulka<>(Podnik.PRO_TABULKU);

        return oblibenePodnikyTabulka.getTableView();
    }
}
