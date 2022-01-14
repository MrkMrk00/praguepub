package cz.vse.praguePub.gui.obrazovky;

import cz.vse.praguePub.gui.ObrazovkyController;
import cz.vse.praguePub.gui.komponenty.AlertBuilder;
import cz.vse.praguePub.gui.obrazovky.abstraktniObrazovky.PodnikInfoObrazovka;
import cz.vse.praguePub.logika.Vysledek;
import cz.vse.praguePub.logika.dbObjekty.Podnik;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

/**
 * Třída reprezentuje GUI přidání nového podniku do databáze.
 */
public class PridejPodnikObrazovka extends PodnikInfoObrazovka {
    private static final Logger log = LoggerFactory.getLogger(PridejPodnikObrazovka.class);

    public PridejPodnikObrazovka(ObrazovkyController controller, Integer cisloMC) {
        super(controller, cisloMC, "Přidej nový podnik");
    }

    private void zobrazChybaDatabazeAlert(String chybaText) {
        new AlertBuilder(Alert.AlertType.ERROR)
                .setTitle("PraguePub")
                .setHeaderText("Chyba databáze")
                .setContent(chybaText)
                .getAlert()
                .showAndWait();
    }

    private void zobrazOpravduPokracovat(Vysledek<Podnik> vysledek) {
        Podnik podobny = vysledek.getNajity();
        String popisNajitehoPiva =
                String.format("""
                        %s
                        %s %s, Praha %d-%s
                        """,
                        podobny.getNazev(),
                        podobny.getAdresa_ulice(), podobny.getAdresa_cp(),
                        podobny.getAdresa_mc_cislo(), podobny.getAdresa_mc_nazev()
                );


        Optional<ButtonType> alertResultOpt = new AlertBuilder(Alert.AlertType.CONFIRMATION)
                .setTitle("Prague Pub")
                .setHeaderText("Opravdu chcete pokračovat?")
                .setContent(
                        vysledek.getZprava() + "\n"
                                + "Podnik v databázi:\n"
                                + popisNajitehoPiva
                )
                .getAlert().showAndWait();

        if (alertResultOpt.isEmpty() || alertResultOpt.get() == ButtonType.CANCEL) return;
        if (alertResultOpt.get() == ButtonType.OK) vysledek.pokracuj();
    }

    @Override
    protected void odeslat() {
        if (!this.nastavHodnoty()) return;

        Vysledek<Podnik> vysledek = this.db.zalozNovyPodnik(this.podnik);
        log.debug(vysledek.toString());
        switch (vysledek.getTypVysledku()) {
            case OK:
                return;

            case DB_CHYBA:
                this.zobrazChybaDatabazeAlert(vysledek.getZprava());
                break;

            case STEJNY_NAZEV:
            case STEJNA_ADRESA:
                this.zobrazOpravduPokracovat(vysledek);
                break;
        }
    }
}
