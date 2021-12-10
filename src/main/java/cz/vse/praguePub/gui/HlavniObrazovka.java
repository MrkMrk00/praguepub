package cz.vse.praguePub.gui;

import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class HlavniObrazovka {
    private final Stage stage;
    private final Scene scene;
    private final BorderPane bp;
    private final HBox hbox;


    public HlavniObrazovka() {
        this.bp = new BorderPane();
        this.stage = new Stage();

        bp.getStyleClass().add("background");
        this.hbox = new HBox();
        Label nazevLabel = new Label("Prague Pub");
        nazevLabel.setFont(Font.font("Helvetica", FontWeight.BOLD, 25));
        this.hbox.getChildren().addAll(nazevLabel);
        this.hbox.getStyleClass().add("vrchniPanel");
        this.bp.setTop(hbox);





        this.scene = new Scene(bp, 700,700);
        this.scene.getStylesheets().add("style.css");
        this.stage.setScene(this.scene);
        this.stage.show();
    }

}
