package cz.vse.praguePub.logika.dbObjekty;

import com.mongodb.client.MongoCollection;
import lombok.Data;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.Map;

@Data
public class Recenze implements DBObjekt {
    private final ObjectId uzivatel;
    private final String text;
    private final double hodnoceni;

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
     * @param uzivatele MongoCollection s uživateli
     * @return jméno uživatele
     */
    public String getUzivatelskeJmeno(MongoCollection<Document> uzivatele) {
        Document uzivatelDoc = uzivatele.find(new Document("_id", this.uzivatel)).first();

        if (uzivatelDoc == null) return null;
        return uzivatelDoc.getString("jmeno");
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
