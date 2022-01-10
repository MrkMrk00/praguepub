package cz.vse.praguePub.gui.obrazovky;

import cz.vse.praguePub.gui.komponenty.AlertBuilder;
import cz.vse.praguePub.gui.obrazovky.abstraktniObrazovky.Obrazovka;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Consumer;

import static cz.vse.praguePub.gui.komponenty.Komponenty.*;

public class Prihlaseni extends Obrazovka<BorderPane> {

    private final BiFunction<String, String, Boolean> jmenoHesloCallback;
    private final Runnable pozadavekNaSkrytiOkna;
    
    private final Consumer<Boolean> alert = prihlaseniUspesne -> 
            new AlertBuilder(prihlaseniUspesne ? Alert.AlertType.INFORMATION : Alert.AlertType.ERROR)
                .setTitle("PraguePub")
                .setHeaderText("Přihlášení")
                .setContent("Přihlášení proběhlo " + (prihlaseniUspesne ? "úspěšně": "neúspěšně"))
                .getAlert()
                .show();
    

    public Prihlaseni(BiFunction<String, String, Boolean> jmenoHesloCallback, Runnable pozadavekNaSkrytiOkna) {
        super(new BorderPane(), 300, 350, "background");
        this.jmenoHesloCallback = jmenoHesloCallback;
        this.pozadavekNaSkrytiOkna = pozadavekNaSkrytiOkna;

        this.registrujInputy();
        this.vytvorGUI();
    }

    private void registrujInputy() {

        PasswordField hesloField = new PasswordField();
        hesloField.getStyleClass().add("tlacitkoAplikace");
        hesloField.setText("hvezdicky");
        hesloField.setOnMouseClicked(e -> {
            hesloField.clear();
            hesloField.setOnMouseClicked(null);
        });
        hesloField.setMaxWidth(150d);

        this.getMapaInputu().putAll(
                Map.of(
                        "jmeno", TextFieldAplikace("Jméno", t -> t.setMaxWidth(150d)),
                        "heslo", hesloField
                )
        );
    }

    private void prihlasitUzivatele() {
        TextField jmenoTF = this.getMapaInputu().get("jmeno");
        TextField hesloTF = this.getMapaInputu().get("heslo");

        final boolean vysledekPrihlaseni = this.jmenoHesloCallback.apply(jmenoTF.getText(), hesloTF.getText());
        this.alert.accept(vysledekPrihlaseni);
        
        if (vysledekPrihlaseni) this.pozadavekNaSkrytiOkna.run();
    }

    private void vytvorGUI() {
        Hyperlink link = new Hyperlink("Vytvořit účet");
        link.setFont(Font.font("Helvetica", FontWeight.BOLD, 10));
        link.setAlignment(Pos.BASELINE_CENTER);

        this.getPane().setTop(
                Sloupec(List.of(
                        NadpisOknaLabel("Vítej zpět!"),

                        this.getMapaInputu().get("jmeno"),
                        this.getMapaInputu().get("heslo"),

                                TlacitkoAplikace("Přihlásit se", t -> {
                            t.setStyle("-fx-font-weight: bold;");
                            t.setOnMouseClicked(mouseEvent -> this.prihlasitUzivatele());
                        })
                ),
                    textFieldVBox -> {
                        textFieldVBox.setSpacing(20);
                        textFieldVBox.setPadding(new Insets(60,0,20,0));
                        textFieldVBox.setAlignment(Pos.BASELINE_CENTER);
                    }
                )
        );
    }
}



