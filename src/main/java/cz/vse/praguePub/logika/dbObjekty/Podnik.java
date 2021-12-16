package cz.vse.praguePub.logika.dbObjekty;

import com.mongodb.client.MongoCollection;
import lombok.Data;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.*;

@Data
public class Podnik implements DBObjekt {
    private final String nazev;

    private final int adresa_mc_cislo;
    private final String adresa_mc_nazev;
    private final String adresa_psc;
    private final String adresa_ulice;
    private final Integer adresa_cp;

    private final Set<Recenze> recenze;
    private final Map<ObjectId, Pivo> pivniListek;

    /**
     * Vytvoří instanci podniku z databázového dokumentu. <br>
     * Pro získání informací o pivu potřebuje přístup do kolekce s pivy.
     * @param doc databázový dokument
     * @param kolekcePiv kolekce piv
     * @return instanci podniku
     */
    public static Podnik inicializujZDokumentu(Document doc, MongoCollection<Document> kolekcePiv) {
        Set<Recenze> recenzeLst = new HashSet<>();
        doc.get("recenze", Document.class).forEach(
                (key, recenze) -> recenzeLst.add(Recenze.inicializujZDokumentu((Document)recenze))
        );

        Map<ObjectId, Pivo> pivniListek = new HashMap<>();
        doc.get("piva", Document.class).forEach(
                (key, pivoDocRaw) -> {
                    Document pivoDoc = (Document)pivoDocRaw;

                    ObjectId pivoID = pivoDoc.get("_id", ObjectId.class);
                    Document naleznutePivo = kolekcePiv.find(new Document("_id", pivoID)).first();

                    if (naleznutePivo != null) pivniListek.put(
                            pivoID,
                            Pivo.inicializujZDokumentu(
                                    naleznutePivo,
                                    pivoDoc.getDouble("cena"),
                                    pivoDoc.getDouble("objem")
                            )
                    );
                });

        Document adresa = doc.get("adresa", Document.class);

        return new Podnik(
                doc.get("jmeno", String.class),
                adresa.get("mc_cislo", Integer.class),
                adresa.get("mc_nazev", String.class),
                adresa.get("ulice", String.class),
                adresa.get("psc", String.class),
                adresa.get("cp", Integer.class),
                recenzeLst,
                pivniListek
        );
    }

    public Set<Recenze> getRecenze() {
        return Collections.unmodifiableSet(this.recenze);
    }

    public Map<ObjectId, Pivo> getPivniListek() {
        return Collections.unmodifiableMap(this.pivniListek);
    }

    /**
     * nefunguje<br>
     * TODO!!!!
     * @return null
     */
    @Override
    public Document getDocument() {
        return null;
    }
}
