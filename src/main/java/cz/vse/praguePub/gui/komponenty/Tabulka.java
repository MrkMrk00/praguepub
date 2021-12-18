package cz.vse.praguePub.gui.komponenty;

import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Tabulka<T> {
    @Getter private final TableView<T> tableView;

    public Tabulka(String[][] nazvySloupcuAatributu) {
        this.tableView = new TableView<>();
        this.vlozSloupce(nazvySloupcuAatributu);
    }

    private void vlozSloupce(String[][] sloupce) {
        List<TableColumn<T, String>> sloupceKVlozeni = new ArrayList<>();

        Arrays.stream(sloupce).forEach(
                (vals) -> {
                    TableColumn<T, String> tableColumn = new TableColumn<>(vals[0]);
                    tableColumn.setCellValueFactory(new PropertyValueFactory<>(vals[1]));
                    sloupceKVlozeni.add(tableColumn);
                }
        );
        this.tableView.getColumns().addAll(sloupceKVlozeni);
    }

    public void setRadky(List<T> instanceDBObjektu) {
        this.tableView.getItems().clear();
        this.tableView.getItems().addAll(instanceDBObjektu);
    }
}
