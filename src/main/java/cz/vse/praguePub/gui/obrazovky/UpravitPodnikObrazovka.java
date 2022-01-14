package cz.vse.praguePub.gui.obrazovky;

import cz.vse.praguePub.gui.ObrazovkyController;
import cz.vse.praguePub.gui.obrazovky.abstraktniObrazovky.PodnikInfoObrazovka;
import cz.vse.praguePub.logika.dbObjekty.Podnik;

/**
 * Třída reprezentuje GUI přidání nového podniku do databáze.
 */
public class UpravitPodnikObrazovka extends PodnikInfoObrazovka {

    public UpravitPodnikObrazovka(ObrazovkyController controller, Podnik podnik) {
        super(controller, podnik, "Uprav podnik");
    }

    @Override
    protected void odeslat() {

    }
}

