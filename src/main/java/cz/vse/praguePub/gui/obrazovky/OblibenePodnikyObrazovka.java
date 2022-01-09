package cz.vse.praguePub.gui.obrazovky;

import cz.vse.praguePub.gui.komponenty.Tabulka;
import cz.vse.praguePub.gui.obrazovky.abstraktniObrazovky.OknoSeSeznamemPodniku;
import cz.vse.praguePub.logika.dbObjekty.Podnik;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;


import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class OblibenePodnikyObrazovka extends OknoSeSeznamemPodniku {

    private final Supplier<List<Podnik>> ziskejPodniky;
    private final Consumer<Podnik> odeberPodnik;
    private final Consumer<Podnik> upravPodnik;

    public OblibenePodnikyObrazovka(
            Supplier<List<Podnik>> ziskejPodniky,
            Consumer<Podnik> odeberCallback,
            Consumer<Podnik> upravCallback
    ) {
        this.odeberPodnik = odeberCallback;
        this.ziskejPodniky = ziskejPodniky;
        this.upravPodnik = upravCallback;

        this.nactiPodniky();
    }

    private void nactiPodniky() {
        this.getZobrazenePodniky().clear();
        this.getZobrazenePodniky().addAll(this.ziskejPodniky.get());
    }

    @Override
    protected HBox pripravHorniPanel() {
        return null;
    }


    @Override
    protected ContextMenu pripravContextoveMenu(Tabulka<Podnik> tabulka) {
        TableView<Podnik> tv = tabulka.getTableView();

        MenuItem odebratMenuItem = new MenuItem("Odebrat z oblíbených");
        odebratMenuItem.setOnAction(
                event -> {
                    this.odeberPodnik.accept(tv.getSelectionModel().getSelectedItem());
                    this.nactiPodniky();
                }
        );

        MenuItem upravitMenuItem = new MenuItem("Upravit podnik");
        upravitMenuItem.setOnAction(
                event -> this.upravPodnik.accept(tv.getSelectionModel().getSelectedItem())
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
