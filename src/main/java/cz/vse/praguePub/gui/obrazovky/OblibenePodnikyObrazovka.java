package cz.vse.praguePub.gui.obrazovky;

import cz.vse.praguePub.gui.ObrazovkyController;
import cz.vse.praguePub.gui.komponenty.Tabulka;
import cz.vse.praguePub.gui.obrazovky.abstraktniObrazovky.OknoSeSeznamemPodniku;
import cz.vse.praguePub.logika.Databaze;
import cz.vse.praguePub.logika.dbObjekty.Podnik;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.TableView;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import static cz.vse.praguePub.gui.komponenty.Komponenty.HorniPanel;
import static cz.vse.praguePub.gui.komponenty.Komponenty.NadpisOknaLabel;

public class OblibenePodnikyObrazovka extends OknoSeSeznamemPodniku {

    private final Stage stage;

    private final ObrazovkyController controller;
    private final Databaze databaze;

    public OblibenePodnikyObrazovka(ObrazovkyController controller, Stage stage) {
        this.controller = controller;
        this.databaze = controller.getDatabaze();
        this.stage = stage;

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
    protected ContextMenu pripravKontextoveMenuAUpravTabulku(Tabulka<Podnik> tabulka) {
        TableView<Podnik> tv = tabulka.getTableView();

        Runnable zobrazInformaceOPodniku = () -> {
            if (tabulka.getTableView().getItems().isEmpty()) return;

            this.controller.zobrazInformaceOPodniku(
                    tabulka.getTableView().getSelectionModel().getSelectedItem(),
                    this.stage,
                    this.getScene()
            );
        };

        tv.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) zobrazInformaceOPodniku.run();
        });

        MenuItem odebratMenuItem = new MenuItem("Odebrat z oblíbených");
        odebratMenuItem.setOnAction(
                event -> {
                    this.databaze.odeberZOblibenych(tv.getSelectionModel().getSelectedItem());
                    this.nactiPodniky();
                }
        );

        MenuItem upravitMenuItem = new MenuItem("Zobrazit podnik");
        upravitMenuItem.setOnAction(event -> zobrazInformaceOPodniku.run());

        MenuItem zrusitMenuItem = new MenuItem("Zrušit");

        return new ContextMenu(
                upravitMenuItem,
                new SeparatorMenuItem(),
                odebratMenuItem,
                zrusitMenuItem
        );
    }


}
