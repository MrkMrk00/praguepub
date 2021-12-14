package cz.vse.praguePub.logika;

import cz.vse.praguePub.logika.dbObjekty.Pivo;
import cz.vse.praguePub.logika.dbObjekty.Podnik;

import java.util.List;

public interface IDatabaze {

    List<Podnik> getPodnikyVMestskeCasti(int mestskaCast);

    List<Podnik> getPodnikyPodlePiva(Pivo pivo);

    List<Podnik> getPodnikyPodleNazvu(String nazev);
}
