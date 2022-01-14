package cz.vse.praguePub.gui.obrazovky;

import cz.vse.praguePub.gui.ObrazovkyController;
import cz.vse.praguePub.logika.dbObjekty.Podnik;
import javafx.geometry.Pos;
import javafx.scene.layout.HBox;

import static cz.vse.praguePub.gui.komponenty.Komponenty.*;

/**
 * Třída reprezentuje GUI přidání nového podniku do databáze.
 */
public class PridejPodnikObrazovka extends UpravitPodnikObrazovka {

    public PridejPodnikObrazovka(ObrazovkyController controller) {
        super(controller, new Podnik());
    }

    @Override
    protected HBox horniPanel() {
        return HorniPanel(hp -> {
            hp.getChildren().add(NadpisOknaLabel("Přidej podnik"));
            hp.setAlignment(Pos.CENTER);
        });
    }
}
