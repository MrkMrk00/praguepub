package cz.vse.praguePub.logika.dbObjekty;

import cz.vse.praguePub.logika.Uzivatel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@EqualsAndHashCode
@AllArgsConstructor
public class Recenze {
    @Getter private Uzivatel uzivatel;
    @Getter private String text;
    @Getter private double hodnoceni;
}
