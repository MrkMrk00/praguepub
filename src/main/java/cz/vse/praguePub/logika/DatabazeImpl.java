package cz.vse.praguePub.logika;

import com.mongodb.client.MongoDatabase;
import cz.vse.praguePub.logika.dbObjekty.DBObjekt;
import cz.vse.praguePub.logika.dbObjekty.Pivo;
import cz.vse.praguePub.logika.dbObjekty.Podnik;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.*;
import java.util.function.Supplier;

import static com.mongodb.client.model.Filters.*;

public class DatabazeImpl implements Databaze {
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
    public Set<Podnik> getPodnikyVMestskeCasti(int mestskaCast) {
        var podnikyDB = this.db.getCollection("podniky")
                .find(eq("adresa.mc_cislo", mestskaCast));

        return this.prevedNalezeneNaInstance(podnikyDB);
    }

    @Override
    public Set<Podnik> getPodnikyPodlePiva(Pivo pivo) {
        Document pivoDB = this.db.getCollection("piva").find(pivo.getDocument()).first();
        if (pivoDB == null) return null;
        return this.getPodnikyPodleIDPiva(pivoDB.getObjectId("_id"));
    }

    @Override
    public Set<Podnik> getPodnikyPodleCenyPiva(double min, double max) {
        var podnikyDB = this.db.getCollection("podniky").find(
                and(lt("piva.cena", max), gt("piva.cena", min))
        );

        return this.prevedNalezeneNaInstance(podnikyDB);
    }

    @Override
    public Set<Podnik> getPodnikyPodleIDPiva(ObjectId pivoID) {
        Set<Podnik> vratit = new HashSet<>();
        Document query = new Document("piva", new Document("$in", List.of(pivoID)));

        this.db.getCollection("podniky").find(query)
                .forEach(doc -> vratit.add(
                            Podnik.inicializujZDokumentu(doc, this.db.getCollection("piva")
                        )
                ));
        return vratit;
    }

    @Override
    public Set<Podnik> getPodnikyPodleNazvu(String nazev) {
        Set<Podnik> vratit = new HashSet<>();
        var podnikyDB = this.db.getCollection("podniky")
                .find(new Document("jmeno", nazev));

        for (Document podnikDoc : podnikyDB)
            vratit.add(Podnik.inicializujZDokumentu(podnikDoc, this.db.getCollection("piva")));

        return vratit;
    }

    private Set<Podnik> prevedNalezeneNaInstance(Iterable<Document> nalezenePodniky) {
        Set<Podnik> vratit = new HashSet<>();
        nalezenePodniky.forEach(podnikDoc ->
                vratit.add(Podnik.inicializujZDokumentu(podnikDoc, this.db.getCollection("piva")))
        );
        return vratit;
    }

    /*
     * ========================================================================
     *                   Část se "insert" dotazy.
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
        List<Podnik> seStejnymNazvem = List.copyOf(this.prevedNalezeneNaInstance(dbQueryVysledekJmeno));

        //Vyhledá podniky podle ulice a čísla popisného
        var dbQueryVysledekAdresa = this.db.getCollection("podniky").find(
                and(
                        eq("adresa.ulice", novyPodnik.getAdresa_ulice()),
                        eq("adresa.cp", novyPodnik.getAdresa_cp()),
                        eq("adresa.mc_cislo", novyPodnik.getAdresa_mc_cislo())
                )
        );
        List<Podnik> seStejnouAdresou = List.copyOf(this.prevedNalezeneNaInstance(dbQueryVysledekAdresa));


        if (!seStejnymNazvem.isEmpty())
            return this.nalezenPodobnyObjekt(novyPodnik, seStejnymNazvem.get(0), TypVysledku.STEJNY_NAZEV_A_MC);

        else if (!seStejnouAdresou.isEmpty())
            return this.nalezenPodobnyObjekt(novyPodnik, seStejnouAdresou.get(0), TypVysledku.STEJNA_ADRESA);

        else return (this.uploadni(novyPodnik) ? this.OK(novyPodnik) : this.CHYBA(novyPodnik));
    }

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
    private <T extends DBObjekt> Vysledek<T> nalezenPodobnyObjekt(T objektDotazovany, T objektNajity, TypVysledku typVysledku) {
        Supplier<Vysledek<T>> nahraj = () -> {
            boolean byloPrijato = this.uploadni(objektDotazovany);

            if (byloPrijato) return this.OK(objektDotazovany);
            return this.CHYBA(objektDotazovany);
        };

        return new Vysledek<>(
                objektDotazovany,
                objektNajity,
                typVysledku,
                nahraj
        );
    }

    /**
     * @param dotazovany objekt nahrávaný do databáze
     * @param <T> typ DBObjektu
     * @return výsledek s typem OK
     */
    private <T extends DBObjekt> Vysledek<T> OK(T dotazovany) {
        return new Vysledek<>(
                dotazovany, null, TypVysledku.OK, null
        );
    }

    /**
     * @param dotazovany objekt nahrávaný do databáze
     * @param <T> typ DBObjektu
     * @return výsledek s typek DB_CHYBA
     */
    private <T extends DBObjekt> Vysledek<T> CHYBA(T dotazovany) {
        return new Vysledek<>(
                dotazovany, null, TypVysledku.DB_CHYBA, null
        );
    }
}
