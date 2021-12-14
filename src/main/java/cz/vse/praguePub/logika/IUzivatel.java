package cz.vse.praguePub.logika;

import com.mongodb.client.MongoDatabase;

public interface IUzivatel {

    /**
     *
     * @return databázi aplikace
     */
    MongoDatabase getPraguePubDatabase();
}
