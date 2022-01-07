package cz.vse.praguePub.gui;

import cz.vse.praguePub.gui.komponenty.Tabulka;
import cz.vse.praguePub.logika.dbObjekty.Podnik;
import cz.vse.praguePub.logika.dbObjekty.Recenze;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.List;

import static cz.vse.praguePub.gui.komponenty.Komponenty.*;

public class ZobrazitPodnik extends Obrazovka<BorderPane>{

    public ZobrazitPodnik() {
        super(new BorderPane(), 700, 700, "background" );
        this.vytvorGUI();
    }

    private void vytvorGUI() {
        this.getPane().setTop(
                HorniPanel(hp -> {
                            BorderPane vrchniBar = new BorderPane();
                            vrchniBar.setLeft(
                                    Radek(
                                            NadpisOknaLabel("Hostinec V Zátiší")

                                            //nějakym stylem udělat hvězdičky recenze
                                    )
                            );

                            vrchniBar.setRight(
                                    Radek(
                                            //tlačítko ve tvaru srdíčka pro přidaní do obl.
                                            TlacitkoAplikace("Upravit", (t) ->{
                                                HBox.setMargin(t, new Insets(6,8,0,5));
                                            })
                                            //TlacitkoZpet((t) ->{})
                                            //tlačítko pro zpět


                                    )
                            );

                            hp.getChildren().add(vrchniBar);
                            hp.setPrefWidth(Integer.MAX_VALUE);
                            vrchniBar.setPrefWidth(Integer.MAX_VALUE);
                        }
                )
        );

        this.getPane().setLeft(
                Sloupec(List.of(
                                LabelAplikace("Adresa:"),
                                TlacitkoAplikace("Ceník", (t)->{
                                    VBox.setMargin(t, new Insets(10,0,0,20));}),
                                TlacitkoAplikace("Přidat recenzi", (t)->{
                                    VBox.setMargin(t, new Insets(10,0,0,20));
                                })

                        ), sloupec -> {}
                )

        );

        /*this.getPane().setBottom(
                pripravTabulku()
        );*/
    }

    /*private TableView<Recenze> pripravTabulku() {
        Tabulka<Recenze> recenzeTabulka = new Tabulka<>(Recenze.inicializujZDokumentu());
        return recenzeTabulka.getTableView();
    }*/
}
