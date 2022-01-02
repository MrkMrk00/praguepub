package cz.vse.praguePub.gui;

import cz.vse.praguePub.gui.komponenty.Tabulka;
import cz.vse.praguePub.logika.Databaze;
import cz.vse.praguePub.logika.dbObjekty.Pivo;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.bson.conversions.Bson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;
import java.util.function.Consumer;

import static cz.vse.praguePub.gui.komponenty.Komponenty.*;

public class VyberPivo extends Obrazovka<BorderPane> {
    private final static Logger LOGGER = LoggerFactory.getLogger(VyberPivo.class);

    private final Databaze databaze;
    private final ObservableList<Pivo> pivaZDatabaze;
    private final Stage oknoProFilter;
    private final Consumer<Pivo> vysledek;

    public VyberPivo(Databaze databaze, Consumer<Pivo> vysledek) {
        super(new BorderPane(), 600, 600, "background");

        this.databaze = databaze;
        this.vysledek = vysledek;

        this.pivaZDatabaze = FXCollections.observableArrayList();
        this.oknoProFilter = new Stage();

        this.vytvorGUI();
    }

    private void zahajFiltrovani() {
        Consumer<Bson> konzumujFiltr = bsonFilter -> {
            this.pivaZDatabaze.addAll(this.najdiVDatabazi(bsonFilter));
            this.pivaZDatabaze.forEach(it -> LOGGER.info(it.toString()));
            this.oknoProFilter.hide();
        };

        this.oknoProFilter.setScene(new Filtr(konzumujFiltr).getScene());
        this.oknoProFilter.show();
    }

    private Set<Pivo> najdiVDatabazi(Bson filter) {
        LOGGER.debug("dostalo se to do #najdiVDatabazi()");
        return this.databaze.getPiva(filter);
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
        Tabulka<Pivo> pivoTabulka = new Tabulka<>(Pivo.PRO_TABULKU);
        pivoTabulka.getTableView().setItems(this.pivaZDatabaze);
        pivoTabulka.getTableView().setOnMouseClicked(
                event -> {
                    if (event.getClickCount() == 2) {
                        this.vysledek.accept(pivoTabulka.getTableView().getSelectionModel().getSelectedItem());
                    }
                }
        );
        return pivoTabulka.getTableView();
    }
}
