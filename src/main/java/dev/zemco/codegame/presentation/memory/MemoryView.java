package dev.zemco.codegame.presentation.memory;

import dev.zemco.codegame.util.BindingUtils;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.value.ObservableObjectValue;
import javafx.beans.value.ObservableStringValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MemoryView extends TableView<IMemoryCellObserver> implements Initializable {

    private static final int WORKING_CELL_ADDRESS = 0;
    private static final String WORKING_CELL_ADDRESS_ALIAS = "Working Cell";
    private static final String NO_VALUE_TEXT = "-";

    @FXML
    private TableColumn<IMemoryCellObserver, String> addressColumn;

    @FXML
    private TableColumn<IMemoryCellObserver, String> valueColumn;

    public MemoryView() throws IOException {
        FXMLLoader loader = new FXMLLoader(MemoryView.class.getResource("/fxml/MemoryControl.fxml"));
        loader.setController(this);
        loader.setRoot(this);
        loader.load();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.addressColumn.minWidthProperty().bind(
                this.widthProperty().divide(2)
        );

        this.valueColumn.minWidthProperty().bind(
                this.widthProperty().divide(2)
        );

        this.addressColumn.setCellValueFactory(this::getAddressTableCellValue);
        this.valueColumn.setCellValueFactory(this::getValueTableCellValue);
    }

    private ObservableStringValue getAddressTableCellValue(CellDataFeatures<IMemoryCellObserver, String> features) {
        IMemoryCellObserver cellObserver = features.getValue();
        int address = cellObserver.getAddress();

        String addressText = address == WORKING_CELL_ADDRESS
                ? WORKING_CELL_ADDRESS_ALIAS
                : String.valueOf(address);

        return new ReadOnlyStringWrapper(addressText).getReadOnlyProperty();
    }

    // TODO: maybe rename?
    private ObservableStringValue getValueTableCellValue(CellDataFeatures<IMemoryCellObserver, String> features) {
        IMemoryCellObserver cellObserver = features.getValue();
        ObservableObjectValue<Integer> value = cellObserver.getValue();

        return Bindings.when(Bindings.isNotNull(value))
                .then(BindingUtils.mapOrNull(value, String::valueOf))
                .otherwise(NO_VALUE_TEXT);
    }

}
