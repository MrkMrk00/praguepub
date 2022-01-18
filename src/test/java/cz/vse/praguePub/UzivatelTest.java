package cz.vse.praguePub;

import cz.vse.praguePub.logika.Uzivatel;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.jupiter.api.Assertions.*;

public class UzivatelTest {
    private static final Logger log = LoggerFactory.getLogger(UzivatelTest.class);

    @Test
    public void prihlaseniHosta() {
        Uzivatel host = null;
        try {
            host = Uzivatel.guest();
        } catch (Exception e) {
            log.error(e.getMessage());
        }

        assertNotNull(host);
        assertTrue(host.isPrihlasen());
        assertTrue(host.isGuest());
        assertDoesNotThrow(host::getPraguePubDatabaze);
    }

    @Test
    public void prihlaseniUzivatele() {
        String prihlJmeno = "test_user";
        String heslo = "cAkD(n:Kd9LHS$^U";

        Uzivatel uzivatel = null;
        try {
            uzivatel = new Uzivatel(prihlJmeno, heslo);
        } catch (Exception e) {
            log.error(e.getMessage());
        }

        assertNotNull(uzivatel);
        assertTrue(uzivatel.isPrihlasen());
        assertFalse(uzivatel.isGuest());
        assertDoesNotThrow(uzivatel::getPraguePubDatabaze);
    }
}
