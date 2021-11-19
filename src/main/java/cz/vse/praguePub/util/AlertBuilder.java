package cz.vse.praguePub.util;

import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.DialogEvent;
import lombok.Getter;

import java.util.function.Consumer;

public class AlertBuilder {
    @Getter private final Alert alert;
    
    public AlertBuilder(Alert.AlertType alertType) {
        this.alert = new Alert(alertType);
    }
    
    public AlertBuilder setTitle(String title) {
        this.alert.setTitle(title);
        return this;
    }
    
    public AlertBuilder setHeaderText(String text) {
        this.alert.setHeaderText(text);
        return this;
    }
    
    public AlertBuilder setContent(String text) {
        this.alert.setContentText(text);
        return this;
    }

    public AlertBuilder setGraphic(Node graphic) {
        this.alert.setGraphic(graphic);
        return this;
    }

    public AlertBuilder setOnCloseRequest(Consumer<DialogEvent> consumer) {
        this.alert.setOnCloseRequest(consumer::accept);
        return this;
    }
}
