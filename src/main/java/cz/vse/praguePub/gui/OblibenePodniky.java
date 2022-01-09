package cz.vse.praguePub.gui;

import cz.vse.praguePub.gui.komponenty.Tabulka;
import cz.vse.praguePub.logika.dbObjekty.Podnik;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.BorderPane;


import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

import static cz.vse.praguePub.gui.komponenty.Komponenty.HorniPanel;
import static cz.vse.praguePub.gui.komponenty.Komponenty.NadpisOknaLabel;

public class OblibenePodniky extends Obrazovka<BorderPane> {

    private final ObservableList<Podnik> zobrazenePodniky;

    private final Supplier<List<Podnik>> ziskejPodniky;
    private final Consumer<Podnik> odeberPodnik;
    private final Consumer<Podnik> upravPodnik;

    public OblibenePodniky(
            Supplier<List<Podnik>> ziskejPodniky,
            Consumer<Podnik> odeberCallback,
            Consumer<Podnik> upravCallback
    ) {
        super(new BorderPane(),  900, 600, "background");
        this.zobrazenePodniky = FXCollections.observableArrayList();
        this.odeberPodnik = odeberCallback;
        this.ziskejPodniky = ziskejPodniky;
        this.upravPodnik = upravCallback;

        this.nactiPodniky();
        this.vytvorGui();
    }

    private void nactiPodniky() {
        this.zobrazenePodniky.clear();
        this.zobrazenePodniky.addAll(this.ziskejPodniky.get());
    }

    /**
     * Metoda, která vytváří grafické rozhraní.
     */
    private void vytvorGui() {
        this.getPane().setTop(
                HorniPanel(
                        horniPanel -> {
                            horniPanel.getChildren().add(NadpisOknaLabel("Oblíbené podniky"));
                            horniPanel.setAlignment(Pos.BASELINE_LEFT);
                        }
                )
        );

        this.getPane().setCenter(
                this.pripravTabulku(this.zobrazenePodniky)
        );
    }

    private TableView<Podnik> pripravTabulku(ObservableList<Podnik> zobrazovanePodniky) {
        Tabulka<Podnik> tab = new Tabulka<>(Podnik.PRO_TABULKU);

        TableView<Podnik> tv = tab.getTableView();
        tv.setItems(zobrazovanePodniky);

        ContextMenu contextMenu = this.pripravContextoveMenu(tab);

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

    private ContextMenu pripravContextoveMenu(Tabulka<Podnik> tabulka) {
        TableView<Podnik> tv = tabulka.getTableView();

        MenuItem odebratMenuItem = new MenuItem("Odebrat z oblíbených");
        odebratMenuItem.setOnAction(
                event -> {
                    this.odeberPodnik.accept(tv.getSelectionModel().getSelectedItem());
                    this.nactiPodniky();
                }
        );

        MenuItem upravitMenuItem = new MenuItem("Upravit podnik");
        upravitMenuItem.setOnAction(
                event -> this.upravPodnik.accept(tv.getSelectionModel().getSelectedItem())
        );

        MenuItem zrusitMenuItem = new MenuItem("Zrušit");

        return new ContextMenu(
                upravitMenuItem,
                new SeparatorMenuItem(),
                odebratMenuItem,
                zrusitMenuItem
        );
    }
}
