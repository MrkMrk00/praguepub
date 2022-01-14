package cz.vse.praguePub.gui.obrazovky;

import cz.vse.praguePub.gui.ObrazovkyController;
import cz.vse.praguePub.logika.dbObjekty.Podnik;

/**
 * Třída reprezentuje GUI přidání nového podniku do databáze.
 */
public class PridejPodnikObrazovka extends UpravitPodnikObrazovka {

    public PridejPodnikObrazovka(ObrazovkyController controller) {
        super(controller, new Podnik());
    }
}
