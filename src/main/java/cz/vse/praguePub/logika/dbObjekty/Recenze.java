package cz.vse.praguePub.logika.dbObjekty;

import cz.vse.praguePub.logika.Uzivatel;
import lombok.Data;
import org.bson.Document;

@Data
public class Recenze implements DBObjekt {
    private final String uzivatel;
    private final String text;
    private final double hodnoceni;

    public static Recenze getFromDocument(Document doc) {
        return new Recenze(
                doc.get("uzivatel", String.class),
                doc.get("text", String.class),
                doc.get("hodnoceni", Double.class));
    }

    @Override
    public Document toDocument() {
        return null;
    }
}
