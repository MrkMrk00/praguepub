package cz.vse.praguePub.gui.obrazovky;

import cz.vse.praguePub.gui.ObrazovkyController;
import cz.vse.praguePub.gui.obrazovky.abstraktniObrazovky.PodnikInfoObrazovka;
import cz.vse.praguePub.logika.dbObjekty.Podnik;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Třída reprezentuje GUI přidání nového podniku do databáze.
 */
public class UpravitPodnikObrazovka extends PodnikInfoObrazovka {
    private static final Logger log = LoggerFactory.getLogger(UpravitPodnikObrazovka.class);

    public UpravitPodnikObrazovka(ObrazovkyController controller, Podnik podnik) {
        super(controller, podnik, "Uprav podnik");
    }


    @Override
    protected void odeslat() {

    }
}

