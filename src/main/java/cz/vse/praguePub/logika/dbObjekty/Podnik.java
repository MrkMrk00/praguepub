package cz.vse.praguePub.logika.dbObjekty;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@EqualsAndHashCode
@AllArgsConstructor
public class Podnik {
    @Getter private final String nazev;
    @Getter final int adresa_mc_cislo;
    @Getter final String adresa_mc_nazev;
    @Getter final String adresa_psc;
    @Getter final String adresa_ulice;
    @Getter final Integer adresa_cp;
    final List<Recenze> recenze;
    final Map<String, Pivo> pivniListek;

    public List<Recenze> getRecenze() {
        return Collections.unmodifiableList(this.recenze);
    }

    public Map<String, Pivo> getPivniListek() {
        return Collections.unmodifiableMap(this.pivniListek);
    }
}
