package cz.vse.praguePub.logika;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Aggregates;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.ArrayList;
import java.util.List;

/**
 * Hlavní logika FiltrBuilderů
 */
public class FiltrBuilder {
    protected final MongoCollection<Document> kolekce;
    protected final List<Bson> listFiltru;
    protected final List<Bson> listAgregatu;

    /**
     * Vytvoří instanci FiltrBuilderu s danou kolekcí
     * @param kolekce kolekce, ve které se má hledat
     */
    public FiltrBuilder(MongoCollection<Document> kolekce) {
        this.kolekce = kolekce;
        this.listFiltru = new ArrayList<>();
        this.listAgregatu = new ArrayList<>();
    }

    /**
     * Přidá do seznamu filtrů jednoduchý Bson filter
     * @param filtr filtr, který se má přidat do složeného dotazu
     * @return FiltrBuilder
     */
    public final FiltrBuilder pridejCustom(Bson filtr) {
        this.listFiltru.add(filtr);
        return this;
    }

    /**
     * Přidá do seznamu agregátů Bson agregovací funkci
     * @param agregat agregát, který se má přidat do složeného dotazu
     * @return FiltrBuilder
     */
    public final FiltrBuilder pridejAgregat(Bson agregat) {
        this.listAgregatu.add(agregat);
        return this;
    }

    /**
     * Vyhledá v databázi objekty, které splňují parametry ve složeném dotazu
     * @return dokumenty nalezených databázových objektů
     */
    public final List<Document> finalizujDokumenty() {
        for (Bson filter : this.listFiltru) this.pridejAgregat(Aggregates.match(filter));

        List<Document> kVraceni = new ArrayList<>();
        for (Document doc : this.kolekce.aggregate(this.listAgregatu)) kVraceni.add(doc);

        return kVraceni;
    }
}
