package cz.vse.praguePub.gui;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import lombok.Getter;

public final class Komponenty {
    public static abstract class HorniBar {
        @Getter private final HBox horniPanel;

        public HorniBar() {
            this.horniPanel = new HBox();
            this.horniPanel.getStyleClass().add("vrchniPanel");
            this.vlozPolozky(this.horniPanel);
        }

        abstract void vlozPolozky(HBox horniPanel);
    }

    public static Button tlacitkoAplikace(String text) {
        Button button = new Button(text);
        button.getStyleClass().add("tlacitkoAplikace");
        button.setAlignment(Pos.BASELINE_RIGHT);
        return button;
    }
}