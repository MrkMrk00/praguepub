package cz.vse.praguePub.gui.obrazovky;

import cz.vse.praguePub.gui.komponenty.Tabulka;
import cz.vse.praguePub.gui.obrazovky.abstraktniObrazovky.Obrazovka;
import cz.vse.praguePub.gui.obrazovky.abstraktniObrazovky.OknoSeSeznamemPodniku;
import cz.vse.praguePub.logika.dbObjekty.Podnik;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import static cz.vse.praguePub.gui.komponenty.Komponenty.HorniPanel;
import static cz.vse.praguePub.gui.komponenty.Komponenty.TlacitkoAplikace;

public class PodnikyVMestskeCastiObrazovka extends OknoSeSeznamemPodniku {

    private final int cisloMestskeCasti;

    public PodnikyVMestskeCastiObrazovka(int cisloMestskeCasti) {
        this.cisloMestskeCasti = cisloMestskeCasti;
        super.vytvorGUI();
    }

    @Override
    protected ContextMenu pripravContextoveMenu(Tabulka<Podnik> tabulka) {
        return null;
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
