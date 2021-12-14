package cz.vse.praguePub.logika;

import com.mongodb.client.MongoDatabase;
import cz.vse.praguePub.logika.dbObjekty.Pivo;
import cz.vse.praguePub.logika.dbObjekty.Podnik;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.mongodb.client.model.Filters.*;

public class Databaze implements IDatabaze {
    private final IUzivatel uzivatel;
    private final MongoDatabase db;

    public Databaze(IUzivatel uzivatel) {
        this.uzivatel = uzivatel;
        this.db = uzivatel.getPraguePubDatabase();
    }

    @Override
    public List<Podnik> getPodnikyVMestskeCasti(int mestskaCast) {
        var podnikyDB = this.db.getCollection("podniky")
                .find(eq("adresa.mc_cislo", mestskaCast));

        List<Podnik> podnikyKVraceni = new ArrayList<>();
        for (Document podnik : podnikyDB) {
            Document adresa = podnik.get("adresa", Document.class);
            podnikyKVraceni.add(Podnik.getFromDocument(podnik)
            );
        }
        return podnikyKVraceni;
    }

    @Override
    public List<Podnik> getPodnikyPodlePiva(Pivo pivo) {
        return null;
    }

    @Override
    public List<Podnik> getPodnikyPodleNazvu(String nazev) {
        return null;
    }
}
