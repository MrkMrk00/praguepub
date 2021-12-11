package cz.vse.praguePub.gui;

import javafx.scene.Parent;
import javafx.scene.Scene;

public abstract class Obrazovka<T extends Parent> {
    protected final Scene scene;
    protected final T pane;

    protected Obrazovka(T toDisplay, double width, double height) {
        this.pane = toDisplay;
        this.scene = new Scene(toDisplay, width, height);
        this.scene.getStylesheets().add("style.css");
    }
}
