package cz.vse.praguePub.start;

import cz.vse.praguePub.gui.ObrazovkyController;
import javafx.application.Application;
import javafx.stage.Stage;

public class FXApp extends Application {

    public static void main(String[] args) {
        Application.launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        /*Databaze db = null;
        try {
             db = Databaze.get(Uzivatel.guest());
        } catch (PraguePubDatabaseException e) {
            e.printStackTrace();
        }
        if (db == null) {
            System.out.println("dosralo se to");
            return;
        }*/

        /*var st = new Stage();
        st.setScene(new ZobrazitPodnik(db).getScene());
        st.show();

        var st2 = new Stage();
        st2.setScene(new Filtr(Filtr.FILTR_PIVA_S_CENOU, null).getScene());
        st2.show();*/

        ObrazovkyController contr = new ObrazovkyController();
        contr.zobrazInformaceOPodniku(contr.getDatabaze().getPodnikFiltrBuilder().finalizuj().get(0));
        contr.zapniAplikaci(primaryStage);
    }
}
