package cz.vse.praguePub.gui.obrazovky;

import cz.vse.praguePub.gui.obrazovky.abstraktniObrazovky.Obrazovka;
import javafx.geometry.Insets;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import lombok.Data;
import org.bson.Document;

import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import static cz.vse.praguePub.gui.komponenty.Komponenty.*;

public class Filtr extends Obrazovka<BorderPane> {

    @Data private static class AtributFilteru {
        private final String identifikacniText;
        private final String textProZobrazeni;
        private final TextField filtr;
    }

    public static final List<AtributFilteru> FILTR_PODNIKY = List.of(
            new AtributFilteru("nazev", "Název", TextFieldAplikace("", null)),
            new AtributFilteru("mc_cislo", "Číslo MČ", TextFieldAplikace("", null)),
            new AtributFilteru("mc_nazev", "Název MČ", TextFieldAplikace("", null)),
            new AtributFilteru("ulice", "Ulice", TextFieldAplikace("", null)),
            new AtributFilteru("cp", "Číslo popisné", TextFieldAplikace("", null)),
            new AtributFilteru("psc", "PSČ", TextFieldAplikace("", null)),
            new AtributFilteru("mc_cislo", "Číslo MČ", TextFieldAplikace("", null))
    );

    private final Consumer<Map<String, Object>> callbackSVysledkem;

    public Filtr(Consumer<Map<String, Object>> callbackSVysledkem) {
        super(new BorderPane(), 350,250 , "background");
        this.callbackSVysledkem = callbackSVysledkem;

        this.registrujInputy();
        this.vytvorGUI();
    }

    private void registrujInputy() {
        this.getMapaInputu().putAll(
                Map.of(
                        "hodnoceni",    TextFieldAplikace("", n -> {}),
                        "cena_piva",    TextFieldAplikace("", n -> {}),
                        "znacka_piva",  TextFieldAplikace("", n -> {})
                )
        );
    }

    private void vytvorGUI() {
        InputStream obrazekIS = this.getClass().getResourceAsStream("/filtr.png");
        ImageView obrazekFiltru =
                (obrazekIS != null) ?
                        new ImageView(
                            new Image(obrazekIS, 40, 40, true, false)
                        )
                        : null;


        this.getPane().setTop(
                HorniPanel(
                        (horniPanel) -> {
                            if (obrazekFiltru != null) horniPanel.getChildren().add(obrazekFiltru);
                            horniPanel.getChildren().add(NadpisOknaLabel("Filtr"));
                        }
                )
        );

        this.getPane().setCenter(
                Sloupec(List.of(
                        Radek(
                                LabelAplikace("Hodnocení:\t", l -> {}),
                                this.getMapaInputu().get("hodnoceni")
                        ),

                        Radek(
                                LabelAplikace("Cena piva:\t", l -> {}),
                                this.getMapaInputu().get("cena_piva")
                        ),

                        Radek(
                                LabelAplikace("Značka piva:\t", l -> {}),
                                this.getMapaInputu().get("znacka_piva")
                        ),

                        TlacitkoAplikace("Odeslat",
                                tlacitko -> tlacitko.setOnMouseClicked(
                                        onClickEvent -> this.callbackSVysledkem.accept(new Document()) //zde přijde výsledny Bson filter (filter požadován uživatelem)
                                )
                        )
                    ),
                    sloupec -> {
                        sloupec.setPadding(new Insets(15));
                    }
                )
        );
    }

    private Map<String, Object> zpracujFiltr() {
        String hodnoceni = this.getMapaInputu().get("hodnoceni").getText();
        String cenaPiva = this.getMapaInputu().get("cena_piva").getText();
        String nazevPivovaru = this.getMapaInputu().get("znacka_piva").getText();


        return Map.of(
                "hodnoceni", hodnoceni,
                "cena_piva", cenaPiva,
                "nazev_pivovaru", nazevPivovaru
        );
    }
}
