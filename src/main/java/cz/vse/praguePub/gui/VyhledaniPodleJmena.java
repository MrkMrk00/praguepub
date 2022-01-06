package cz.vse.praguePub.gui;

import com.mongodb.client.MongoCollection;
import cz.vse.praguePub.gui.komponenty.Tabulka;
import cz.vse.praguePub.logika.dbObjekty.Podnik;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;
import org.bson.Document;
import java.util.ArrayList;
import java.util.List;

import static cz.vse.praguePub.gui.komponenty.Komponenty.*;

public class VyhledaniPodleJmena extends Obrazovka<BorderPane> {

    private final List<Podnik> vsechnyPodniky;
    private final ObservableList<Podnik> vyhledanyPodnik;
    private final MongoCollection<Document> db;


    public VyhledaniPodleJmena(MongoCollection<Document> kolekce) {
        super(new BorderPane(), 900,600, "background");

        this.vsechnyPodniky = new ArrayList<>();
        this.vyhledanyPodnik = FXCollections.observableArrayList();
        this.db =kolekce;
        this.registrujInputy();

        for(Document podniky : kolekce.find()) this.vsechnyPodniky.add(Podnik.inicializujZDokumentu(podniky,kolekce));
        this.vyhledanyPodnik.addAll(this.vsechnyPodniky);
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
        oblibenePodnikyTabulka.getTableView().setItems(this.vyhledanyPodnik);

        return oblibenePodnikyTabulka.getTableView();
    }
}
