package cz.vse.praguePub.gui;

import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class HlavniObrazovka {
    private final Stage stage;
    private final Scene scene;
    private final BorderPane bp;

    public HlavniObrazovka() {
        this.bp = new BorderPane();
        this.stage = new Stage();
        this.obrys();

        bp.setStyle("-fx-background-color: #92d36e");


        this.scene = new Scene(this.bp, 700,700);
        this.stage.setScene(this.scene);
        this.stage.show();
    }

    private void obrys() {
        Label lab = new Label("PraguePub");
        lab.setFont(Font.font(25));
        this.bp.setTop(lab);
    }
}
