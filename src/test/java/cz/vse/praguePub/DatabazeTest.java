package cz.vse.praguePub;

import cz.vse.praguePub.logika.Databaze;
import cz.vse.praguePub.logika.Uzivatel;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.jupiter.api.Assertions.*;

public class DatabazeTest {
    private static final Logger log = LoggerFactory.getLogger(DatabazeTest.class);

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
}
