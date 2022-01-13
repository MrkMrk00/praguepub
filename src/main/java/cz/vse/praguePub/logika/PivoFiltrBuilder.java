package cz.vse.praguePub.logika;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Aggregates;
import cz.vse.praguePub.logika.dbObjekty.Pivo;
import org.apache.commons.lang3.math.NumberUtils;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.mongodb.client.model.Filters.*;

public class PivoFiltrBuilder extends FiltrBuilder {

    public PivoFiltrBuilder(MongoCollection<Document> kolekce) {
        super(kolekce);
    }

    public List<Pivo> finalizuj() {
        for (Bson filter : this.listFiltru) this.listAgregatu.add(Aggregates.match(filter));

        List<Pivo> kVraceni = new ArrayList<>();
        for (Document pivoDoc : this.kolekce.aggregate(this.listAgregatu))
            kVraceni.add(Pivo.inicializujZDokumentu(pivoDoc, null, null));

        return kVraceni;
    }

    public PivoFiltrBuilder pivovar(String nazevPivovaru) {
        this.pridejCustom(regex("pivovar", nazevPivovaru ));
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
        this.pridejCustom(regex("nazev", nazev));
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
        this.pridejCustom(regex("typ", nazevTypu));
        return this;
    }

    public PivoFiltrBuilder typKvaseni(String nazevTypuKvaseni) {
        this.pridejCustom(regex("typ", nazevTypuKvaseni));
        return this;
    }

    public PivoFiltrBuilder parse(Map<String, String> atributy) {
        String pivovar = atributy.get("pivovar");
        if (pivovar != null && !pivovar.isBlank()) this.nazev(pivovar);

        double stupnovitost = NumberUtils.toDouble(atributy.get("stupnovitost"), -1);
        if (stupnovitost != -1) this.stupnovitost(stupnovitost - 0.5, stupnovitost + 0.5);

        String nazev = atributy.get("nazev");
        if (nazev != null && !nazev.isBlank()) this.nazev(nazev);

        double obsahAlkoholu = NumberUtils.toDouble(atributy.get("obsah_alkoholu"), -1);
        if (obsahAlkoholu != -1) this.obsahAlkoholu(obsahAlkoholu - 0.5, obsahAlkoholu + 0.5);

        String typ = atributy.get("typ");
        if (typ != null && !typ.isBlank()) this.typ(typ);

        String typKvaseni = atributy.get("typ_kvaseni");
        if (typKvaseni != null && !typKvaseni.isBlank()) this.typKvaseni(typKvaseni);

        return this;
    }
}
