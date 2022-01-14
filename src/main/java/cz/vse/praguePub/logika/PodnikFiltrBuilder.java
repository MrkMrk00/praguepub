package cz.vse.praguePub.logika;

import com.mongodb.client.AggregateIterable;
import com.mongodb.client.MongoCollection;
import cz.vse.praguePub.logika.dbObjekty.Pivo;
import cz.vse.praguePub.logika.dbObjekty.Podnik;
import org.apache.commons.lang3.math.NumberUtils;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.mongodb.client.model.Filters.*;

public class PodnikFiltrBuilder extends FiltrBuilder {

    private final AggregateIterable<Document> hodnoceniLookup;
    private final MongoCollection<Document> piva;

    public PodnikFiltrBuilder(MongoCollection<Document> kolekceSPodniky, MongoCollection<Document> kolekceSPivy) {
        super(kolekceSPodniky);

        this.piva = kolekceSPivy;
        this.hodnoceniLookup = kolekceSPodniky.aggregate(
                List.of(
                        Document.parse("{ $unwind: '$recenze' }"),
                        Document.parse("""
                {
                    $group: {
                        _id: '$_id',
                        avg_hodnoceni: {
                            $avg: '$recenze.hodnoceni'
                        }
                    }
                }
                """)
                )
        );

    }

    public List<Podnik> finalizuj() {
        List<Podnik> kVraceni = new ArrayList<>();
        for (Document doc : this.finalizujDokumenty()) kVraceni.add(Podnik.inicializujZDokumentu(doc, this.piva));
        return kVraceni;
    }

    public PodnikFiltrBuilder nazev(String nazev) {
        this.pridejCustom(regex("nazev", nazev, "i"));
        return this;
    }

    public PodnikFiltrBuilder cisloMestskeCasti(int cisloMestskeCasti) {
        this.pridejCustom(eq("adresa.mc_cislo", cisloMestskeCasti));
        return this;
    }

    public PodnikFiltrBuilder nazevMestskeCasti(String nazevMestskeCasti) {
        this.pridejCustom(regex("adresa.mc_nazev", nazevMestskeCasti, "i"));
        return this;
    }

    public PodnikFiltrBuilder psc(int postovniSmerovaciCislo) {
        this.pridejCustom(eq("adresa.psc", postovniSmerovaciCislo));
        return this;
    }

    public PodnikFiltrBuilder cisloPopisne(String cisloPopisne) {
        this.pridejCustom(eq("adresa.cp", cisloPopisne));
        return this;
    }

    public PodnikFiltrBuilder ulice(String nazevUlice) {
        this.pridejCustom(regex("adresa.ulice", nazevUlice, "i"));
        return this;
    }

    public PodnikFiltrBuilder prumerneHodnoceni(double min, double max) {
        List<ObjectId> vyfiltrovano = new ArrayList<>();

        for (Document doc : this.hodnoceniLookup)
            if (doc.getDouble("avg_hodnoceni") > min &&  doc.getDouble("avg_hodnoceni") < max)
                vyfiltrovano.add(doc.getObjectId("_id"));


        this.pridejCustom(in("_id", vyfiltrovano));
        return this;
    }

    public PodnikFiltrBuilder obsahujePivo(Pivo pivo) {
        this.pridejCustom(elemMatch("piva", eq("_id", pivo.get_id())));
        return this;
    }

    public PodnikFiltrBuilder cenaPiva(double min, double max) {
        this.pridejCustom(elemMatch("piva", and(
                gte("cena", min),
                lte("cena", max)
        )));
        return this;
    }

    public PodnikFiltrBuilder cenaPiva(double max) {
        this.pridejCustom(elemMatch("piva", lte("cena", max)));
        return this;
    }

    public PodnikFiltrBuilder parse(Map<String, String> mapaSFiltry) {
        String nazev = mapaSFiltry.getOrDefault("nazev", null);
        if (nazev != null && !nazev.isBlank()) this.nazev(nazev);

        String mcCislo = mapaSFiltry.getOrDefault("mc_cislo", null);
        int mcCisloInt = mcCislo != null && !mcCislo.isBlank() ?
                    NumberUtils.toInt(mcCislo, -1)
                :   -1;
        if (mcCisloInt != -1) this.cisloMestskeCasti(mcCisloInt);

        String mcNazev = mapaSFiltry.getOrDefault("mc_nazev", null);
        if (mcNazev != null && !mcNazev.isBlank()) this.nazevMestskeCasti(mcNazev);

        String ulice = mapaSFiltry.getOrDefault("ulice", null);
        if (ulice != null && !ulice.isBlank()) this.ulice(ulice);

        String cp = mapaSFiltry.getOrDefault("cp", null);
        if (cp != null && !cp.isBlank()) this.cisloPopisne(cp);

        String psc = mapaSFiltry.getOrDefault("psc", null);
        int pscInt = psc != null && !psc.isBlank() ?
                    NumberUtils.toInt(psc, -1)
                :   -1;
        if (pscInt != -1) this.psc(pscInt);

        return this;
    }
}
