package dev.zemco.codegame.presentation.execution;

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

/**
 * JavaFX control presenting the state of a memory during execution. The state is presented using a table
 * displaying all memory cells as rows observed using {@link IMemoryCellObserver memory cell observers}.
 * This table contains two columns, one for the {@link IMemoryCellObserver#getAddress() address} of the memory cells,
 * and one for the {@link IMemoryCellObserver#valueProperty() observed value} of the memory cells.
 * <p>
 * {@link IMemoryCellObserver Memory cell observers} should be added to the control as items,
 * either through the {@link #itemsProperty()} or the {@link #getItems()}.
 *
 * @author Erik Zemčík
 * @see IMemoryCellObserver
 */
public class MemoryView extends TableView<IMemoryCellObserver> {

    private static final int WORKING_CELL_ADDRESS = 0;
    private static final String WORKING_CELL_ADDRESS_ALIAS = "Working Cell";
    private static final String NO_VALUE_TEXT = "-";

    /**
     * Creates an instance of {@link MemoryView}.
     * This instance needs {@link IMemoryCellObserver memory cell observers} added as presented items.
     */
    public MemoryView() {
        super();
        this.addNamedColumn("Address", this::getAddressTableCellValue);
        this.addNamedColumn("Value", this::getValueTableCellValue);
    }

    /**
     * Adds a new column to the table with the given name and value factory.
     * The added columns takes half of the width of the table, which means only two
     * columns may be added through this method.
     * <p>
     * To avoid mistakes during development, this method throws an {@link IllegalStateException}
     * if the table already has at least two columns.
     *
     * @param columnTitle title of the column
     * @param cellValueFactory factory to use to produce the cell values of the column
     *                         from the {@link IMemoryCellObserver memory cell observers}
     *
     * @throws IllegalStateException when the table already has at least two columns
     */
    private void addNamedColumn(
        String columnTitle,
        Callback<CellDataFeatures<IMemoryCellObserver, String>, ObservableValue<String>> cellValueFactory
    ) {
        if (this.getColumns().size() >= 2) {
            throw new IllegalStateException("Table already has at least two columns!");
        }

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

        // use the working cell alias for the working cell instead of the address
        String addressText = address == WORKING_CELL_ADDRESS
            ? WORKING_CELL_ADDRESS_ALIAS
            : String.valueOf(address);

        return new ReadOnlyStringWrapper(addressText).getReadOnlyProperty();
    }

    private ObservableStringValue getValueTableCellValue(CellDataFeatures<IMemoryCellObserver, String> features) {
        IMemoryCellObserver cellObserver = features.getValue();
        ObservableObjectValue<Integer> value = cellObserver.valueProperty();
        return BindingUtils.mapOrDefault(value, String::valueOf, NO_VALUE_TEXT);
    }

}
