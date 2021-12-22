package cz.vse.praguePub.logika;

import cz.vse.praguePub.logika.dbObjekty.Pivo;
import cz.vse.praguePub.logika.dbObjekty.Podnik;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import java.util.Set;

public interface Databaze {

    /**
     * Připojení k MongoDB databázi
     * @param uzivatel instance uživatele, který se k databázi chce připojit
     * @return instanci databáze
     */
    static DatabazeImpl get(Uzivatel uzivatel) {
        return new DatabazeImpl(uzivatel);
    }

    Set<Podnik> getPodniky(Bson filter);

    /**
     * Metoda s logikou založení nového podniku. Vrací výsledek, který může znamenat, že podnik byl úspěšně vytvořen, nebo
     * že založení potřebuje další vstup uživatele. (více v třídě {@link cz.vse.praguePub.logika.Vysledek Vysledek})
     * @param novyPodnik instance podniku, který se má založit
     * @return výsledek s možností dalšího vstupu nebo s informací o úspěšném vložení
     */
    Vysledek<Podnik> zalozNovyPodnik(Podnik novyPodnik);
}
