package cz.vse.praguePub.logika.dbObjekty;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@EqualsAndHashCode
@AllArgsConstructor
public class Pivo {
    @Getter private final String nazevPivovaru;
    @Getter private final double stupnovitost;
    @Getter private final double obsahAlkoholu;
    @Getter private final String typ;
    @Getter private final String typKvaseni;
    @Getter private final String nazev;

    @Getter private final double cena;
    @Getter private final double objem;
}