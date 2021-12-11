package cz.vse.praguePub.gui;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;

import java.util.function.Consumer;

public final class Komponenty {

    public static Button tlacitkoAplikace(String text, Consumer<Button> styluj) {
        Button button = new Button(text);
        button.getStyleClass().add("tlacitkoAplikace");
        button.setAlignment(Pos.BASELINE_RIGHT);
        styluj.accept(button);
        return button;
    }

    public static HBox horniPanel(Consumer<HBox> styluj) {
        HBox hbox = new HBox();
        hbox.getStyleClass().add("vrchniPanel");
        styluj.accept(hbox);
        return hbox;
    }
}