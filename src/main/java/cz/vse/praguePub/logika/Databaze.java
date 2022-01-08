package cz.vse.praguePub.logika;

import com.mongodb.client.MongoCollection;
import cz.vse.praguePub.logika.dbObjekty.Pivo;
import cz.vse.praguePub.logika.dbObjekty.Podnik;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import java.util.Set;

public interface Databaze {

    /**
     * Připojení k MongoDB databázi
     * @param uzivatel instance uživatele, který se k databázi chce připojit
     * @return instanci databáze
     */
    static Databaze get(Uzivatel uzivatel) {
        return new DatabazeImpl(uzivatel);
    }

    /**
     * Vytvoří instanci builderu pro filtrování piv z databáze.
     * @return PivoFiltrBuilder
     */
    PivoFiltrBuilder getPivoFilterBuilder();

    /**
     * Vytvoří instanci builderu pro filtrování podniků z databáze.
     * @return PodnikFiltrBuilder
     */
    PodnikFiltrBuilder getPodnikFiltrBuilder();

    /**
     * @return databázi piv
     */
    MongoCollection<Document> getPivaCollection();

    /**
     * @return databázi podniků
     */
    MongoCollection<Document> getPodnikyCollection();

    /**
     * Metoda s logikou založení nového podniku. Vrací výsledek, který může znamenat, že podnik byl úspěšně vytvořen, nebo
     * že založení potřebuje další vstup uživatele. (více v třídě {@link cz.vse.praguePub.logika.Vysledek Vysledek})
     * @param novyPodnik instance podniku, který se má založit
     * @return výsledek s možností dalšího vstupu nebo s informací o úspěšném vložení
     */
    Vysledek<Podnik> zalozNovyPodnik(Podnik novyPodnik);

    /**
     * Metoda s logikou úpravy podniku. Instance podniku v sobě obsahuje parametr _id, tzn. porovnají objekty v databázi a
     * v argumentu funkce a podnik v databázi se přepíše úpravami podniku v argumentu.
     * @param upravenyPodnik podnik z databáze se změnami
     * @return výsledek (OK, ZADNA_ZMENA, CHYBA)
     */
    Vysledek<Podnik> upravPodnik(Podnik upravenyPodnik);

    /**
     * Metoda vymaže podnik z databáze. Podnik, který se má smazat, je v databázi vyhledán podle _id. Nezáleží tedy na jiných
     * instančních atributech argumentu podnikKVymazani.
     * @param podnikKVymazani podnik, ze kterého se vezme _id
     * @return výsledek (OK, ZADNA_ZMENA, CHYBA)
     */
    Vysledek<Podnik> vymazPodnik(Podnik podnikKVymazani);

    /**
     * Metoda pro založení nového piva do databáze. Vrací výsledek, který může znamenat, že podnik byl úspěšně vytvořen, nebo
     * že vytvoření potřebuje další vstup uživatele. (více v třídě {@link cz.vse.praguePub.logika.Vysledek Vysledek})
     * @param pivo pivo, které se vloží do databáze
     * @return výsledek s možností dalšího vstupu nebo s informací o úspěšném vložení
     */
    Vysledek<Pivo> vytvorNovePivo(Pivo pivo);
}
