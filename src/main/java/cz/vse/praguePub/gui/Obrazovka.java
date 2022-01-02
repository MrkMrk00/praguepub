package cz.vse.praguePub.gui;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import lombok.AccessLevel;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

/**
 * Třída, která obsahuje boilerplate kód pro vytváření obrazovek
 * @param <T> hlavní Parent obrazovky (např. BorderPane)
 */
public abstract class Obrazovka<T extends Parent> {
    @Getter private final Scene scene;
    @Getter(AccessLevel.PROTECTED) private final T pane;
    @Getter(AccessLevel.PROTECTED) private final Map<String, TextField> mapaInputu;

    protected Obrazovka(
            T toDisplay,
            double width,
            double height,
            String... styleClasses
    ) {
        this.pane = toDisplay;
        this.scene = new Scene(toDisplay, width, height);
        this.scene.getStylesheets().add("style.css");
        this.pane.getStyleClass().addAll(styleClasses);
        this.mapaInputu = new HashMap<>();
    }
}
