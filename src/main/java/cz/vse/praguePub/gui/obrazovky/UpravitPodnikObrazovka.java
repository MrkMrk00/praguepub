package cz.vse.praguePub.gui.obrazovky;

import cz.vse.praguePub.gui.ObrazovkyController;
import cz.vse.praguePub.gui.obrazovky.abstraktniObrazovky.PodnikInfoObrazovka;
import cz.vse.praguePub.logika.dbObjekty.Podnik;
import javafx.scene.Node;
import javafx.scene.Parent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static cz.vse.praguePub.gui.komponenty.Komponenty.*;

/**
 * Třída reprezentuje GUI přidání nového podniku do databáze.
 */
public class UpravitPodnikObrazovka extends PodnikInfoObrazovka {
    private static final Logger log = LoggerFactory.getLogger(UpravitPodnikObrazovka.class);

    private Runnable zpetCallback;

    public UpravitPodnikObrazovka(ObrazovkyController controller, Podnik podnik) {
        super(controller, podnik, "Uprav podnik");
        this.zpetCallback = null;
    }

    public UpravitPodnikObrazovka(ObrazovkyController controller, Podnik podnik, Runnable zpetCallback) {
        this(controller, podnik);
        this.zpetCallback = zpetCallback;
        this.pridejTlacitkoZpet();
    }

    private void pridejTlacitkoZpet() {
        Node topNode = this.getPane().getTop();
        if (topNode instanceof Parent horniPanel) {
            if (this.zpetCallback == null || horniPanel.getChildrenUnmodifiable().isEmpty()) return;
            Node nadpis = horniPanel.getChildrenUnmodifiable().get(0);

            this.getPane().setTop(Radek(Spacer(), nadpis, Spacer(), TlacitkoZpet(t-> this.zpetCallback.run(), t->{})));
        }
    }

    @Override
    protected void odeslat() {
    this.nastavHodnoty();
    this.controller.getDatabaze().upravPodnik(this.podnik);
    this.zpetCallback.run();
    }
}

