package cz.vse.praguePub.logika;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.UpdateOptions;
import com.mongodb.client.model.Updates;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import cz.vse.praguePub.logika.dbObjekty.DBObjekt;
import cz.vse.praguePub.logika.dbObjekty.Pivo;
import cz.vse.praguePub.logika.dbObjekty.Podnik;
import cz.vse.praguePub.util.PraguePubDatabaseException;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import static com.mongodb.client.model.Filters.*;

public class DatabazeImpl implements Databaze {
    private static final Logger log = LoggerFactory.getLogger(DatabazeImpl.class);

    private final Uzivatel uzivatel;
    private final MongoDatabase db;

    DatabazeImpl(Uzivatel uzivatel) throws PraguePubDatabaseException {
        this.uzivatel = uzivatel;
        if (uzivatel == null) {
            throw new PraguePubDatabaseException("Nelze se připojit do databáze jako host");
        }
        this.db = uzivatel.getPraguePubDatabaze();
    }

    @Override
    public MongoCollection<Document> getPivaCollection() {
        return this.db.getCollection("piva");
    }

    @Override
    public MongoCollection<Document> getPodnikyCollection() {
        return this.db.getCollection("podniky");
    }

    @Override
    public Uzivatel getUzivatel() {
        return this.uzivatel;
    }

    @Override
    public String getUzivatelskeJmeno(ObjectId idUzivatele) {
        Document nalezenyUzivatel = this.db.getCollection("uzivatele").find(eq("_id", idUzivatele)).first();
        if (nalezenyUzivatel == null || !nalezenyUzivatel.containsKey("userName")) return null;
        return nalezenyUzivatel.getString("userName");
    }

    @Override
    public boolean pridejDoOblibenych(Podnik podnik) {
        UpdateResult updateResult = this.db.getCollection("oblibene_podniky").updateOne(
                eq("_id", this.uzivatel.get_id()),
                Updates.push("podniky", podnik.get_id()),
                new UpdateOptions().upsert(true)
        );

        boolean vysledek = (updateResult.wasAcknowledged() && updateResult.getModifiedCount() > 0);
        if (vysledek) log.info("Úspěšně přidán podnik do oblíbených - " + podnik.getNazev());
        else log.error("Nepodařilo se přidat podnik do oblíbených - " + podnik.getNazev());

        return vysledek;
    }

    @Override
    public boolean odeberZOblibenych(Podnik podnik) {
        UpdateResult updateResult = this.db.getCollection("oblibene_podniky").updateOne(
                eq("_id", this.uzivatel.get_id()),
                Updates.pull("podniky", podnik.get_id())
        );

        boolean vysledek = (updateResult.wasAcknowledged() && updateResult.getModifiedCount() > 0);
        if (vysledek) log.info("Úspěšně odebrán podnik z oblíbených - " + podnik.getNazev());
        else log.error("Nepodařilo se odebrat podnik z oblíbených - " + podnik.getNazev());

        return vysledek;
    }

    @Override
    public boolean jeVOblibenych(Podnik podnik) {
        Document oblPodniky = this.db.getCollection("oblibene_podniky")
                .find(eq("_id", this.uzivatel.get_id())).first();
        if (oblPodniky == null || oblPodniky.isEmpty()) {
            log.error("Nepodařilo se dostat k oblíbeným podnikům uživatele");
            return false;
        }

        if (!oblPodniky.containsKey("podniky")) return false;

        List<ObjectId> idOblibenychPodnikuUzivatele = oblPodniky.getList("podniky", ObjectId.class);
        for (ObjectId id : idOblibenychPodnikuUzivatele) {
            if (podnik.get_id().equals(id)) return true;
        }

        return false;
    }

    @Override
    public List<Podnik> getOblibenePodniky() {
        Document userDoc = this.db.getCollection("oblibene_podniky")
                .find(eq("_id", this.uzivatel.get_id()))
                .first();

        if (userDoc == null || userDoc.isEmpty()) return List.of();

        List<ObjectId> idList = userDoc.getList("podniky", ObjectId.class);
        return this.prevedNalezenePodnikyNaInstance(
                this.getPodnikyCollection()
                        .find(in("_id", idList))
        );
    }

    @Override
    public PivoFiltrBuilder getPivoFilterBuilder() {
        return new PivoFiltrBuilder(this.getPivaCollection());
    }

    @Override
    public PodnikFiltrBuilder getPodnikFiltrBuilder() {
        return new PodnikFiltrBuilder(this.getPodnikyCollection(), this.getPivaCollection());
    }

    private List<Podnik> prevedNalezenePodnikyNaInstance(Iterable<Document> nalezenePodniky) {
        List<Podnik> vratit = new ArrayList<>();
        nalezenePodniky.forEach(podnikDoc ->
                vratit.add(Podnik.inicializujZDokumentu(podnikDoc, this.db.getCollection("piva")))
        );
        return vratit;
    }

    private List<Pivo> prevedNalezenaPivaNaInstance(Iterable<Document> nalezenaPiva) {
        List<Pivo> vratit = new ArrayList<>();
        nalezenaPiva.forEach(pivoDoc ->
                vratit.add(Pivo.inicializujZDokumentu(pivoDoc, null, null))
        );
        return vratit;
    }


    @Override
    public Vysledek<Podnik> zalozNovyPodnik(Podnik novyPodnik) {
        //Vyhledá podniky ve stejné městské části se stejným jménem
        var dbQueryVysledekJmeno = this.db.getCollection("podniky").find(
                and(
                        regex("nazev", novyPodnik.getNazev(), "i"),
                        eq("adresa.mc_cislo", novyPodnik.getAdresa_mc_cislo())
                )
        );
        List<Podnik> seStejnymNazvem = this.prevedNalezenePodnikyNaInstance(dbQueryVysledekJmeno);

        //Vyhledá podniky podle ulice a čísla popisného
        var dbQueryVysledekAdresa = this.db.getCollection("podniky").find(
                and(
                        regex("adresa.ulice", novyPodnik.getAdresa_ulice(), "i"),
                        regex("adresa.cp", "^" + novyPodnik.getAdresa_cp(), "i"),
                        eq("adresa.mc_cislo", novyPodnik.getAdresa_mc_cislo())
                )
        );
        List<Podnik> seStejnouAdresou = this.prevedNalezenePodnikyNaInstance(dbQueryVysledekAdresa);


        if (!seStejnymNazvem.isEmpty())
            return this.PODOBNY_OBJEKT(novyPodnik, seStejnymNazvem.get(0), TypVysledku.STEJNY_NAZEV, null);

        else if (!seStejnouAdresou.isEmpty())
            return this.PODOBNY_OBJEKT(novyPodnik, seStejnouAdresou.get(0), TypVysledku.STEJNA_ADRESA, null);

        else return (this.uploadni(novyPodnik) ? this.OK(novyPodnik) : this.CHYBA(novyPodnik));
    }

    @Override
    public Vysledek<Podnik> upravPodnik(Podnik upravenyPodnik) {
        UpdateResult vysledek = this.getPodnikyCollection()
                .updateOne(
                        eq("_id", upravenyPodnik.get_id()),
                        Document.parse("{ $set: " + upravenyPodnik.getDocument().toJson() + " }")
                );

        if (!vysledek.wasAcknowledged()) return CHYBA(upravenyPodnik);
        if (vysledek.getModifiedCount() < 1) return ZADNA_ZMENA(upravenyPodnik);
        return OK(upravenyPodnik);
    }

    @Override
    public Vysledek<Podnik> vymazPodnik(Podnik podnikKVymazani) {
        DeleteResult vysledek = this.getPodnikyCollection().deleteOne(
                eq("_id", podnikKVymazani.get_id())
        );

        if (!vysledek.wasAcknowledged()) return CHYBA(podnikKVymazani);
        if (vysledek.getDeletedCount() < 1) return ZADNA_ZMENA(podnikKVymazani);
        return OK(podnikKVymazani);
    }

    @Override
    public Vysledek<Pivo> vytvorNovePivo(Pivo pivo) {
        FindIterable<Document> dbQuery = this.db.getCollection("piva").find(
                and(
                        regex("nazev", pivo.getNazev(), "i"),
                        eq("stupnovitost", pivo.getStupnovitost())
                )
        );
        List<Pivo> nalezene = this.prevedNalezenaPivaNaInstance(dbQuery);
        if (!nalezene.isEmpty()) return
                this.PODOBNY_OBJEKT(
                        pivo,
                        nalezene.get(0),
                        TypVysledku.STEJNY_NAZEV,
                        "Bylo nalezeno pivo se stejným názevem a stejnou stupňovitostí"
                );

        return (this.uploadni(pivo)) ? this.OK(pivo) : this.CHYBA(pivo);
    }

    @Override
    public Vysledek<Pivo> vymazPivo(Pivo pivo) {
        Document pivoDoc = this.getPivaCollection().find(eq("_id", pivo.get_id())).first();
        if (pivoDoc == null) return this.CHYBA(pivo);

        FindIterable<Document> nalezenePodnikyDoc = this.getPodnikyCollection()
                .find(
                        elemMatch("piva", eq("pivo", pivo.get_id()) )
                );

        List<Podnik> nalezenePodniky = this.prevedNalezenePodnikyNaInstance(nalezenePodnikyDoc);
        if (!nalezenePodniky.isEmpty()) return new Vysledek<>(
                pivo, pivo, TypVysledku.ZADNA_ZMENA, "Pivo je nabízeno alespoň jedním podnikem", null
        );

        DeleteResult mongoVysledek = this.getPivaCollection().deleteOne(pivo.getDocument());
        if (!mongoVysledek.wasAcknowledged() || mongoVysledek.getDeletedCount() == 0) return this.CHYBA(pivo);

        return this.OK(pivo);
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

        log.debug(kVraceni.toString());

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
     * @return výsledek s typem DB_CHYBA
     */
    private <T extends DBObjekt> Vysledek<T> CHYBA(T dotazovany) {
        return new Vysledek<>(
                dotazovany, null, TypVysledku.DB_CHYBA, "Chyba databáze", null
        );
    }

    /**
     * @param dotazovany objekt, který byl do databáze nahráván
     * @return výsledek s typem ZADNA_ZMENA
     */
    private <T extends DBObjekt> Vysledek<T> ZADNA_ZMENA(T dotazovany) {
        return new Vysledek<>(
                dotazovany, null, TypVysledku.ZADNA_ZMENA, "Nestala se zadna zmena", null
        );
    }
}
