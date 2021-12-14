package cz.vse.praguePub.start;

import cz.vse.praguePub.logika.Databaze;
import cz.vse.praguePub.logika.Uzivatel;
import javafx.application.Application;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import cz.vse.praguePub.util.AlertBuilder;

public class FXApp extends Application {

    public static void main(String[] args) {
        new Databaze(Uzivatel.guest()).getPodnikyVMestskeCasti(6).forEach(it -> System.out.println(it.getNazev()));
        Application.launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        new AlertBuilder(Alert.AlertType.INFORMATION)
                .setHeaderText("Zdar!")
                .setContent("Týmová semestrální práce")
                .getAlert()
                .showAndWait();
    }
}
