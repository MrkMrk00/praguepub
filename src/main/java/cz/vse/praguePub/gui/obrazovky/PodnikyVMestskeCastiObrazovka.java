package cz.vse.praguePub.gui.obrazovky;

import cz.vse.praguePub.gui.ObrazovkyController;
import cz.vse.praguePub.gui.komponenty.Tabulka;
import cz.vse.praguePub.gui.obrazovky.abstraktniObrazovky.OknoSeSeznamemPodniku;
import cz.vse.praguePub.logika.Databaze;
import cz.vse.praguePub.logika.PodnikFiltrBuilder;
import cz.vse.praguePub.logika.dbObjekty.Podnik;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Consumer;

import static cz.vse.praguePub.gui.komponenty.Komponenty.*;

public class PodnikyVMestskeCastiObrazovka extends OknoSeSeznamemPodniku {

    private final int cisloMestskeCasti;
    private final ObrazovkyController controller;
    private final Databaze databaze;

    public PodnikyVMestskeCastiObrazovka(int cisloMestskeCasti, ObrazovkyController controller) {
        this.cisloMestskeCasti = cisloMestskeCasti;
        this.controller = controller;
        this.databaze = controller.getDatabaze();

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
        Map<String, Filtr.AtributFilteru> atributy = new LinkedHashMap<>();
        Filtr.FILTR_PODNIKY.forEach(
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

        this.controller.filtruj(atributy, callbackSFiltrem);
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
