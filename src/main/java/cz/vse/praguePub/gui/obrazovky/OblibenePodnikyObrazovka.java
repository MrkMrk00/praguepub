package cz.vse.praguePub.gui.obrazovky;

import cz.vse.praguePub.gui.ObrazovkyController;
import cz.vse.praguePub.gui.komponenty.Tabulka;
import cz.vse.praguePub.gui.obrazovky.abstraktniObrazovky.OknoSeSeznamemPodniku;
import cz.vse.praguePub.logika.Databaze;
import cz.vse.praguePub.logika.dbObjekty.Podnik;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;


import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

import static cz.vse.praguePub.gui.komponenty.Komponenty.HorniPanel;
import static cz.vse.praguePub.gui.komponenty.Komponenty.NadpisOknaLabel;

public class OblibenePodnikyObrazovka extends OknoSeSeznamemPodniku {

    private final ObrazovkyController controller;
    private final Databaze databaze;

    public OblibenePodnikyObrazovka(ObrazovkyController controller) {
        this.controller = controller;
        this.databaze = controller.getDatabaze();

        this.nactiPodniky();
        super.vytvorGUI();
    }

    private void nactiPodniky() {
        this.getZobrazenePodniky().clear();
        this.getZobrazenePodniky().addAll(this.databaze.getOblibenePodniky());
    }

    @Override
    protected HBox pripravHorniPanel() {
        return HorniPanel(
                hp -> {
                    hp.getChildren().add(NadpisOknaLabel("Oblíbené podniky"));
                }
        );
    }


    @Override
    protected ContextMenu pripravContextoveMenu(Tabulka<Podnik> tabulka) {
        TableView<Podnik> tv = tabulka.getTableView();

        MenuItem odebratMenuItem = new MenuItem("Odebrat z oblíbených");
        odebratMenuItem.setOnAction(
                event -> {
                    this.databaze.odeberZOblibenych(tv.getSelectionModel().getSelectedItem());
                    this.nactiPodniky();
                }
        );

        MenuItem upravitMenuItem = new MenuItem("Upravit podnik");
        upravitMenuItem.setOnAction(
                event -> this.controller.zobrazUpraveniPodniku(tv.getSelectionModel().getSelectedItem())
        );

        MenuItem zrusitMenuItem = new MenuItem("Zrušit");

        return new ContextMenu(
                upravitMenuItem,
                new SeparatorMenuItem(),
                odebratMenuItem,
                zrusitMenuItem
        );
    }


}
