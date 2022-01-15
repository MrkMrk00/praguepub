package cz.vse.praguePub.gui.obrazovky;

import cz.vse.praguePub.gui.ObrazovkyController;
import cz.vse.praguePub.gui.obrazovky.abstraktniObrazovky.PodnikInfoObrazovka;
import cz.vse.praguePub.logika.Vysledek;
import cz.vse.praguePub.logika.dbObjekty.Podnik;
import javafx.scene.control.ButtonType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

import static cz.vse.praguePub.gui.komponenty.AlertBuilder.*;

/**
 * Třída reprezentuje GUI přidání nového podniku do databáze.
 */
public class PridejPodnikObrazovka extends PodnikInfoObrazovka {
    private static final Logger log = LoggerFactory.getLogger(PridejPodnikObrazovka.class);

    public PridejPodnikObrazovka(ObrazovkyController controller, Integer cisloMC) {
        super(controller, cisloMC, "Přidej nový podnik");
    }

    private void zobrazOpravduPokracovat(Vysledek<Podnik> vysledek) {
        Optional<ButtonType> alertResultOpt = OpravduPokracovatAlert(vysledek).showAndWait();

        if (alertResultOpt.isEmpty() || alertResultOpt.get() == ButtonType.CANCEL) return;
        if (alertResultOpt.get() == ButtonType.OK) vysledek.pokracuj();
    }

    @Override
    protected void odeslat() {
        if (!this.nastavHodnoty()) return;

        Vysledek<Podnik> vysledek = this.db.zalozNovyPodnik(this.podnik);
        log.debug(vysledek.toString());

        switch (vysledek.getTypVysledku()) {
            case DB_CHYBA -> ChybaDatabazeAlert(vysledek.getZprava()).show();
            case OK -> UspesneVlozenObjekt(vysledek.getDotazovany()).show();
            case STEJNY_NAZEV, STEJNA_ADRESA -> this.zobrazOpravduPokracovat(vysledek);
        }
    }
}
