package cz.vse.praguePub.gui;

import cz.vse.praguePub.gui.komponenty.Tabulka;
import cz.vse.praguePub.logika.dbObjekty.Podnik;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.BorderPane;


import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

import static cz.vse.praguePub.gui.komponenty.Komponenty.HorniPanel;
import static cz.vse.praguePub.gui.komponenty.Komponenty.NadpisOknaLabel;

public class OblibenePodniky extends Obrazovka<BorderPane> {

    private final Supplier<List<Podnik>> ziskejPodniky;
    private final ObservableList<Podnik> zobrazenePodniky;
    private final Consumer<Podnik> odeberCallback;

    public OblibenePodniky(
            Supplier<List<Podnik>> ziskejPodniky,
            Consumer<Podnik> odeberCallback,
            Consumer<Podnik> upravCallback
    ) {
        super(new BorderPane(),  900, 600, "background");
        this.zobrazenePodniky = FXCollections.observableArrayList();
        this.odeberCallback = odeberCallback;
        this.ziskejPodniky = ziskejPodniky;

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
                this.pripravtabulku(this.zobrazenePodniky)
        );
    }

    private TableView<Podnik> pripravtabulku(ObservableList<Podnik> zobrazovanePodniky) {
        Tabulka<Podnik> tab = new Tabulka<>(Podnik.PRO_TABULKU);
        TableView<Podnik> tv = tab.getTableView();

        tv.setItems(zobrazovanePodniky);

        ContextMenu contextMenu = new ContextMenu();
            MenuItem odebratMenuItem = new MenuItem("Odebrat z oblíbených");
            MenuItem zrusit = new MenuItem("Zrušit");

        contextMenu.getItems().addAll(odebratMenuItem, new SeparatorMenuItem(), zrusit);

        tv.setOnMouseClicked(mouseEvent -> {
            if (mouseEvent.getButton() == MouseButton.SECONDARY) {
                odebratMenuItem.setOnAction(
                        event -> {
                            this.odeberCallback.accept(tv.getSelectionModel().getSelectedItem());
                        }
                );
                contextMenu.show(tv, mouseEvent.getScreenX(), mouseEvent.getScreenY());
            }
        });

        return tv;
    }
}
