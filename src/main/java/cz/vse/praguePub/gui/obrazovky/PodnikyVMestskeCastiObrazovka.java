package cz.vse.praguePub.gui.obrazovky;

import cz.vse.praguePub.gui.ObrazovkyController;
import cz.vse.praguePub.gui.komponenty.Tabulka;
import cz.vse.praguePub.gui.obrazovky.abstraktniObrazovky.OknoSeSeznamemPodniku;
import cz.vse.praguePub.logika.Databaze;
import cz.vse.praguePub.logika.PodnikFiltrBuilder;
import cz.vse.praguePub.logika.dbObjekty.Podnik;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.stage.Stage;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Consumer;

import static cz.vse.praguePub.gui.komponenty.Komponenty.*;

public class PodnikyVMestskeCastiObrazovka extends OknoSeSeznamemPodniku {

    private final Stage stage;

    private final int cisloMestskeCasti;
    private final ObrazovkyController controller;
    private final Databaze databaze;

    public PodnikyVMestskeCastiObrazovka(ObrazovkyController controller, Stage stage, int cisloMestskeCasti) {
        this.cisloMestskeCasti = cisloMestskeCasti;
        this.controller = controller;
        this.databaze = controller.getDatabaze();
        this.stage = stage;

        this.nactiPodniky(null);
        super.vytvorGUI();
    }

    private void nactiPodniky(PodnikFiltrBuilder pfb) {
        this.getZobrazenePodniky().clear();
        PodnikFiltrBuilder filtr =
                pfb == null ?
                            this.databaze.getPodnikFiltrBuilder().cisloMestskeCasti(this.cisloMestskeCasti)
                        :   pfb;

        this.getZobrazenePodniky().addAll(filtr.finalizuj());
    }

    private void zpracujFiltr() {
        Map<String, FiltrDialog.AtributFilteru> atributy = new LinkedHashMap<>();
        FiltrDialog.FILTR_PODNIKY.forEach(
                (key, atr) -> {
                    if (!key.equals("cena") && !key.equals("mc_cislo")) atributy.put(key, atr);
                }
        );

        Consumer<Map<String, String>> callbackSFiltrem = filtrMapa -> {
            PodnikFiltrBuilder pfb = this.databaze.getPodnikFiltrBuilder();
            pfb.cisloMestskeCasti(this.cisloMestskeCasti)
                    .parse(filtrMapa);

            this.nactiPodniky(pfb);
        };

        this.controller.zobrazFiltr(atributy, callbackSFiltrem);
    }

    @Override
    protected ContextMenu pripravKontextoveMenuAUpravTabulku(Tabulka<Podnik> tabulka) {
        TableView<Podnik> tv = tabulka.getTableView();
        Runnable zobrazInformaceOPodniku = () -> {

            if (tv.getItems().isEmpty() || !this.controller.jeUzivatelPrihlasen()) return;

            this.controller.zobrazInformaceOPodniku(
                    tabulka.getTableView().getSelectionModel().getSelectedItem(),
                    this.stage,
                    this.getScene());
        };

        ContextMenu menu = new ContextMenu();
            MenuItem zobrazInfo = new MenuItem("Zobraz podnik");
            zobrazInfo.setOnAction(
                    event -> zobrazInformaceOPodniku.run()
            );

        tabulka.getTableView().setOnMouseClicked(
                event -> {
                    if (event.getClickCount() == 2) zobrazInformaceOPodniku.run();
                }
        );

        menu.getItems().add(zobrazInfo);

        return menu;
    }

    @Override
    protected HBox pripravHorniPanel() {
        return HorniPanel((horniPanel) -> {
            Label nazevLokace = NadpisOknaLabel("Praha " + this.cisloMestskeCasti);

            Button pridatNovyPodnik = TlacitkoAplikace("Přidat nový podnik", t -> this.controller.zobrazPridejNovyPodnik(),null);
            Button filtrovat = TlacitkoAplikace("Filtr",
                    t -> this.zpracujFiltr(),
                    null
            );

            Region separator = new Region();
            HBox.setHgrow(separator, Priority.ALWAYS);

            horniPanel.getChildren().addAll(nazevLokace, pridatNovyPodnik, separator, filtrovat);
        });
    }
}
