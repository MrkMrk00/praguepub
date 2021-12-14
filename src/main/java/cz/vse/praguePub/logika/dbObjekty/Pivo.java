package cz.vse.praguePub.logika.dbObjekty;

import lombok.Data;
import org.bson.Document;

import java.util.Map;

@Data
public class Pivo implements DBObjekt {
    private final String nazev;
    private final String nazevPivovaru;
    private final double stupnovitost;
    private final double obsahAlkoholu;
    private final String typ;
    private final String typKvaseni;

    private final double cena;
    private final double objem;

    @Override
    public Document toDocument() {
        Document pivo = new Document();
        pivo.putAll(
                Map.of(
                        "nazev", this.nazev,
                        "nazev_pivovaru", this.nazevPivovaru,
                        "stupnovitost", this.stupnovitost,
                        "obsah_alkoholu", this.obsahAlkoholu,
                        "typ", this.typ,
                        "typ_kvaseni", this.typKvaseni
                )
        );
        return pivo;
    }
}