package cz.vse.praguePub.logika;

import com.mongodb.client.MongoDatabase;
import cz.vse.praguePub.logika.dbObjekty.Podnik;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

import static com.mongodb.client.model.Filters.*;

public class Databaze {
    private final MongoDatabase db;

    public Databaze(MongoDatabase db) {
        this.db = db;
    }

    public List<Podnik> getPodnikyVMestskeCasti(int mestskaCast) {
        var podnikyDB = this.db.getCollection("podniky")
                .find(eq("adresa.mc_cislo", mestskaCast));

        List<Podnik> podnikyKVraceni = new ArrayList<>();
        for (Document podnik : podnikyDB) {
            Document adresa = podnik.get("adresa", Document.class);
            podnikyKVraceni.add(new Podnik(
                    podnik.get("jmeno", String.class),
                    adresa.get("mc_cislo", Integer.class),
                    adresa.get("mc_nazev", String.class),
                    adresa.get("ulice", String.class),
                    adresa.get("psc", String.class),
                    adresa.get("cp", Integer.class),
                    null,
                    null
            ));
        }
        return podnikyKVraceni;
    }
}
