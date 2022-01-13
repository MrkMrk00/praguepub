package cz.vse.praguePub.gui.obrazovky;

import cz.vse.praguePub.gui.ObrazovkyController;
import cz.vse.praguePub.gui.komponenty.Tabulka;
import cz.vse.praguePub.gui.obrazovky.abstraktniObrazovky.Obrazovka;
import cz.vse.praguePub.logika.Databaze;
import cz.vse.praguePub.logika.dbObjekty.Pivo;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import static cz.vse.praguePub.gui.komponenty.Komponenty.*;

public class VyberPivoDialog extends Obrazovka<BorderPane> {
    private final static Logger log = LoggerFactory.getLogger(VyberPivoDialog.class);

    private final ObrazovkyController controller;
    private final Databaze db;

    private final ObservableList<Pivo> zobrazovanaPiva;
    private final Consumer<Pivo> callbackSVysledkem;

    public VyberPivoDialog(ObrazovkyController controller, Consumer<Pivo> callbackSVysledkem) {
        super(new BorderPane(), 600, 600, "background");
        this.controller = controller;
        this.db = controller.getDatabaze();

        this.zobrazovanaPiva = FXCollections.observableArrayList();
        this.callbackSVysledkem = callbackSVysledkem;

        this.vytvorGUI();
    }

    private void zahajFiltrovani() {
        Consumer<Map<String, String>> konzumujAtributy = atributy -> {
            this.zobrazovanaPiva.clear();
            List<Pivo> vyfiltrovana = this.db.getPivoFilterBuilder().parse(atributy).finalizuj();
            this.zobrazovanaPiva.addAll(vyfiltrovana);
        };

        this.controller.zobrazFiltr(FiltrDialog.FILTR_PIVA, konzumujAtributy);
    }


    private void vytvorGUI() {
        this.getPane().setTop(
                HorniPanel(horniPanel -> horniPanel.getChildren().addAll(
                        NadpisOknaLabel("Vyber pivo"),
                        TlacitkoAplikace("Filter",
                                t -> t.setOnMouseClicked(
                                        event -> this.zahajFiltrovani()
                                )
                        )
                ))
        );

        this.getPane().setCenter(this.pripravTabulku());
    }

    private TableView<Pivo> pripravTabulku() {
        Tabulka<Pivo> pivoTabulka = new Tabulka<>(Pivo.PRO_TABULKU_BEZ_CENY_A_OBJEMU);
        pivoTabulka.getTableView().setItems(this.zobrazovanaPiva);
        pivoTabulka.getTableView().setOnMouseClicked(
                event -> {
                    if (event.getClickCount() == 2) {
                        this.callbackSVysledkem.accept(pivoTabulka.getTableView().getSelectionModel().getSelectedItem());
                    }
                }
        );
        return pivoTabulka.getTableView();
    }
}
