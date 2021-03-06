package cz.vse.praguePub.gui.obrazovky.abstraktniObrazovky;

import cz.vse.praguePub.gui.komponenty.Tabulka;
import cz.vse.praguePub.logika.dbObjekty.Podnik;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import lombok.AccessLevel;
import lombok.Getter;

/**
 * Třída, která obsahuje implementaci metod zaměřující se na seznamy podniků v aplikaci
 * pro vytváření jednotlivých částí grafického rozhraní.
 */

public abstract class OknoSeSeznamemPodniku extends Obrazovka<BorderPane> {

    @Getter(AccessLevel.PROTECTED)
    private final ObservableList<Podnik> zobrazenePodniky;

    public OknoSeSeznamemPodniku() {
        super(new BorderPane(), 800, 600, "background");
        this.zobrazenePodniky = FXCollections.observableArrayList();
    }

    /**
     * Metoda, která vytváří grafické rozhraní.
     */
    protected void vytvorGUI() {
        this.getPane().setTop(this.pripravHorniPanel());

        this.getPane().setCenter(
                this.pripravTabulku(this.zobrazenePodniky)
        );
    }

    /**
     * Metoda, která vytváří tabulku podniků aplikace.
     */

    private TableView<Podnik> pripravTabulku(ObservableList<Podnik> zobrazovanePodniky) {
        Tabulka<Podnik> tab = new Tabulka<>(Podnik.PRO_TABULKU);

        TableView<Podnik> tv = tab.getTableView();
        tv.setItems(zobrazovanePodniky);

        ContextMenu contextMenu = this.pripravKontextoveMenuAUpravTabulku(tab);

        tv.setRowFactory(tableView -> {
            TableRow<Podnik> tableRow = new TableRow<>();
            tableRow.contextMenuProperty().bind(
                    Bindings.when(tableRow.emptyProperty())
                            .then((ContextMenu) null)
                            .otherwise(contextMenu)
            );
            return tableRow;
        });

        return tv;
    }

    /**
     * Metoda, která vytváří kontextové menu v obrazovkách grafického rozhraní a umožňuje úpravy tabulky aplikace.
     */

    protected abstract ContextMenu pripravKontextoveMenuAUpravTabulku(Tabulka<Podnik> tabulka);

    /**
     * Metoda, která vytváří horní panel v obrazovkách grafického rozhraní.
     */

    protected abstract HBox pripravHorniPanel();

}
