package cz.vse.praguePub.gui;

import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class HlavniObrazovka    {
    public HlavniObrazovka(BorderPane borderPane) {
        this.borderPane = borderPane;
       Stage HlObr = new Stage();
       HlObr.setScene(new Scene(borderPane));
       HlObr.show();
    }

    BorderPane borderPane = new BorderPane();

}
