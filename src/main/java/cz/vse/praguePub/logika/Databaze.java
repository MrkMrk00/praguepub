package cz.vse.praguePub.logika;


import com.mongodb.client.MongoDatabase;
import cz.vse.praguePub.logika.dbObjekty.Pivo;
import cz.vse.praguePub.logika.dbObjekty.Podnik;
import org.bson.Document;

import java.util.*;

import static com.mongodb.client.model.Filters.*;

public class Databaze implements IDatabaze {
    private final IUzivatel uzivatel;
    private final MongoDatabase db;

    public Databaze(IUzivatel uzivatel) {
        this.uzivatel = uzivatel;
        this.db = uzivatel.getPraguePubDatabase();
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
        return null;
    }

    @Override
    public Set<Podnik> getPodnikyPodleNazvu(String nazev) {
        return null;
    }
}
