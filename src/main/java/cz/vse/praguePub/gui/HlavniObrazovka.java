package cz.vse.praguePub.gui;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import static cz.vse.praguePub.gui.Komponenty.*;

public class HlavniObrazovka {
    private final Stage stage;
    private final BorderPane bp;

    public HlavniObrazovka() {
        this.bp = new BorderPane();
        this.stage = new Stage();

        bp.getStyleClass().add("background");
        this.nastaveni();

        Scene scene = new Scene(bp, 700, 700);
        scene.getStylesheets().add("style.css");
        stage.setScene(scene);
        stage.show();

    }

    private void nastaveni() {
        this.bp.setTop(
                horniPanel((horniPanel) -> {
                    Button prihlasitSe = tlacitkoAplikace("Prihlasit se", (t)->{});
                    Button oblibenePodniky = tlacitkoAplikace("Oblibene podniky", (t)->{});

                    TextField vyhledavani = new TextField("Vyhledat");
                    vyhledavani.getStyleClass().add("tlacitkoAplikace");
                    vyhledavani.autosize();

                    Label nazevLabel = new Label("Prague Pub");
                    nazevLabel.setFont(Font.font("Helvetica", FontWeight.BOLD, 30));
                    nazevLabel.setAlignment(Pos.BASELINE_LEFT);

                    horniPanel.getChildren().addAll(nazevLabel, vyhledavani, oblibenePodniky, prihlasitSe);
                })
        );
    }
}
