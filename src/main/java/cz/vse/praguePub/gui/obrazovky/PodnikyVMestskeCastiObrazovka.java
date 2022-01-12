package cz.vse.praguePub.gui.obrazovky;

import com.mongodb.client.MongoCollection;
import cz.vse.praguePub.gui.ObrazovkyController;
import cz.vse.praguePub.gui.komponenty.Tabulka;
import cz.vse.praguePub.gui.obrazovky.abstraktniObrazovky.Obrazovka;
import cz.vse.praguePub.gui.obrazovky.abstraktniObrazovky.OknoSeSeznamemPodniku;
import cz.vse.praguePub.logika.Databaze;
import cz.vse.praguePub.logika.PivoFiltrBuilder;
import cz.vse.praguePub.logika.dbObjekty.Podnik;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import lombok.Data;
import org.bson.Document;

import java.util.Map;
import java.util.function.Consumer;

import static cz.vse.praguePub.gui.komponenty.Komponenty.HorniPanel;
import static cz.vse.praguePub.gui.komponenty.Komponenty.TlacitkoAplikace;

public class PodnikyVMestskeCastiObrazovka extends OknoSeSeznamemPodniku {

    private final int cisloMestskeCasti;
    private final ObrazovkyController controller;
    private final Databaze databaze;
    private final Stage oknoProFilter;
    private final ObservableList<Podnik> kolekceHospod;

    public PodnikyVMestskeCastiObrazovka(int cisloMestskeCasti, ObrazovkyController controller) {
        this.cisloMestskeCasti = cisloMestskeCasti;
        this.controller = controller;
        this.databaze = controller.getDatabaze();
        this.oknoProFilter = new Stage();

        this.nactiPodniky();
        super.vytvorGUI();
    }

    private void nactiPodniky() {
        this.getZobrazenePodniky().clear();

        this.getZobrazenePodniky().addAll(
            this.databaze.getPodnikFiltrBuilder()
                    .cisloMestskeCasti(this.cisloMestskeCasti)
                    .finalizuj()

        );
    }

    private void zahajFiltrovani() {
        Consumer<Map<String, Object>> konzumujFiltr = filtery -> {
            PivoFiltrBuilder pfb = new PivoFiltrBuilder(this.kolekceHospod);
            if (filtery.get("nazev_pivovaru") != null) pfb.pivovar((String) filtery.get("nazev_pivovaru"));

            this.oknoProFilter.hide();
        };

        this.oknoProFilter.setScene(new Filtr(konzumujFiltr).getScene());
        this.oknoProFilter.show();
    }

    @Override
    protected ContextMenu pripravContextoveMenu(Tabulka<Podnik> tabulka) {
        ContextMenu menu = new ContextMenu();
            MenuItem upravPodnik = new MenuItem("Uprav podnik");

        return new ContextMenu();
    }

    @Override
    protected HBox pripravHorniPanel() {
        return HorniPanel((horniPanel) -> {
            Label nazevLokace = new Label("Praha " + this.cisloMestskeCasti);
            nazevLokace.setFont(Font.font("Helvetica", FontWeight.BOLD, 30));
            nazevLokace.setAlignment(Pos.BASELINE_LEFT);

            Button pridatNovyPodnik = TlacitkoAplikace("Pridat novy podnik", t->this.controller.zobrazPridejNovyPodnik(databaze),null );
            Button filtrovat = TlacitkoAplikace("Filter",
                    t -> t.setOnMouseClicked(
                            event -> this.zahajFiltrovani()
                    )
            );

            horniPanel.getChildren().addAll(nazevLokace, pridatNovyPodnik, filtrovat);
        });
    }
}
