package cz.vse.praguePub.gui;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;

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
                                                HBox.setMargin(t, new Insets(6,0,0,5));
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
    }
}
