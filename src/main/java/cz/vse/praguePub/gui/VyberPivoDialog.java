package cz.vse.praguePub.gui;

import com.mongodb.client.MongoCollection;
import cz.vse.praguePub.gui.komponenty.Tabulka;
import cz.vse.praguePub.logika.PivoFiltrBuilder;
import cz.vse.praguePub.logika.dbObjekty.Pivo;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import static cz.vse.praguePub.gui.komponenty.Komponenty.*;

public class VyberPivoDialog extends Obrazovka<BorderPane> {
    private final static Logger log = LoggerFactory.getLogger(VyberPivoDialog.class);

    private final List<Pivo> vsechnaPiva;
    private final MongoCollection<Document> kolekcePiv;
    private final ObservableList<Pivo> zobrazovanaPiva;
    private final Stage oknoProFilter;

    private final Consumer<Pivo> callbackSVysledkem;

    public VyberPivoDialog(MongoCollection<Document> kolekcePiv, Consumer<Pivo> callbackSVysledkem) {
        super(new BorderPane(), 600, 600, "background");

        this.vsechnaPiva = new ArrayList<>();
        this.kolekcePiv = kolekcePiv;
        this.zobrazovanaPiva = FXCollections.observableArrayList();
        this.oknoProFilter = new Stage();

        for (Document pivo : kolekcePiv.find()) this.vsechnaPiva.add(Pivo.inicializujZDokumentu(pivo, null, null));
        this.zobrazovanaPiva.addAll(this.vsechnaPiva);
        this.callbackSVysledkem = callbackSVysledkem;

        this.vytvorGUI();

    }

    private void zahajFiltrovani() {
        Consumer<Map<String, Object>> konzumujFiltr = filtery -> {
            PivoFiltrBuilder pfb = new PivoFiltrBuilder(this.kolekcePiv);
            if (filtery.get("nazev_pivovaru") != null) pfb.pivovar((String) filtery.get("nazev_pivovaru"));

            this.oknoProFilter.hide();
        };

        this.oknoProFilter.setScene(new Filtr(konzumujFiltr).getScene());
        this.oknoProFilter.show();
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
