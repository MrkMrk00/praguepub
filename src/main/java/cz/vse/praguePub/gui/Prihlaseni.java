package cz.vse.praguePub.gui;

import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Prihlaseni {

    public Prihlaseni(BorderPane borderPane) {
        this.borderPane = borderPane;
        Stage prihlObr = new Stage();
        prihlObr.setTitle("Prihlaseni");
        borderPane.setStyle("-fx-background-color: #92d36e");
        prihlObr.setScene(new Scene(borderPane, 450,450));
        prihlObr.show();


        VBox vBox = new VBox(new Label("A JavaFX Label"));
        Scene scene = new Scene(vBox);
    }

    BorderPane borderPane = new BorderPane();

}





   /*/ private void pripravScenuAStage(Stage primaryStage) {
        Scene scene = new Scene(menuAHraVBox, 822, 650);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Hon za Pokladem");
        primaryStage.show();
        primaryStage.setOnCloseRequest(event -> {
            Platform.exit();
            System.exit(0);
        });/*/
