package cz.vse.praguePub.logika;

import lombok.ToString;

/**
 * Typy možných výsledků, při manipulaci s databází
 */
@ToString
public enum TypVysledku {
    OK, DB_CHYBA, ZADNA_ZMENA,
    STEJNA_ADRESA, STEJNY_NAZEV
}
