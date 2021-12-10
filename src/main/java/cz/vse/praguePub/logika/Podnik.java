package cz.vse.praguePub.logika;

import java.util.List;
import java.util.Map;

public class Podnik {
    final String nazev;
    //enum lokace
    final String adresa;
    final List<Recenze> recenze;
    final Map<String,Object> pivniListek;

    public Podnik(String nazev, String adresa, List<Recenze> recenze, Map<String, Object> pivniListek) {
        this.nazev = nazev;
        this.adresa = adresa;
        this.recenze = recenze;
        this.pivniListek = pivniListek;
    }



    public String getNazev() {
        return nazev;
    }

    public String getAdresa() {
        return adresa;
    }

    public List<Recenze> getRecenze() {
        return recenze;
    }

    public Map<String, Object> getPivniListek() {
        return pivniListek;
    }
}
