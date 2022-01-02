package cz.vse.praguePub.gui;


import com.mongodb.client.model.Filters;
import javafx.geometry.Insets;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;


import static cz.vse.praguePub.gui.komponenty.Komponenty.*;

public class Filtr extends Obrazovka <BorderPane> {
    private final Map<String, TextField> mapaInputu;
    private final Consumer<Bson> vysledek;

    public Filtr(Consumer<Bson> vysledek) {
        super(new BorderPane(), 350,200 , "background");
        this.mapaInputu = new HashMap<>();
        this.vysledek = vysledek;

        this.registrujInputy();
        this.vytvorGUI();
    }

    private void registrujInputy() {
        this.mapaInputu.putAll(
                Map.of(
                        "hodnoceni", TextFieldAplikace("Zadejte hodnocení", n -> {}),
                        "cena_piva", TextFieldAplikace("Zadejte cenu piva", n -> {}),
                        "znacka_piva", TextFieldAplikace("Zadejte značku Piva", n -> {})
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
                                this.mapaInputu.get("hodnoceni")
                        ),

                        Radek(
                                LabelAplikace("Cena piva:\t", l -> {}),
                                this.mapaInputu.get("cena_piva")
                        ),

                        Radek(
                                LabelAplikace("Značka piva:\t", l -> {}),
                                this.mapaInputu.get("znacka_piva")
                        ),

                        TlacitkoAplikace("Odeslat",
                                tlacitko -> tlacitko.setOnMouseClicked(
                                        onClickEvent -> {
                                            this.vysledek.accept(new Document());
                                        }
                                )
                        )
                    ),
                    sloupec -> {
                        sloupec.setPadding(new Insets(15));
                    }
                )
        );

    }
}
