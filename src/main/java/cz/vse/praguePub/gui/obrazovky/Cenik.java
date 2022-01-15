package cz.vse.praguePub.gui.obrazovky;

import cz.vse.praguePub.gui.komponenty.Tabulka;
import cz.vse.praguePub.gui.obrazovky.abstraktniObrazovky.Obrazovka;
import cz.vse.praguePub.logika.dbObjekty.Pivo;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.layout.*;

import static cz.vse.praguePub.gui.komponenty.Komponenty.*;

public class Cenik extends Obrazovka<BorderPane> {

    private final String nazevPodniku;

    public Cenik(String nazevPodniku) {
        super(new BorderPane(),600,600,"background");
        this.nazevPodniku = nazevPodniku;

        this.vytvorGui();
    }

    private void vytvorGui(){
        this.getPane().setTop(
                HorniPanel(horniPanel -> {
                    Label nadpisOkna = NadpisOknaLabel(nazevPodniku);

                    Region separator = new Region();
                    HBox.setHgrow(separator, Priority.ALWAYS);

                    Button upravitButton = TlacitkoAplikace("Upravit", t-> {});
                    Button zpetButton = TlacitkoZpet(null);

                    horniPanel.getChildren().addAll(
                            Radek(nadpisOkna),separator, Radek(upravitButton,zpetButton)
                    );

                })
        );
        this.getPane().setCenter(
                pripravTabulku()
        );
    }

    private TableView<Pivo> pripravTabulku() {
        Tabulka<Pivo> pivoTabulka = new Tabulka<>(Pivo.PRO_TABULKU_CENIK);
        return pivoTabulka.getTableView();
    }
}
