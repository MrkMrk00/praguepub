package cz.vse.praguePub.gui;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import static cz.vse.praguePub.gui.Komponenty.horniPanel;
import static cz.vse.praguePub.gui.Komponenty.tlacitkoAplikace;

public class ZobrazitSeznamVLokaci extends Obrazovka<BorderPane> {
    private final Stage stage;

    public ZobrazitSeznamVLokaci(){
        super(new BorderPane(), 700, 700);
        this.stage = new Stage();
        pane.getStyleClass().add("background");
        this.nastaveni();
        stage.setScene(scene);
        stage.show();
    }

    private void nastaveni() {
        this.pane.setTop(
                horniPanel((horniPanel) ->{
                    Label nazevLokace = new Label("Praha 10");
                    nazevLokace.setFont(Font.font("Helvetica", FontWeight.BOLD, 30));
                    nazevLokace.setAlignment(Pos.BASELINE_LEFT);

                    Button pridatNovyPodnik = tlacitkoAplikace("Pridat novy podnik", (t)->{});
                    Button filtrovat = tlacitkoAplikace("Filtrovat", (t)->{});

                    horniPanel.getChildren().addAll(nazevLokace, pridatNovyPodnik, filtrovat);
                })
        );
    }

}
