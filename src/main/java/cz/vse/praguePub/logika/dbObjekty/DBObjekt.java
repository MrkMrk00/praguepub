package cz.vse.praguePub.logika.dbObjekty;

import org.bson.Document;

public sealed interface DBObjekt permits Pivo, Podnik, Recenze {
    /**
     * Převede instanci databázového objektu do formátu pro použití s databází
     * @return instanci Document s parametry DBObjektu
     */
    Document getDocument();
}
