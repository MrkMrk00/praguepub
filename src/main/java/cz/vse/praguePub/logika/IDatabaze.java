package cz.vse.praguePub.logika;

import cz.vse.praguePub.logika.dbObjekty.Pivo;
import cz.vse.praguePub.logika.dbObjekty.Podnik;

import java.util.List;
import java.util.Set;

public interface IDatabaze {

    Set<Podnik> getPodnikyVMestskeCasti(int mestskaCast);

    Set<Podnik> getPodnikyPodlePiva(Pivo pivo);

    Set<Podnik> getPodnikyPodleNazvu(String nazev);
}
