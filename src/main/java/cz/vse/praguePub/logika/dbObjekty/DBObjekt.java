package cz.vse.praguePub.logika.dbObjekty;

import org.bson.Document;

public interface DBObjekt {
    /**
     * Převede instanci databázového objektu do formátu pro použití s databází
     * @return instanci Document s parametry DBObjektu
     */
    Document getDocument();
}
