package cz.vse.praguePub.logika;

import com.mongodb.client.MongoCollection;
import cz.vse.praguePub.logika.dbObjekty.DBObjekt;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.ArrayList;
import java.util.List;

import static com.mongodb.client.model.Filters.*;

public class FiltrBuilder {
    protected final MongoCollection<Document> kolekce;
    protected final List<Bson> listFiltru;

    public FiltrBuilder(MongoCollection<Document> kolekce) {
        this.kolekce = kolekce;
        this.listFiltru = new ArrayList<>();
    }

    public FiltrBuilder pridejCustom(Bson filtr) {
        this.listFiltru.add(filtr);
        return this;
    }
}
