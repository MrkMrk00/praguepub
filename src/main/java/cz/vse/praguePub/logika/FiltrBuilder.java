package cz.vse.praguePub.logika;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Aggregates;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.ArrayList;
import java.util.List;

public class FiltrBuilder {
    protected final MongoCollection<Document> kolekce;
    protected final List<Bson> listFiltru;
    protected final List<Bson> listAgregatu;

    public FiltrBuilder(MongoCollection<Document> kolekce) {
        this.kolekce = kolekce;
        this.listFiltru = new ArrayList<>();
        this.listAgregatu = new ArrayList<>();
    }

    public final FiltrBuilder pridejCustom(Bson filtr) {
        this.listFiltru.add(filtr);
        return this;
    }

    public final FiltrBuilder pridejAgregat(Bson agregat) {
        this.listAgregatu.add(agregat);
        return this;
    }

    public final List<Document> finalizujDokumenty() {
        for (Bson filter : this.listFiltru) this.pridejAgregat(Aggregates.match(filter));

        List<Document> kVraceni = new ArrayList<>();
        for (Document doc : this.kolekce.aggregate(this.listAgregatu)) kVraceni.add(doc);

        return kVraceni;
    }
}
