package cz.vse.praguePub.gui.komponenty;

import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.List;
import java.util.function.Consumer;

public final class Komponenty {

    public static Button TlacitkoAplikace(String text, Consumer<Button> styluj) {
        Button button = new Button(text);
        button.getStyleClass().add("tlacitkoAplikace");
        button.setAlignment(Pos.BASELINE_RIGHT);
        styluj.accept(button);
        return button;
    }

    public static HBox HorniPanel(Consumer<HBox> styluj) {
        HBox hbox = new HBox();
        hbox.getStyleClass().add("vrchniPanel");
        styluj.accept(hbox);
        return hbox;
    }

    public static VBox Sloupec(Node... pridej) {
        VBox vBox = new VBox(10);
        vBox.getStyleClass().add("sloupec");
        vBox.getChildren().addAll(pridej);

        return vBox;
    }

    public static VBox Sloupec(List<Node> pridej, Consumer<VBox> styluj) {
        VBox vBox = Sloupec(pridej.toArray(new Node[]{}));
        styluj.accept(vBox);
        return vBox;
    }

    public static HBox Radek(Node... pridej) {
        HBox hBox = new HBox(10);
        hBox.getStyleClass().add("radek");
        hBox.getChildren().addAll(pridej);
        return hBox;
    }

    public static HBox Radek(List<Node> pridej, Consumer<HBox> styluj) {
        HBox hBox = Radek(pridej.toArray(new Node[]{}));
        styluj.accept(hBox);
        return hBox;
    }

    public static TextField TextFieldAplikace(String defaultText, Consumer<TextField> styluj) {
        TextField textField = new TextField(defaultText);
        textField.getStyleClass().add("tlacitkoAplikace");
        textField.setOnMouseClicked(
                event -> {
                    textField.clear();
                    textField.setOnMouseClicked(e -> {});
                }
        );

        styluj.accept(textField);
        return textField;
    }

    public static TextField TextFieldAplikace(String defaultText, EventHandler<MouseEvent> onClick, Consumer<TextField> styluj) {
        TextField textField = new TextField(defaultText);
        textField.getStyleClass().add("tlacitkoAplikace");
        textField.setOnMouseClicked(onClick);

        styluj.accept(textField);
        return textField;
    }

    public static Label LabelAplikace(String defaultText) {
        Label label = new Label(defaultText);
        label.getStyleClass().addAll("tlacitkoAplikace", "labelAplikace");
        label.setPrefWidth(150);
        return label;
    }

    public static Label LabelAplikace(String defaultText, Consumer<Label> styluj) {
        Label label = LabelAplikace(defaultText);
        styluj.accept(label);
        return label;
    }

    public static Label NadpisOknaLabel(String text) {
        Label label = new Label(text);
        label.getStyleClass().add("nadpisLabel");

        return label;
    }

    public static VBox ProstorV(Consumer<VBox> styluj) {
        VBox vbox = new VBox();
        vbox.getStyleClass().add("background");
        styluj.accept(vbox);
        return vbox;
    }

    public static HBox ProstorH(Consumer<HBox> styluj) {
        HBox hbox = new HBox();
        hbox.getStyleClass().add("backgroun");
        styluj.accept(hbox);
        return hbox;
    }
}