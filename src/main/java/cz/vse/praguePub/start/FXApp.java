package cz.vse.praguePub.start;

import cz.vse.praguePub.gui.ObrazovkyController;
import cz.vse.praguePub.gui.obrazovky.Cenik;
import javafx.application.Application;
import javafx.stage.Stage;

public class FXApp extends Application {

    public static void main(String[] args) {
        Application.launch(args);
    }

    @Override
    public void start(Stage primaryStage) {

        var st = new Stage();
        st.setScene(new Cenik().getScene());
        st.showAndWait();

        new ObrazovkyController().zapniAplikaci(primaryStage);
    }
}
