package cz.vse.praguePub.gui;


import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Font;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static cz.vse.praguePub.gui.komponenty.Komponenty.*;

public class Filtr extends Obrazovka <BorderPane>{
    private final Map<String, TextField> mapaInputu;

    public Filtr() {
        super(new BorderPane(), 320,200 , "background");
        this.mapaInputu = new HashMap<>();

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
        Label hlavniNadpisOkna = LabelAplikace("Filtr: ", l -> l.setFont(Font.font(30)));

        this.getPane().setTop(
                HorniPanel((horniPanel) -> {
                    horniPanel.getChildren().add(hlavniNadpisOkna);

                    if (obrazekIS != null) {
                        horniPanel.getChildren().add(
                                new ImageView(
                                        new Image(
                                                obrazekIS,
                                                40,
                                                40,
                                                true,
                                                false
                                        )
                                )
                        );
                    }
                })
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
                        )
                    ),
                    s -> {
                        s.setPadding(new Insets(15));
                    }
                )
        );

    }
}
