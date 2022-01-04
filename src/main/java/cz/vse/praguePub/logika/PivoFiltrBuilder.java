package cz.vse.praguePub.logika;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Aggregates;
import cz.vse.praguePub.logika.dbObjekty.Pivo;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.ArrayList;
import java.util.List;

import static com.mongodb.client.model.Filters.*;

public class PivoFiltrBuilder extends FiltrBuilder {
    public PivoFiltrBuilder(MongoCollection<Document> kolekce) {
        super(kolekce);
    }

    public List<Pivo> finalizuj() {
        List<Bson> listAgregatu = new ArrayList<>();
        this.listFiltru.forEach(filtr -> listAgregatu.add(Aggregates.match(filtr)));

        List<Pivo> vratit = new ArrayList<>();
        this.kolekce.aggregate(listAgregatu).forEach(
                pivoDoc -> vratit.add(Pivo.inicializujZDokumentu(pivoDoc, null, null))
        );

        return vratit;
    }

    public PivoFiltrBuilder pivovar(String nazevPivovaru) {
        this.pridejCustom(eq("pivovar", "nazevPivovaru"));
        return this;
    }

    public PivoFiltrBuilder stupnovitost(double stupnovitost) {
        this.pridejCustom(eq("stupnovitost", stupnovitost));
        return this;
    }

    public PivoFiltrBuilder stupnovitost(double min, double max) {
        this.pridejCustom(
                and(
                        gte("stupnovitost", min),
                        lte("stupnovitost", max)
                )
        );
        return this;
    }

    public PivoFiltrBuilder nazev(String nazev) {
        this.pridejCustom(eq("nazev", nazev));
        return this;
    }

    public PivoFiltrBuilder obsahAlkoholu(double obsahAlkoholu) {
        this.pridejCustom(eq("obsah_alkoholu", obsahAlkoholu));
        return this;
    }

    public PivoFiltrBuilder obsahAlkoholu(double min, double max) {
        this.pridejCustom(
                and(
                        gte("obsah_alkoholu", min),
                        lte("obsah_alkoholu", max)
                )
        );
        return this;
    }

    public PivoFiltrBuilder typ(String nazevTypu) {
        this.pridejCustom(eq("typ", nazevTypu));
        return this;
    }

    public PivoFiltrBuilder typKvaseni(String nazevTypuKvaseni) {
        this.pridejCustom(eq("typ", nazevTypuKvaseni));
        return this;
    }
}
