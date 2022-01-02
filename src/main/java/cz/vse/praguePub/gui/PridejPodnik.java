package cz.vse.praguePub.gui;

import cz.vse.praguePub.logika.dbObjekty.Podnik;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

import static cz.vse.praguePub.gui.komponenty.Komponenty.*;

public class PridejPodnik extends Obrazovka<BorderPane> {

    private final static Logger log = LoggerFactory.getLogger(PridejPodnik.class);

    public PridejPodnik() {
        super(new BorderPane(), 400, 600, "background");
        this.registrujInputy();
        this.vytvorGUI();
    }

    private void registrujInputy() {
        this.getMapaInputu().putAll(
                Map.of(
                        "nazev",    TextFieldAplikace("Název podniku", t -> {}),
                        "mc_cislo", TextFieldAplikace("Číslo městské části", t -> {}),
                        "mc_nazev", TextFieldAplikace("Název městské části", t -> {}),
                        "psc",      TextFieldAplikace("Poštovní směr. číslo", t -> {}),
                        "ulice",    TextFieldAplikace("Ulice", t -> {}),
                        "cp",       TextFieldAplikace("Číslo popisné", t -> {})
                )
        );
    }

    private void parsuj() {
        log.debug(new Podnik(
                this.getMapaInputu().get("nazev").getText(),
                Integer.parseInt(this.getMapaInputu().get("mc_cislo").getText()),
                this.getMapaInputu().get("mc_nazev").getText(),
                this.getMapaInputu().get("ulice").getText(),
                this.getMapaInputu().get("psc").getText(),
                Integer.parseInt(this.getMapaInputu().get("cp").getText()),
                new HashSet<>(),
                new HashMap<>()
        ).toString());
    }

    private void vytvorGUI() {
        this.getPane().setTop(
                HorniPanel(
                    horniPanel -> {
                        horniPanel.getChildren().add(NadpisOknaLabel("Přidat nový podnik"));
                        horniPanel.setAlignment(Pos.CENTER);
                    }
                )
        );

        VBox inputy = Sloupec(List.of(
                Radek(LabelAplikace("Název podniku:"), this.getMapaInputu().get("nazev")),
                Radek(LabelAplikace("Adresa:")),
                Radek(
                        Sloupec(List.of(), sloupec -> { sloupec.setFillWidth(true); sloupec.setPrefWidth(20);}),
                        Sloupec(
                                Radek(LabelAplikace("Číslo MČ:"), this.getMapaInputu().get("mc_cislo")),
                                Radek(LabelAplikace("Název MČ:"), this.getMapaInputu().get("mc_nazev")),
                                Radek(LabelAplikace("Ulice:"), this.getMapaInputu().get("ulice")),
                                Radek(LabelAplikace("PSČ:"), this.getMapaInputu().get("psc")),
                                Radek(LabelAplikace("Číslo popisné:"), this.getMapaInputu().get("cp"))
                        )
                ),
                Radek(TlacitkoAplikace("Parsuj", t -> {
                    t.setOnAction(event -> this.parsuj());
                }))
                ),
                inputySloupec -> {
                    inputySloupec.setPadding(new Insets(20));
                    inputySloupec.setFillWidth(true);
                }
        );

        this.getPane().setCenter(inputy);
    }
}
