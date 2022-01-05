package cz.vse.praguePub.logika;

import com.mongodb.client.AggregateIterable;
import com.mongodb.client.MongoCollection;
import cz.vse.praguePub.logika.dbObjekty.Pivo;
import cz.vse.praguePub.logika.dbObjekty.Podnik;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

import static com.mongodb.client.model.Filters.*;

public class PodnikFiltrBuilder extends FiltrBuilder {

    private final AggregateIterable<Document> hodnoceniLookup;

    public PodnikFiltrBuilder(MongoCollection<Document> kolekceSPodniky) {
        super(kolekceSPodniky);

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

    public List<Podnik> finalizuj(MongoCollection<Document> kolekcePiv) {
        List<Podnik> kVraceni = new ArrayList<>();
        for (Document doc : this.finalizujDokumenty()) kVraceni.add(Podnik.inicializujZDokumentu(doc, kolekcePiv));
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
        this.pridejCustom(regex("adresa.mc_nazev", "^" + cisloPopisne));
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

}
