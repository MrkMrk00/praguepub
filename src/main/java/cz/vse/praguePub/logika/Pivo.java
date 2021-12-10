package cz.vse.praguePub.logika;

import lombok.Data;


@Data public class Pivo {

    private final String nazevPivovaru;
    private final double stupnovitost;
    private final double obsahAlkoholu;
    private final String typ;
    private final String typKvaseni;
    private final String nazev;

    /**
     * Konstruktor pro třídu pivo
     * @param nazevPivovaru
     * @param stupnovitost
     * @param obsahAlkoholu
     * @param typ
     * @param typKvaseni
     * @param nazev
     */

    public Pivo(String nazevPivovaru, double stupnovitost, double obsahAlkoholu, String typ, String typKvaseni, String nazev) {
        this.nazevPivovaru = nazevPivovaru;
        this.stupnovitost = stupnovitost;
        this.obsahAlkoholu = obsahAlkoholu;
        this.typ = typ;
        this.typKvaseni = typKvaseni;
        this.nazev = nazev;
    }
}



