package cz.vse.praguePub.gui.obrazovky;

import cz.vse.praguePub.gui.komponenty.Tabulka;
import cz.vse.praguePub.gui.obrazovky.abstraktniObrazovky.Obrazovka;
import cz.vse.praguePub.logika.dbObjekty.Pivo;
import cz.vse.praguePub.logika.dbObjekty.Podnik;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.layout.*;

import static cz.vse.praguePub.gui.komponenty.Komponenty.*;

public class Cenik extends Obrazovka<BorderPane> {

    private final ObservableList<Pivo> seznamPiv;
    private final Podnik zobrazovanyPodnik;
    private final Runnable tlacitkoZpetCallback;

    public Cenik(Podnik podnik, Runnable callback) {
        super(new BorderPane(),600,600,"background");
        this.zobrazovanyPodnik = podnik;
        this.tlacitkoZpetCallback = callback;
        this.seznamPiv = FXCollections.observableArrayList();

        this.nactiAtributyCeniku();
        this.vytvorGui();
    }

    private void nactiAtributyCeniku() {
        this.seznamPiv.clear();
        this.seznamPiv.addAll(this.zobrazovanyPodnik.getPivniListek());
    }


    private void vytvorGui(){
        this.getPane().setTop(
                HorniPanel(horniPanel -> {
                    Label nadpisOkna = NadpisOknaLabel(zobrazovanyPodnik.getNazev());

                    Region separator = new Region();
                    HBox.setHgrow(separator, Priority.ALWAYS);

                    Button upravitButton = TlacitkoAplikace("Upravit", t-> {});
                    Button zpetButton = TlacitkoZpet(null,t-> {
                        this.tlacitkoZpetCallback.run();
                    });

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
        pivoTabulka.getTableView().setItems(this.seznamPiv);


        return pivoTabulka.getTableView();
    }
}
