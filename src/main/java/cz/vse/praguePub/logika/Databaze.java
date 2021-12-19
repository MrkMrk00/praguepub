package cz.vse.praguePub.logika;

import cz.vse.praguePub.logika.dbObjekty.Pivo;
import cz.vse.praguePub.logika.dbObjekty.Podnik;
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

    /**
     * Vratí všechny podniky, které se nachází v dané městské části.
     * <p>
     * <strong>Vrací null v případě špatných vstupních parametrů! (např. městská část neexistuje)</strong>
     * @param mestskaCast číslo městské části ze které vrátí podniky
     * @return Set s podniky v městské části nebo <strong>null</strong>
     */
    Set<Podnik> getPodnikyVMestskeCasti(int mestskaCast);

    /**
     * Vratí všechny podniky, které nabízí dané pivo.
     * Instance piva je vyhledávána v databázi - pokud možno, použij metodu <strong>getPodnikyPodleIDPiva(ObjectId pivoID)</strong>
     * <p>
     * <strong>Vrací null v případě špatných vstupních parametrů! (např. pivo neexistuje v databázi)</strong>
     * @param pivo instance piva, podle které vyhledá v databázi podnik
     * @return Set s podniky, které nabízí dané pivo nebo <strong>null</strong>
     */
    Set<Podnik> getPodnikyPodlePiva(Pivo pivo);

    /**
     * Vyhledá podniky podle ceny piva. V případě, že nic nenajde, vrátí prázdný Set
     * @param min nejnižší hodnota ceny
     * @param max nejvyšší hodnota ceny
     * @return Set s podniky, kde je cena piva v daném intervalu
     */
    Set<Podnik> getPodnikyPodleCenyPiva(double min, double max);

    /**
     * Vrátí všechny podniky, které nabízí pivo s daným ID.
     * @param pivoID ObjectID piva, které je nabízeno hledanými podniky
     * @return Set s podniky, které nabízí dané pivo
     */
    Set<Podnik> getPodnikyPodleIDPiva(ObjectId pivoID);

    /**
     * Vyhledá podniky podle jejich názvu.
     * @param nazev název hledaného/hledaných podniků
     * @return Set s podniky s hledaným jménem
     */
    Set<Podnik> getPodnikyPodleNazvu(String nazev);

    /**
     * Metoda s logikou založení nového podniku. Vrací výsledek, který může znamenat, že podnik byl úspěšně vytvořen, nebo
     * že založení potřebuje další vstup uživatele. (více v třídě {@link cz.vse.praguePub.logika.Vysledek Vysledek})
     * @param novyPodnik instance podniku, který se má založit
     * @return výsledek s možností dalšího vstupu nebo s informací o úspěšném vložení
     */
    Vysledek<Podnik> zalozNovyPodnik(Podnik novyPodnik);
}
