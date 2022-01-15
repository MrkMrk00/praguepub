package cz.vse.praguePub.gui.komponenty;

import cz.vse.praguePub.logika.Vysledek;
import cz.vse.praguePub.logika.dbObjekty.DBObjekt;
import cz.vse.praguePub.logika.dbObjekty.Pivo;
import cz.vse.praguePub.logika.dbObjekty.Podnik;
import cz.vse.praguePub.logika.dbObjekty.Recenze;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.DialogEvent;
import lombok.Getter;

public class AlertBuilder {
    @Getter private final Alert alert;
    
    public AlertBuilder(Alert.AlertType alertType) {
        this.alert = new Alert(alertType);
    }
    
    public AlertBuilder setTitle(String title) {
        this.alert.setTitle(title);
        return this;
    }
    
    public AlertBuilder setHeaderText(String text) {
        this.alert.setHeaderText(text);
        return this;
    }
    
    public AlertBuilder setContent(String text) {
        this.alert.setContentText(text);
        return this;
    }

    public AlertBuilder setGraphic(Node graphic) {
        this.alert.setGraphic(graphic);
        return this;
    }

    public AlertBuilder setOnCloseRequest(EventHandler<DialogEvent> eventHandler) {
        this.alert.setOnCloseRequest(eventHandler);
        return this;
    }

    public static Alert ChybaDatabazeAlert(String chybaText) {
        return new AlertBuilder(Alert.AlertType.ERROR)
                .setTitle("PraguePub")
                .setHeaderText("Chyba databáze")
                .setContent(chybaText)
                .getAlert();
    }

    public static <T extends DBObjekt> Alert OpravduPokracovatAlert(Vysledek<T> vysledek) {
        T podobnyGen = vysledek.getNajity();
        String popis = null;

        if (Podnik.class.equals(vysledek.getDBObjektClass())) {
            Podnik podobny = (Podnik) podobnyGen;
            popis = String.format("""
                        %s
                        %s %s, Praha %d-%s
                        """,
                    podobny.getNazev(),
                    podobny.getAdresa_ulice(), podobny.getAdresa_cp(),
                    podobny.getAdresa_mc_cislo(), podobny.getAdresa_mc_nazev()
            );
        }

        else if (Pivo.class.equals(vysledek.getDBObjektClass())) {
            Pivo podobne = (Pivo) podobnyGen;
            popis = String.format("%s %s", podobne.getNazevPivovaru(), podobne.getNazev());
        }

        return new AlertBuilder(Alert.AlertType.CONFIRMATION)
                .setTitle("Prague Pub")
                .setHeaderText("Opravdu chcete pokračovat?")
                .setContent(
                        vysledek.getZprava() + "\n"
                                + (Podnik.class.equals(vysledek.getDBObjektClass()) ? "Podnik v databázi:" : "Pivo v databázi:")
                                + "\n"
                                + popis
                )
                .getAlert();
    }

    public static <T extends DBObjekt> Alert UspesneVlozenObjekt(T vlozenyObjekt) {
        String zprava = null;

        if (vlozenyObjekt instanceof Podnik)
            zprava = String.format("Úspěšně vložen podnik %s do databáze!", ((Podnik)vlozenyObjekt).getNazev());

        else if (vlozenyObjekt instanceof Pivo)
            zprava = String.format("Úspěšně vloženo pivo %s do databáze!", ((Pivo)vlozenyObjekt).getNazev());

        else if (vlozenyObjekt instanceof Recenze)
            zprava = "Recenze byla přidána";

        return new AlertBuilder(Alert.AlertType.INFORMATION)
                .setTitle("Prague Pub")
                .setHeaderText("Povedlo se!")
                .setContent(zprava != null ? zprava : "")
                .getAlert();
    }
}
