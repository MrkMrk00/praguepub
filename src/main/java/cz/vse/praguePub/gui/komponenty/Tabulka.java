package cz.vse.praguePub.gui.komponenty;

import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import lombok.Getter;

import java.util.List;
import java.util.Map;

public class Tabulka<T> {
    @Getter private final TableView<T> tableView;

    public Tabulka(Map<String, String> nazvySloupcuAatributu) {
        this.tableView = new TableView<>();
        this.vlozSloupce(nazvySloupcuAatributu);
    }

    private void vlozSloupce(Map<String, String> sloupce) {
        sloupce.forEach(
                (nazev, atr) -> {
                    TableColumn<T, String> tableColumn = new TableColumn<>(nazev);
                    tableColumn.setCellValueFactory(new PropertyValueFactory<>(atr));
                    this.tableView.getColumns().add(tableColumn);
                }
        );
    }

    public void setRadky(List<T> instanceDBObjektu) {
        this.tableView.getItems().clear();
        this.tableView.getItems().addAll(instanceDBObjektu);
    }
}
