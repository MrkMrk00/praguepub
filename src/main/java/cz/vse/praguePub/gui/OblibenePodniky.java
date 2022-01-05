package cz.vse.praguePub.gui;

import javafx.geometry.Pos;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import static cz.vse.praguePub.gui.komponenty.Komponenty.HorniPanel;
import static cz.vse.praguePub.gui.komponenty.Komponenty.NadpisOknaLabel;

public class OblibenePodniky extends Obrazovka<BorderPane> {

    private final Stage oknoProOblibenePodniky;

    public OblibenePodniky() {
        super(new BorderPane(),  900, 600, "background");

        this.oknoProOblibenePodniky = new Stage();

        this.vytvorGui();
    }


    private void vytvorGui(){
        this.getPane().setTop(
                HorniPanel(
                        horniPanel -> {
                            horniPanel.getChildren().add(NadpisOknaLabel("Oblíbené podniky"));
                            horniPanel.setAlignment(Pos.BASELINE_LEFT);
                        }
                )
        );
    }
}
