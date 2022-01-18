package cz.vse.praguePub;

import cz.vse.praguePub.logika.Databaze;
import cz.vse.praguePub.logika.Uzivatel;
import cz.vse.praguePub.util.PraguePubDatabaseException;
import org.bson.Document;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.jupiter.api.Assertions.*;

public class DatabazeTest {
    private static final Logger log = LoggerFactory.getLogger(DatabazeTest.class);
    private Databaze db;

    @BeforeEach
    public void vytvorInstanciDatabaze() {
        try {
            this.db = Databaze.get(Uzivatel.guest());
        } catch (PraguePubDatabaseException e) {
            log.error(e.getMessage());
        }
    }

    @Test
    public void prihlaseni() {
        Databaze db = null;
        try {
            db = Databaze.get(Uzivatel.guest());
        } catch (Exception e) {
            log.error(e.getMessage());
        }

        assertNotNull(db);
        assertTrue(db.getUzivatel().isPrihlasen());
        assertDoesNotThrow(db::getPivaCollection);
        assertDoesNotThrow(db::getPodnikyCollection);
    }

    @Test
    public void getUzivatelskeJmeno() {
        Document testUserDoc = Uzivatel.guest()
                .getPraguePubDatabaze()
                .getCollection("uzivatele")
                .find(new Document("userName", "test_user"))
                .first();
        String uzivJmeno = this.db.getUzivatelskeJmeno(testUserDoc.getObjectId("_id"));
        assertEquals(uzivJmeno, testUserDoc.getString("userName"));
    }


}
