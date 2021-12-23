package cz.vse.praguePub.gui;


import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import static cz.vse.praguePub.gui.komponenty.Komponenty.*;

public class Filtr extends Obrazovka <BorderPane>{


    public Filtr() {
        super(new BorderPane(), 300,175 , "background");
        this.nastaveni();
    }

    private void nastaveni() {
        this.getPane().setTop(
                HorniPanel((horniPanel) -> {
                    Label nazevLabel = new Label("Filtr");
                    nazevLabel.setFont(Font.font("Helvetica", FontWeight.BOLD, 30));
                    nazevLabel.setAlignment(Pos.BASELINE_LEFT);

                    horniPanel.getChildren().addAll(nazevLabel);
                })
        );

        this.getPane().setRight(
                vBox((textFieldVBox)-> {
                    TextField zadejteHodnoceni = new TextField("Zadejte hodnocení");
                    zadejteHodnoceni.getStyleClass().add("tlacitkoAplikace");
                    zadejteHodnoceni.autosize();

                    TextField zadejteCenuPiva = new TextField("Zadejte cenu piva");
                    zadejteCenuPiva.getStyleClass().add("tlacitkoAplikace");
                    zadejteCenuPiva.autosize();

                    TextField zadejteZnackuPiva = new TextField("Zadejte značku Piva");
                    zadejteZnackuPiva.getStyleClass().add("tlacitkoAplikace");
                    zadejteZnackuPiva.autosize();

                    textFieldVBox.getChildren().addAll(zadejteHodnoceni, zadejteCenuPiva, zadejteZnackuPiva);
                })
        );
        this.getPane().setLeft(
                vBox((textFieldVBox)-> {
                    Label hodnoceni = new Label("Hodnocení: ");
                    hodnoceni.setFont(Font.font("Helvetica", FontWeight.BOLD,12));
                    hodnoceni.setAlignment(Pos.BASELINE_LEFT);
                    hodnoceni.getStyleClass().add("tlacitkoAplikace");

                    Label cenaPiva = new Label("Cena piva: ");
                    cenaPiva.setFont(Font.font("Helvetica", FontWeight.BOLD,12));
                    cenaPiva.setAlignment(Pos.BASELINE_LEFT);
                    cenaPiva.getStyleClass().add("tlacitkoAplikace");

                    Label znackaPiva = new Label("Značka piva: ");
                    znackaPiva.setFont(Font.font("Helvetica", FontWeight.BOLD,12));
                    znackaPiva.setAlignment(Pos.BASELINE_LEFT);
                    znackaPiva.getStyleClass().add("tlacitkoAplikace");

                    textFieldVBox.getChildren().addAll(hodnoceni, cenaPiva, znackaPiva);
                })
        );
    }
}
