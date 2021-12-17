package cz.vse.praguePub.logika;

import cz.vse.praguePub.logika.dbObjekty.Pivo;
import cz.vse.praguePub.logika.dbObjekty.Podnik;
import org.bson.types.ObjectId;

import java.util.Set;

public interface IDatabaze {

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
     * Vrátí všechny podniky, které nabízí pivo s daným ID.
     * <p>
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
}
