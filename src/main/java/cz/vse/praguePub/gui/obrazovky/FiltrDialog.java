package cz.vse.praguePub.gui.obrazovky;

import cz.vse.praguePub.gui.obrazovky.abstraktniObrazovky.Obrazovka;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import lombok.Data;

import java.io.InputStream;
import java.util.*;
import java.util.function.Consumer;

import static cz.vse.praguePub.gui.komponenty.Komponenty.*;

public class FiltrDialog extends Obrazovka<BorderPane> {

    @Data public static class AtributFilteru {
        private final String textProZobrazeni;
        private final TextField filtr;
    }

    public static final Map<String, AtributFilteru> FILTR_PODNIKY;
    public static final Map<String, AtributFilteru> FILTR_PIVA;
    public static final Map<String, AtributFilteru> FILTR_PIVA_S_CENOU;

    static {
        Map<String, AtributFilteru> podniky = new LinkedHashMap<>();
        podniky.put("nazev",    new AtributFilteru("Název", TextFieldAplikace("", null)));
        podniky.put("mc_cislo", new AtributFilteru("Číslo MČ", TextFieldAplikace("", null)));
        podniky.put("mc_nazev", new AtributFilteru("Název MČ", TextFieldAplikace("", null)));
        podniky.put("ulice",    new AtributFilteru("Ulice", TextFieldAplikace("", null)));
        podniky.put("cp",       new AtributFilteru("Číslo popisné", TextFieldAplikace("", null)));
        podniky.put("psc",      new AtributFilteru("PSČ", TextFieldAplikace("", null)));
        FILTR_PODNIKY = Collections.unmodifiableMap(podniky);

        Map<String, AtributFilteru> piva = new LinkedHashMap<>();
        piva.put("nazev",               new AtributFilteru("Název", TextFieldAplikace("", null)));
        piva.put("pivovar",             new AtributFilteru("Název pivovaru", TextFieldAplikace("", null)));
        piva.put("stupnovitost",        new AtributFilteru("Stupňovitost", TextFieldAplikace("", null)));
        piva.put("obsah_alkoholu",      new AtributFilteru("Obsah alkoholu", TextFieldAplikace("", null)));
        piva.put("typ",                 new AtributFilteru("Typ piva", TextFieldAplikace("", null)));
        piva.put("typ_kvaseni",         new AtributFilteru("Typ kvašení", TextFieldAplikace("", null)));
        FILTR_PIVA = Collections.unmodifiableMap(piva);

        Map<String, AtributFilteru> piva_s_cenou = new LinkedHashMap<>(piva);
        piva_s_cenou.put("cena",  new AtributFilteru("Cena", TextFieldAplikace("", null)));
        FILTR_PIVA_S_CENOU = Collections.unmodifiableMap(piva_s_cenou);
    }

    private final Consumer<Map<String, String>> callbackSVysledkem;
    private final Map<String, AtributFilteru> atributy;

    public FiltrDialog(Map<String, AtributFilteru> atributy, Consumer<Map<String, String>> callbackSVysledkem) {
        super(new BorderPane(), 350, (atributy.size() * 40) + 100, "background");

        this.callbackSVysledkem = callbackSVysledkem;
        this.atributy = atributy;

        this.vytvorGUI();
    }

    private void vytvorGUI() {
        this.getPane().setOnKeyPressed(
                keyEvent -> {
                    if (keyEvent.getCode() == KeyCode.ENTER) this.zpracujFiltr();
                }
        );

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

        List<Node> nastred = new ArrayList<>();
        for (AtributFilteru atr : this.atributy.values()) {
            nastred.add(Radek(LabelAplikace(atr.getTextProZobrazeni()), atr.getFiltr()));
        }
        nastred.add(TlacitkoAplikace("Odeslat", t -> this.zpracujFiltr(), null));
        this.getPane().setCenter(Sloupec(nastred, sl -> {
            sl.setPadding(new Insets(10));
        }));
    }

    private void zpracujFiltr() {
        Map<String, String> kVraceni = new HashMap<>();
        this.atributy.forEach(
                (key, atrFiltru) -> kVraceni.put(key, atrFiltru.getFiltr().getText())
        );
        this.callbackSVysledkem.accept(kVraceni);
    }
}
