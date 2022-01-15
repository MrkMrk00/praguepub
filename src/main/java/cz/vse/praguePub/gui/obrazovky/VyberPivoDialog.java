package cz.vse.praguePub.gui.obrazovky;

import cz.vse.praguePub.gui.ObrazovkyController;
import cz.vse.praguePub.gui.komponenty.Tabulka;
import cz.vse.praguePub.gui.obrazovky.abstraktniObrazovky.Obrazovka;
import cz.vse.praguePub.logika.Databaze;
import cz.vse.praguePub.logika.dbObjekty.Pivo;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
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
    private final Stage stage;

    private final ObservableList<Pivo> zobrazovanaPiva;
    private final Consumer<Pivo> callbackSVysledkem;

    public VyberPivoDialog(ObrazovkyController controller, Stage stage, Consumer<Pivo> callbackSVysledkem) {
        super(new BorderPane(), 600, 600, "background");
        this.controller = controller;
        this.db = controller.getDatabaze();
        this.stage = stage;

        this.zobrazovanaPiva = FXCollections.observableArrayList();
        this.callbackSVysledkem = callbackSVysledkem;

        this.zobrazovanaPiva.addAll(this.db.getPivoFilterBuilder().finalizuj());
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
        TableView<Pivo> tabulkaTV = this.pripravTabulku();

        this.getPane().setTop(
                HorniPanel(horniPanel -> horniPanel.getChildren().addAll(
                        NadpisOknaLabel("Vyber pivo"),
                        Spacer(),
                        TlacitkoAplikace("Filtr",
                                t -> t.setOnMouseClicked(
                                        event -> this.zahajFiltrovani()
                                )
                        ),
                        TlacitkoAplikace("Vytvoř nové pivo",
                                t -> t.setOnMouseClicked(
                                        event -> this.controller.vytvorNovePivo(
                                                this.stage,
                                                this.getScene(),
                                                tabulkaTV::refresh
                                        )
                                )
                        )
                ))
        );

        this.getPane().setCenter(tabulkaTV);
    }

    private TableView<Pivo> pripravTabulku() {
        Tabulka<Pivo> pivoTabulka = new Tabulka<>(Pivo.PRO_TABULKU_BEZ_CENY_A_OBJEMU);
        TableView<Pivo> tv = pivoTabulka.getTableView();
        tv.setItems(this.zobrazovanaPiva);

        tv.setRowFactory(tableView -> {
            TableRow<Pivo> tableRow = new TableRow<>();
            tableRow.setOnMouseClicked(
                    mouseEvent -> {
                        Pivo vybranePivo = tableRow.getItem();
                        if (mouseEvent.getClickCount() == 2 && vybranePivo != null) this.callbackSVysledkem.accept(vybranePivo);
                    }
            );
            return tableRow;
        });

        return pivoTabulka.getTableView();
    }
}
