package dev.zemco.codegame.presentation.memory;

import dev.zemco.codegame.util.BindingUtils;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.value.ObservableObjectValue;
import javafx.beans.value.ObservableStringValue;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.util.Callback;

import static javafx.scene.layout.Region.USE_COMPUTED_SIZE;

public class MemoryView extends TableView<IMemoryCellObserver> {

    private static final int WORKING_CELL_ADDRESS = 0;
    private static final String WORKING_CELL_ADDRESS_ALIAS = "Working Cell";
    private static final String NO_VALUE_TEXT = "-";

    public MemoryView() {
        super();

        this.addNamedColumn("Address", this::getAddressTableCellValue);
        this.addNamedColumn("Value", this::getValueTableCellValue);
    }

    private void addNamedColumn(
        String columnTitle,
        // TODO: document this
        Callback<CellDataFeatures<IMemoryCellObserver, String>, ObservableValue<String>> cellValueFactory
    ) {
        var column = new TableColumn<IMemoryCellObserver, String>(columnTitle);
        
        DoubleBinding columnWidth = this.widthProperty().divide(2);
        column.minWidthProperty().bind(columnWidth);
        column.prefWidthProperty().bind(columnWidth);
        column.setMaxWidth(USE_COMPUTED_SIZE);

        column.setEditable(false);
        column.setResizable(false);
        column.setSortable(false);
        column.setCellValueFactory(cellValueFactory);

        this.getColumns().add(column);
    }

    private ObservableStringValue getAddressTableCellValue(CellDataFeatures<IMemoryCellObserver, String> features) {
        IMemoryCellObserver cellObserver = features.getValue();
        int address = cellObserver.getAddress();

        String addressText = address == WORKING_CELL_ADDRESS
            ? WORKING_CELL_ADDRESS_ALIAS
            : String.valueOf(address);

        return new ReadOnlyStringWrapper(addressText).getReadOnlyProperty();
    }

    // TODO: this name is a bit clunky - maybe rename?
    private ObservableStringValue getValueTableCellValue(CellDataFeatures<IMemoryCellObserver, String> features) {
        IMemoryCellObserver cellObserver = features.getValue();
        ObservableObjectValue<Integer> value = cellObserver.getValue();
        return BindingUtils.mapOrDefault(value, String::valueOf, NO_VALUE_TEXT);
    }

}
