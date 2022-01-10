package cz.vse.praguePub.gui.obrazovky;

import cz.vse.praguePub.gui.ObrazovkyController;
import cz.vse.praguePub.gui.komponenty.Tabulka;
import cz.vse.praguePub.gui.obrazovky.abstraktniObrazovky.Obrazovka;
import cz.vse.praguePub.gui.obrazovky.abstraktniObrazovky.OknoSeSeznamemPodniku;
import cz.vse.praguePub.logika.Databaze;
import cz.vse.praguePub.logika.dbObjekty.Podnik;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import lombok.Data;

import static cz.vse.praguePub.gui.komponenty.Komponenty.HorniPanel;
import static cz.vse.praguePub.gui.komponenty.Komponenty.TlacitkoAplikace;

public class PodnikyVMestskeCastiObrazovka extends OknoSeSeznamemPodniku {

    private final int cisloMestskeCasti;
    private final ObrazovkyController controller;
    private final Databaze databaze;

    public PodnikyVMestskeCastiObrazovka(int cisloMestskeCasti, ObrazovkyController controller) {
        this.cisloMestskeCasti = cisloMestskeCasti;
        this.controller = controller;
        this.databaze = controller.getDatabaze();

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

            Button pridatNovyPodnik = TlacitkoAplikace("Pridat novy podnik", (t)->{});
            Button filtrovat = TlacitkoAplikace("Filtrovat", (t)->{});

            horniPanel.getChildren().addAll(nazevLokace, pridatNovyPodnik, filtrovat);
        });
    }
}
