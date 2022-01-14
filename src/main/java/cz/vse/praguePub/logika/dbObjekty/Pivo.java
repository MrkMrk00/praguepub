package cz.vse.praguePub.logika.dbObjekty;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.Map;

@Data
@AllArgsConstructor
public class Pivo implements DBObjekt {
    private final ObjectId _id;
    private String nazev;
    private String nazevPivovaru;
    private double stupnovitost;
    private double obsahAlkoholu;
    private String typ;
    private String typKvaseni;
    private Double cena;
    private Double objem;

    //levý sloupec: názvy, co se zobrazí v tabulce jako nadpisy sloupců; pravý sloupec: názvy atributů instance
    //(logika tabulky bere atribut instance přes getter, tudíž atribut musí být v camelCase a metoda musí začínat "get")
    public static final String[][] PRO_TABULKU = {
            { "Název",          "nazev"         },
            { "Název pivovaru", "nazevPivovaru" },
            { "Stupňovitost",   "stupnovitost"  },
            { "Obsah alkoholu", "obsahAlkoholu" },
            { "Typ",            "typ"           },
            { "Typ kvašení",    "typKvaseni"    },
            { "Cena",           "cena"          },
            { "Objem",          "objem"         }
    };

    public static final String[][] PRO_TABULKU_BEZ_CENY_A_OBJEMU = {
            { "Název",          "nazev"         },
            { "Název pivovaru", "nazevPivovaru" },
            { "Stupňovitost",   "stupnovitost"  },
            { "Obsah alkoholu", "obsahAlkoholu" },
            { "Typ",            "typ"           },
            { "Typ kvašení",    "typKvaseni"    }
    };

    public static final String [][] PRO_TABULKU_CENIK = {
            { "Název",          "nazev"         },
            { "Cena",           "cena"          },
            { "Objem",          "objem"         }
    };

    /**
     * Vytvoří instanci podniku z databázového dokumentu. <br>
     * @param pivo databázový dokument
     * @param cena cena piva, za kterou podnik pivo nabízí
     * @param objem objem piva který podnik nabízí
     * @return instanci podniku
     */
    public static Pivo inicializujZDokumentu(Document pivo, Double cena, Double objem) {
        return new Pivo(
                pivo.get("_id", ObjectId.class),
                pivo.get("nazev", String.class),
                pivo.get("pivovar", String.class),
                pivo.get("stupnovitost", Double.class),
                pivo.get("obsah_alkoholu", Double.class),
                pivo.get("typ", String.class),
                pivo.get("typ_kvaseni", String.class),
                cena,
                objem
        );
    }

    @Override
    public Document getDocument() {
        Document pivo = new Document();
        pivo.putAll(
                Map.of(
                        "_id", this._id,
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