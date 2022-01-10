package cz.vse.praguePub.gui.komponenty;

import cz.vse.praguePub.logika.dbObjekty.DBObjekt;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

/**
 * Třída tabulky, která je schopná zobrazit seznam piv nebo podniků z databáze.
 * @param <T> definice datového typu - řádků
 */
public class Tabulka<T extends DBObjekt> {
    @Getter private final TableView<T> tableView;

    public Tabulka(String[][] nazvySloupcuAatributu) {
        this.tableView = new TableView<>();
        this.vlozSloupce(nazvySloupcuAatributu);
    }

    /**
     * Metoda nastavuje sloupce tabulky a vytváří instanci PropertyValueFactory pro
     * jednoduché vkládání dat do tabulky.
     * @param sloupce matice 2xn, kde 1. sloupec je String, který se má zobrazit jako nadpis
     * a 2. sloupec reprezentuje názvy atributů instancí vypisovaných do tabulky.
     */
    private void vlozSloupce(String[][] sloupce) {
        List<TableColumn<T, String>> sloupceKVlozeni = new ArrayList<>();

        for (String[] vals : sloupce) {
            TableColumn<T, String> tableColumn = new TableColumn<>(vals[0]);
            tableColumn.setCellValueFactory(new PropertyValueFactory<>(vals[1]));
            tableColumn.prefWidthProperty().bind(this.tableView.widthProperty().divide(sloupce.length));

            sloupceKVlozeni.add(tableColumn);
        }

        this.tableView.getColumns().addAll(sloupceKVlozeni);
    }

    /**
     * Metoda vymaže objekty vložené v defaultním ObservableList u TableView a vloží instance z parametru. <br>
     * Pro nastavení jiného seznamu ke sledování je třeba přistupovat přímo k TableView (tabulkaInstance.getTableView().setItems(ObservableList&lt;T&gt;)
     * @param instanceDBObjektu List obsahující objekty, které se mají vypsat do tabulky
     */
    public void setRadky(List<T> instanceDBObjektu) {
        this.tableView.getItems().clear();
        this.tableView.getItems().addAll(instanceDBObjektu);
    }
}
