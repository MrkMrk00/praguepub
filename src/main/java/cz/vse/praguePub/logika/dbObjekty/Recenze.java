package cz.vse.praguePub.logika.dbObjekty;

import cz.vse.praguePub.logika.Uzivatel;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.Map;

import static com.mongodb.client.model.Filters.eq;

@Data
public class Recenze implements DBObjekt {
    private final ObjectId uzivatel;
    private final String text;
    private final double hodnoceni;

    @Setter(AccessLevel.NONE) @Getter(AccessLevel.NONE)
    private String uzivatelskeJmeno = null;

    /**
     * Vytvoří instanci recenze z databázového dokumentu
     * @param doc databázový dokument
     * @return instanci recenze
     */
    public static Recenze inicializujZDokumentu(Document doc) {
        return new Recenze(
                doc.get("uzivatel", ObjectId.class),
                doc.get("text", String.class),
                doc.get("hodnoceni", Double.class));
    }

    /**
     * V recenzi je uložené pouze ID uživatele, který recenzi vytvořil. Pro doptání se na jméno uživatele je potřeba ho najít v databázi.
     * @return jméno uživatele
     */
    public String getUzivatelskeJmeno() {
        if (this.uzivatelskeJmeno != null) return this.uzivatelskeJmeno;

        Uzivatel guest = Uzivatel.guest();
        if (guest == null || !guest.isPrihlasen()) return null;

        Document uzivatelDoc = guest
                .getPraguePubDatabaze()
                .getCollection("uzivatele")
                .find(eq("_id", this.uzivatel))
                .first();

        if (uzivatelDoc == null) return null;
        this.uzivatelskeJmeno = uzivatelDoc.getString("jmeno");
        return this.uzivatelskeJmeno;
    }

    @Override
    public Document getDocument() {
        return new Document(
                Map.of(
                        "uzivatel", this.uzivatel,
                        "text", this.text,
                        "hodnoceni", this.hodnoceni
                )
        );
    }
}
