package cz.vse.praguePub.logika.dbObjekty;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import lombok.Data;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.*;
import java.util.stream.Collectors;

@Data
public class Podnik implements DBObjekt {
    private final ObjectId _id;
    private final String nazev;
    private final int adresa_mc_cislo;
    private final String adresa_mc_nazev;
    private final String adresa_ulice;
    private final Integer adresa_psc;
    private final String adresa_cp;
    private final List<Recenze> recenze;
    private final List<Pivo> pivniListek;

    //levý sloupec: názvy, co se zobrazí v tabulce jako nadpisy sloupců; pravý sloupec: názvy atributů instance)
    //(logika tabulky bere atribut instance přes getter, tudíž atribut musí být v camelCase a metoda musí začínat "get")
    public static final String[][] PRO_TABULKU = {
            { "Název",                     "nazev"            },
            { "Adresa",                    "adresaProTabulku" },
            { "Nabízená piva",             "pivaProTabulku"   },
            { "Průmerné hodnocení",        "prumerneHodnoceni"}
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
                pivo -> sb.append(pivo.getNazev())
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
    public List<Recenze> getRecenze() {
        return Collections.unmodifiableList(this.recenze);
    }

    /**
     * Getter pro nabízená piva podnikem
     * @return kopii mapy s nabízenými pivy
     */
    public List<Pivo> getPivniListek() {
        return Collections.unmodifiableList(this.pivniListek);
    }

    /**
     * Vytvoří instanci podniku z databázového dokumentu. <br>
     * Pro získání informací o pivu potřebuje přístup do kolekce s pivy.
     * @param doc databázový dokument
     * @param kolekcePiv kolekce piv
     * @return instanci podniku
     */
    public static Podnik inicializujZDokumentu(Document doc, MongoCollection<Document> kolekcePiv) {
        List<Recenze> recenzeLst = new ArrayList<>();
        doc.getList("recenze", Document.class).forEach(
                (recenze) -> recenzeLst.add(Recenze.inicializujZDokumentu(recenze))
        );

        List<Pivo> pivniListek = new ArrayList<>();
        doc.getList("piva", Document.class).forEach(
                pivoDoc -> {
                    Document pivoDocZKolekce = kolekcePiv.find(Filters.eq("_id", pivoDoc.get("_id"))).first();
                    if (pivoDocZKolekce == null) return;

                    pivniListek.add(
                            Pivo.inicializujZDokumentu(
                                    pivoDocZKolekce,
                                    pivoDoc.getDouble("cena"),
                                    pivoDoc.getDouble("objem")
                            )
                    );
                }
        );

        Document adresa = doc.get("adresa", Document.class);

        return new Podnik(
                doc.get("_id", ObjectId.class),
                doc.get("jmeno", String.class),
                adresa.get("mc_cislo", Integer.class),
                adresa.get("mc_nazev", String.class),
                adresa.get("ulice", String.class),
                adresa.get("psc", Integer.class),
                adresa.get("cp", String.class),
                recenzeLst,
                pivniListek
        );
    }

    @Override
    public Document getDocument() {
        List<Document> recenzeList = this.getRecenze()
                .stream()
                .map(Recenze::getDocument)
                .collect(Collectors.toList());


        List<Document> pivoList = this.getPivniListek()
                .stream()
                .map(pivo -> new Document(
                        Map.of(
                                "pivo", pivo.get_id(),
                                "cena", pivo.getCena(),
                                "objem", pivo.getObjem()
                        )
                ))
                .collect(Collectors.toList());

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
