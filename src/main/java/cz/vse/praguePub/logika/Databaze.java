package cz.vse.praguePub.logika;

import com.mongodb.client.MongoDatabase;
import cz.vse.praguePub.logika.dbObjekty.Pivo;
import cz.vse.praguePub.logika.dbObjekty.Podnik;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.*;

import static com.mongodb.client.model.Filters.*;

public class Databaze implements IDatabaze {
    private final Uzivatel uzivatel;
    private final MongoDatabase db;

    public Databaze(Uzivatel uzivatel) {
        this.uzivatel = uzivatel;
        this.db = uzivatel.getPraguePubDatabaze();
    }

    private Set<Podnik> prevedNalezeneNaInstance(Iterable<Document> nalezenePodniky) {
        Set<Podnik> vratit = new HashSet<>();
        nalezenePodniky.forEach(podnikDoc ->
                vratit.add(Podnik.inicializujZDokumentu(podnikDoc, this.db.getCollection("piva")))
        );
        return vratit;
    }

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
}
