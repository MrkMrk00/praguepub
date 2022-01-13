package cz.vse.praguePub.gui.obrazovky;

import cz.vse.praguePub.gui.obrazovky.abstraktniObrazovky.Obrazovka;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;

import static cz.vse.praguePub.gui.komponenty.Komponenty.*;

public class Cenik extends Obrazovka<BorderPane> {

    public Cenik() {
        super(new BorderPane(),600,600,"background");

        this.vytvorGui();
    }

    private void vytvorGui(){
        this.getPane().setTop(
                HorniPanel(horniPanel -> {
                    Label nadpisOkna = NadpisOknaLabel("Ceník");

                    Region separator = new Region();
                    HBox.setHgrow(separator, Priority.ALWAYS);

                    Button upravitButton = TlacitkoAplikace("Upravit", t-> {});
                    Button zpetButton = TlacitkoZpet(null);

                    horniPanel.getChildren().addAll(
                            Radek(nadpisOkna),separator, Radek(upravitButton,zpetButton)
                    );

                })
        );
    }
}
