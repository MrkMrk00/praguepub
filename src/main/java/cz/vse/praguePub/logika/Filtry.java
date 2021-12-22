package cz.vse.praguePub.logika;


import com.mongodb.client.MongoCollection;
import cz.vse.praguePub.logika.dbObjekty.Pivo;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

import static com.mongodb.client.model.Filters.*;

public final class Filtry {
    private final List<Bson> filtryList;

    public Filtry() {
        this.filtryList = new ArrayList<>();
    }

    public Filtry pridejFilter(Bson filter) {
        this.filtryList.add(filter);
        return this;
    }

    public void odeberFilter(Bson filter) {
        this.filtryList.remove(filter);
    }

    public Bson finalizuj() {
        return and(this.filtryList);
    }


    public static Bson podnik_mestskaCast(int mc) {
        return eq("adresa.mc_cislo", mc);
    }

    public static Bson podnik_cenaPiva(double min, double max) {
        return and(
                gte("piva.cena", min),
                lte("piva.cena", max)
        );
    }

    public static Bson podnik_pivoID(ObjectId pivoID) {
        return elemMatch("piva", eq("_id", pivoID));
    }

    public static Bson podnik_pivoInstance(Pivo pivo, MongoCollection<Document> piva) {
        Document najito = piva.find(pivo.getDocument()).first();
        if (najito == null) return null;

        return podnik_pivoID(najito.getObjectId("_id"));
    }

    public static Bson podnik_nazev(String nazev) {
        return eq("jmeno", nazev);
    }
}
