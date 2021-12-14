package cz.vse.praguePub.logika.dbObjekty;

import lombok.Data;
import org.bson.Document;

import java.util.*;

@Data
public class Podnik implements DBObjekt {
    private final String nazev;

    private final int adresa_mc_cislo;
    private final String adresa_mc_nazev;
    private final String adresa_psc;
    private final String adresa_ulice;
    private final Integer adresa_cp;

    private final Set<Recenze> recenze;
    private final Set<Pivo> pivniListek;

    public static Podnik getFromDocument(Document doc) {
        Set<Recenze> recenzeLst = new HashSet<>();
        doc.get("recenze", Document.class).forEach((key, recenze) -> recenzeLst.add(Recenze.getFromDocument((Document)recenze)));

        Set<Pivo> pivniListek = new HashSet<>();
        Document adresa = doc.get("adresa", Document.class);

        return new Podnik(
                doc.get("jmeno", String.class),
                adresa.get("mc_cislo", Integer.class),
                adresa.get("mc_nazev", String.class),
                adresa.get("ulice", String.class),
                adresa.get("psc", String.class),
                adresa.get("cp", Integer.class),
                recenzeLst,
                pivniListek);
    }

    public Set<Recenze> getRecenze() {
        return Collections.unmodifiableSet(this.recenze);
    }

    public Set<Pivo> getPivniListek() {
        return Collections.unmodifiableSet(this.pivniListek);
    }

    @Override
    public Document toDocument() {
        return null;
    }
}
