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

public class HlavniObrazovka {
    private final Stage stage;
    private final Scene scene;
    private final BorderPane bp;



    public HlavniObrazovka() {
        this.bp = new BorderPane();
        this.stage = new Stage();

        bp.getStyleClass().add("background");
        bp.setTop(horniBar());


        this.scene = new Scene(bp, 700,700);
        this.scene.getStylesheets().add("style.css");
        this.stage.setScene(this.scene);
        this.stage.show();
    }

    private HBox horniBar() {
        Button prihlasitSe = new Button("Prihlasit se");
        prihlasitSe.getStyleClass().add("tlacitkoAplikace");
        prihlasitSe.setAlignment(Pos.BASELINE_RIGHT);

        Button oblibenePodniky = new Button("Oblibene podniky");
        oblibenePodniky.getStyleClass().add("tlacitkoAplikace");
        oblibenePodniky.setAlignment(Pos.BASELINE_CENTER);


        TextField vyhledavani = new TextField("Vyhledat");
        vyhledavani.getStyleClass().add("tlacitkoAplikace");
        vyhledavani.autosize();



        HBox hbox = new HBox();
        Label nazevLabel = new Label("Prague Pub");
        nazevLabel.setFont(Font.font("Helvetica", FontWeight.BOLD, 30));
        nazevLabel.setAlignment(Pos.BASELINE_LEFT);
        hbox.getChildren().addAll(nazevLabel, vyhledavani, oblibenePodniky, prihlasitSe);

        hbox.getStyleClass().add("vrchniPanel");

        return hbox;
    }


}
