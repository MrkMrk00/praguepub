package cz.vse.praguePub.logika.dbObjekty;

import lombok.Data;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.Map;

@Data
public non-sealed class Recenze implements DBObjekt {
    private final ObjectId uzivatel;
    private final String text;
    private final double hodnoceni;

    private String uzivatelskeJmeno;


    //levý sloupec: názvy, co se zobrazí v tabulce jako nadpisy sloupců; pravý sloupec: názvy atributů instance)
    //(logika tabulky bere atribut instance přes getter, tudíž atribut musí být v camelCase a metoda musí začínat "get")
    public static final String[][] PRO_TABULKU = {
            { "Jméno uživatele",    "uzivatelskeJmeno"  },
            { "Hodnocení",          "hodnoceni"         },
            { "Komentář",           "text"              }
    };

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
