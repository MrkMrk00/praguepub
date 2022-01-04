package cz.vse.praguePub.logika;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import cz.vse.praguePub.logika.dbObjekty.DBObjekt;
import cz.vse.praguePub.logika.dbObjekty.Pivo;
import cz.vse.praguePub.logika.dbObjekty.Podnik;
import cz.vse.praguePub.logika.dbObjekty.Recenze;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static com.mongodb.client.model.Filters.*;

public class DatabazeImpl implements Databaze {
    private static final Logger LOGGER = LoggerFactory.getLogger(DatabazeImpl.class);

    private final Uzivatel uzivatel;
    private final MongoDatabase db;


    DatabazeImpl(Uzivatel uzivatel) {
        this.uzivatel = uzivatel;
        this.db = uzivatel.getPraguePubDatabaze();
    }

    /*
    * ========================================================================
    *                   Část se "select" dotazy.
    * ========================================================================
     */

    @Override
    public Set<Podnik> getPodniky(Bson filter) {
        Iterable<Document> agregovano = this.db.getCollection("podniky")
                .find(filter);
        return this.prevedNalezenePodnikyNaInstance(agregovano);
    }

    public Set<Podnik> filtrujPodnikyPodleHodnoceni(Set<Podnik> podniky, double hodnoceni) {
        return podniky.stream().filter(podnik -> podnik.getPrumerneHodnoceni() >= hodnoceni).collect(Collectors.toSet());
    }

    @Override
    public Set<Pivo> getPiva(Bson filter) {
        return this.prevedNalezenaPivaNaInstance(this.db.getCollection("piva").find(filter));
    }

    @Override
    public MongoCollection<Document> getPivaCollection() {
        return this.db.getCollection("piva");
    }

    private Set<Podnik> prevedNalezenePodnikyNaInstance(Iterable<Document> nalezenePodniky) {
        Set<Podnik> vratit = new HashSet<>();
        nalezenePodniky.forEach(podnikDoc ->
                vratit.add(Podnik.inicializujZDokumentu(podnikDoc, this.db.getCollection("piva")))
        );
        return vratit;
    }

    private Set<Pivo> prevedNalezenaPivaNaInstance(Iterable<Document> nalezenaPiva) {
        Set<Pivo> vratit = new HashSet<>();
        nalezenaPiva.forEach(pivoDoc ->
                vratit.add(Pivo.inicializujZDokumentu(pivoDoc, null, null))
        );
        return vratit;
    }


    /*
     * ========================================================================
     *                   Část s "insert" dotazy.
     * ========================================================================
     */

    @Override
    public Vysledek<Podnik> zalozNovyPodnik(Podnik novyPodnik) {
        //Vyhledá podniky ve stejné městské části se stejným jménem
        var dbQueryVysledekJmeno = this.db.getCollection("podniky").find(
                and(
                        eq("nazev", novyPodnik.getNazev()),
                        eq("adresa.mc_cislo", novyPodnik.getAdresa_mc_cislo())
                )
        );
        List<Podnik> seStejnymNazvem = List.copyOf(this.prevedNalezenePodnikyNaInstance(dbQueryVysledekJmeno));

        //Vyhledá podniky podle ulice a čísla popisného
        var dbQueryVysledekAdresa = this.db.getCollection("podniky").find(
                and(
                        eq("adresa.ulice", novyPodnik.getAdresa_ulice()),
                        eq("adresa.cp", novyPodnik.getAdresa_cp()),
                        eq("adresa.mc_cislo", novyPodnik.getAdresa_mc_cislo())
                )
        );
        List<Podnik> seStejnouAdresou = List.copyOf(this.prevedNalezenePodnikyNaInstance(dbQueryVysledekAdresa));


        if (!seStejnymNazvem.isEmpty())
            return this.PODOBNY_OBJEKT(novyPodnik, seStejnymNazvem.get(0), TypVysledku.STEJNY_NAZEV, null);

        else if (!seStejnouAdresou.isEmpty())
            return this.PODOBNY_OBJEKT(novyPodnik, seStejnouAdresou.get(0), TypVysledku.STEJNA_ADRESA, null);

        else return (this.uploadni(novyPodnik) ? this.OK(novyPodnik) : this.CHYBA(novyPodnik));
    }

    @Override
    public Vysledek<Pivo> vytvorNovePivo(Pivo pivo) {
        var dbQuery = this.db.getCollection("piva").find(
                and(
                        eq("nazev", pivo.getNazev()),
                        eq("stupnovitost", pivo.getStupnovitost())
                )
        );
        List<Pivo> nalezene = List.copyOf(this.prevedNalezenaPivaNaInstance(dbQuery));
        if (nalezene != null && !nalezene.isEmpty()) return
                this.PODOBNY_OBJEKT(
                        pivo,
                        nalezene.get(0),
                        TypVysledku.STEJNY_NAZEV,
                        "Bylo nalezeno pivo se stejným názevem a stejnou stupňovitostí"
                );

        return (this.uploadni(pivo)) ? this.OK(pivo) : this.CHYBA(pivo);
    }

    /*
     * ========================================================================
     *                   Část s "update" dotazy.
     * ========================================================================
     */



    /**
     * Metoda nahraje objekt do databáze (finální krok)
     * @param objekt databázový objekt, který se má nahrát do databáze
     * @param <T> typ DBObjektu (objekty se nahrávají do jiných kolekcí v DB)
     * @return výsledek z databáze (úspěšně nahráno / chyba)
     */
    private <T extends DBObjekt> boolean uploadni(T objekt) {
        String nazevKolekce = "";
        Class<? extends DBObjekt> typ = objekt.getClass();

        if (typ.equals(Podnik.class)) nazevKolekce = "podniky";
        else if (typ.equals(Pivo.class)) nazevKolekce = "piva";

        return this.db.getCollection(nazevKolekce).insertOne(objekt.getDocument()).wasAcknowledged();
    }

    /**
     * Metoda je volána v případě nalezeného podniku v databázi, který je stejný (podobný) tomu, který je do databáze nahráván.<br>
     * Do metody {@link cz.vse.praguePub.logika.Vysledek#pokracovat pokracovat} u Vysledku se zabalí nahrání objektu do databáze, jelikož
     * jediným problémem je podobnost objektů.
     * @param objektDotazovany objekt vytvořený uživatelem, který má být nahrán do databáze
     * @param objektNajity objekt, který je již v databázi a je podobný tomu, který se má nahrát
     * @param typVysledku v čem je objekt podobný ({@link cz.vse.praguePub.logika.TypVysledku typ výsledku})
     * @param <T> typ DBObjektu
     * @return výsledek s {@link cz.vse.praguePub.logika.TypVysledku#OK OK} nebo {@link cz.vse.praguePub.logika.TypVysledku#DB_CHYBA DB_CHYBA}
     */
    private <T extends DBObjekt> Vysledek<T> PODOBNY_OBJEKT(T objektDotazovany, T objektNajity, TypVysledku typVysledku, String zprava) {
        Supplier<Vysledek<T>> nahraj = () -> {
            boolean byloPrijato = this.uploadni(objektDotazovany);

            if (byloPrijato) return this.OK(objektDotazovany);
            return this.CHYBA(objektDotazovany);
        };
        String zpravaDoVysledku = (zprava == null) ? "Nalezen podobný objekt" : zprava;

        Vysledek<T> kVraceni = new Vysledek<>(
                objektDotazovany,
                objektNajity,
                typVysledku,
                zpravaDoVysledku,
                nahraj
        );

        LOGGER.debug(kVraceni.toString());

        return kVraceni;
    }

    /**
     * @param dotazovany objekt nahrávaný do databáze
     * @param <T> typ DBObjektu
     * @return výsledek s typem OK
     */
    private <T extends DBObjekt> Vysledek<T> OK(T dotazovany) {
        return new Vysledek<>(
                dotazovany, null, TypVysledku.OK,"OK", null
        );
    }

    /**
     * @param dotazovany objekt nahrávaný do databáze
     * @param <T> typ DBObjektu
     * @return výsledek s typek DB_CHYBA
     */
    private <T extends DBObjekt> Vysledek<T> CHYBA(T dotazovany) {
        return new Vysledek<>(
                dotazovany, null, TypVysledku.DB_CHYBA, "Chyba databáze", null
        );
    }
}
