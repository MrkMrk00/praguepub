package cz.vse.praguePub.logika;

import java.util.List;
import java.util.Map;

public class Podnik {
    final String nazev;
    //enum lokace
    final String adresa;
    final List<Recenze> recenze;
    final Map<String,Pivo> pivniListek;

    public Podnik(String nazev, String adresa, List<Recenze> recenze, Map<String, Pivo> pivniListek) {
        this.nazev = nazev;
        this.adresa = adresa;
        this.recenze = recenze;
        this.pivniListek = pivniListek;
    }
    public void zalozRecenzi(Recenze recenze){
        this.recenze.add(recenze);
    }
    public void upravitPodnik(){
        //udelat
    }
    public void pridatPivo(Pivo pivo){
        pivniListek.put(pivo.getNazev(),pivo);
    }
    public void odebratPivo(Pivo pivo){
        pivniListek.remove(pivo.getNazev());
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

    public Map<String, Pivo> getPivniListek() {
        return pivniListek;
    }
}
