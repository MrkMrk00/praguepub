package cz.vse.praguePub.gui.obrazovky;

import cz.vse.praguePub.gui.obrazovky.abstraktniObrazovky.Obrazovka;
import cz.vse.praguePub.logika.dbObjekty.Pivo;
import javafx.scene.layout.BorderPane;
import org.apache.commons.lang3.math.NumberUtils;

import java.util.Map;

import static cz.vse.praguePub.gui.komponenty.Komponenty.*;

public class ZadejCenuAObjem extends Obrazovka<BorderPane> {

    private final Pivo upravovanePivo;
    private final Runnable zavriOkno;

    public ZadejCenuAObjem(Pivo pivo, Runnable callback) {
        super(new BorderPane(), 340, 150, "background");
        this.upravovanePivo = pivo;
        this.zavriOkno = callback;

        this.registrujInputy();
        this.pripravGUI();
    }

    private void nastavPivo() {
        double cena = NumberUtils.toDouble(this.getMapaInputu().get("cena").getText(), -1);
        double objem = NumberUtils.toDouble(this.getMapaInputu().get("objem").getText(), -1);
        if (cena != -1) this.upravovanePivo.setCena(cena);
        if (objem != -1) this.upravovanePivo.setObjem(objem);
        this.zavriOkno.run();
    }

    private void registrujInputy() {
        this.getMapaInputu().putAll(
                Map.of(
                        "cena",     TextFieldAplikace("", null),
                        "objem",    TextFieldAplikace("", null)
                )
        );
    }

    private void pripravGUI() {
        this.getPane().setCenter(
                Sloupec(
                        Radek(LabelAplikace("Cena:\t"), this.getMapaInputu().get("cena")),
                        Radek(LabelAplikace("Objem:\t"), this.getMapaInputu().get("objem")),
                        Radek(TlacitkoAplikace(
                                "Odeslat",
                                onClickEvent -> this.nastavPivo(),
                                t -> {}
                                )
                        )
                )
        );
    }

}
