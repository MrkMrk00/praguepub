package cz.vse.praguePub.gui;

import javafx.scene.Parent;
import javafx.scene.Scene;

/**
 * Třída, která obsahuje boilerplate kód pro vytváření obrazovek
 * @param <T> hlavní Parent obrazovky (např. BorderPane)
 */
public abstract class Obrazovka<T extends Parent> {
    private final Scene scene;
    private final T pane;

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
    }

    /**
     * Metoda slouží jako odkaz k hlavnímu Parentu scény.
     * @return ? extends Parent (který byl při extendování použit)
     */
    protected T getPane() {
        return this.pane;
    }

    /**
     * @return scénu obrazovky
     */
    public Scene getScene() {
        return this.scene;
    }
}
