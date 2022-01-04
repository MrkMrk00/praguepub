package cz.vse.praguePub.logika.dbObjekty;

import com.mongodb.BasicDBObject;
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
    private final String adresa_ulice;
    private final String adresa_psc;
    private final Integer adresa_cp;
    private final Set<Recenze> recenze;
    private final Map<ObjectId, Pivo> pivniListek;

    //levý sloupec: názvy, co se zobrazí v tabulce jako nadpisy sloupců; pravý sloupec: názvy atributů instance)
    //(logika tabulky bere atribut instance přes getter, tudíž atribut musí být v camelCase a metoda musí začínat "get")
    public static final String[][] PRO_TABULKU = {
            { "Název",          "nazev"            },
            { "Adresa",         "adresaProTabulku" },
            { "Nabízená piva",  "pivaProTabulku"   }
    };

    /**
     * Naformátuje adresu pro výpis v tabulce podniků
     * @return formátovaný string
     */
    public String getAdresaProTabulku() {
        return this.adresa_ulice + " " + this.adresa_cp + ", " + this.adresa_mc_nazev;
    }

    /**
     * Vrátí string, který se zobrazí v tabulce jako krátký popis toho, co podnik nabízí
     * @return formátovaný string
     */
    public String getPivaProTabulku() {
        StringBuilder sb = new StringBuilder();

        this.getPivniListek().forEach(
                (objID, pivo) -> sb.append(pivo.getNazev())
                        .append(" à ")
                        .append(pivo.getCena())
                        .append(" Kč")
                        .append(",\n")
        );

        int start = (sb.length() < 3) ? 0 : sb.length() - 2;
        int end   = (sb.length() < 3) ? 0 : sb.length() - 1;
        return sb.delete(start, end).toString();
    }

    /**
     * @return průměrné hodnocení
     */
    public double getPrumerneHodnoceni() {
        double prumerneHodnoceni = 0d;

        for (Recenze r : this.recenze) prumerneHodnoceni += r.getHodnoceni();
        return (prumerneHodnoceni / this.recenze.size());
    }

    /**
     * Getter pro recenze u podniku
     * @return kopii setu s recenzemi
     */
    public Set<Recenze> getRecenze() {
        return Collections.unmodifiableSet(this.recenze);
    }

    /**
     * Getter pro nabízená piva podnikem
     * @return kopii mapy s nabízenými pivy
     */
    public Map<ObjectId, Pivo> getPivniListek() {
        return Collections.unmodifiableMap(this.pivniListek);
    }

    /**
     * Vytvoří instanci podniku z databázového dokumentu. <br>
     * Pro získání informací o pivu potřebuje přístup do kolekce s pivy.
     * @param doc databázový dokument
     * @param kolekcePiv kolekce piv
     * @return instanci podniku
     */
    public static Podnik inicializujZDokumentu(Document doc, MongoCollection<Document> kolekcePiv) {
        if (doc == null || kolekcePiv == null) return null;

        Set<Recenze> recenzeLst = new HashSet<>();
        doc.getList("recenze", Document.class).forEach(
                (recenze) -> recenzeLst.add(Recenze.inicializujZDokumentu(recenze))
        );

        Map<ObjectId, Pivo> pivniListek = new HashMap<>();
        doc.getList("piva", Document.class).forEach(
                (pivoDoc) -> {
                    ObjectId pivoID = pivoDoc.get("pivo", ObjectId.class);
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

    @Override
    public Document getDocument() {
        List<BasicDBObject> recenzeList = new ArrayList<>();
        this.getRecenze().forEach(rec -> recenzeList.add(new BasicDBObject(rec.getDocument())));

        List<BasicDBObject> pivoList = new ArrayList<>();
        this.getPivniListek().forEach(
                (id, pivo) -> pivoList.add(
                        new BasicDBObject(Map.of(
                        "pivo", id,
                        "cena", pivo.getCena(),
                        "objem", pivo.getObjem()
                )))
        );

        return new Document(Map.of(
                "jmeno", this.nazev,
                "adresa", Map.of(
                        "mc_cislo", this.adresa_mc_cislo,
                        "mc_nazev", this.adresa_mc_nazev,
                        "psc", this.adresa_psc,
                        "ulice", this.adresa_ulice,
                        "cp", this.adresa_cp
                ),
                "recenze", recenzeList,
                "piva", pivoList
        ));
    }
}
