package cz.vse.praguePub.gui.komponenty;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.shape.SVGPath;

import java.io.InputStream;
import java.util.function.Consumer;

public final class Komponenty {

    public static Button TlacitkoAplikace(String text, Consumer<Button> styluj) {
        Button button = new Button(text);
        button.getStyleClass().add("tlacitkoAplikace");
        button.setAlignment(Pos.BASELINE_RIGHT);
        styluj.accept(button);
        return button;
    }

    public static HBox HorniPanel(Consumer<HBox> styluj) {
        HBox hbox = new HBox();
        hbox.getStyleClass().add("vrchniPanel");
        styluj.accept(hbox);
        return hbox;
    }
}