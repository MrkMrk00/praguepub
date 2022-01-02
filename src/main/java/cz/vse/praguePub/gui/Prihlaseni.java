package cz.vse.praguePub.gui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import static cz.vse.praguePub.gui.komponenty.Komponenty.*;

public class Prihlaseni extends Obrazovka<BorderPane> {

    public Prihlaseni() {
        super(new BorderPane(), 400, 400, "background");
        this.nastaveni();
    }

    private void nastaveni() {
        /*this.getPane().setTop(
                Sloupec((textFieldVBox) -> {
        Button prihlasitSe = TlacitkoAplikace("Prihlasit se", (t)->{});

        TextField login = new TextField("Jméno");
        login.getStyleClass().add("tlacitkoAplikace");
        login.setMaxWidth(150.0);
        textFieldVBox.setAlignment(Pos.BASELINE_CENTER);


        TextField password = new TextField("Heslo");
        password.getStyleClass().add("tlacitkoAplikace");
        password.setMaxWidth(150.0);

        Label nazevLabel = new Label("Vítej zpět!");
        nazevLabel.setFont(Font.font("Helvetica", FontWeight.BOLD, 30));
        nazevLabel.setAlignment(Pos.BASELINE_CENTER);

        Hyperlink link = new Hyperlink("Vytvořit účet");
        link.setFont(Font.font("Helvetica", FontWeight.BOLD, 10));
        link.setAlignment(Pos.BASELINE_CENTER);

        textFieldVBox.getChildren().addAll(nazevLabel,login,password,prihlasitSe,link);
                    textFieldVBox.setSpacing(20);
                    textFieldVBox.setPadding(new Insets(60,0,20,0));
                })
        );*/
    }
}



