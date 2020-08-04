package jetoze.gunga.widget;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkState;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;
import javax.swing.JComponent;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import com.google.common.collect.ImmutableList;

public class TableWidget implements Widget {

    private final JTable table;
    private final JScrollPane ui;
    
    public TableWidget() {
        table = new JTable();
        initTable();
        ui = new JScrollPane(table);
    }
    
    public TableWidget(TableModel model) {
        table = new JTable(model);
        initTable();
        ui = new JScrollPane(table);
    }
    
    private void initTable() {
        table.setFillsViewportHeight(true);
        setVisibleRowCount(8);
    }
    
    public void setVisibleRowCount(int visibleRows) {
        checkArgument(visibleRows > 0, "visibleRows must be > 0");
        Dimension dim = table.getPreferredScrollableViewportSize();
        dim.height = table.getRowHeight() * visibleRows;
        table.setPreferredScrollableViewportSize(dim);
    }

    @Override
    public JComponent getUi() {
        return ui;
    }

    @Override
    public void requestFocus() {
        table.requestFocusInWindow();
    }
    
    public static Builder builder(String... columnNames) {
        return new Builder(columnNames);
    }
    
    public static Builder builder(List<String> columnNames) {
        return new Builder(columnNames);
    }

    
    public static class Builder {
        
        private final ImmutableList<String> columnNames;
        private final List<Object[]> rows = new ArrayList<>();
        @Nullable
        private Integer visibleRowCount;

        private Builder(List<String> columnNames) {
            checkArgument(!columnNames.isEmpty(), "Must provide at least one column");
            this.columnNames = ImmutableList.copyOf(columnNames);
        }

        private Builder(String... columnNames) {
            checkArgument(columnNames.length > 0, "Must provide at least one column");
            this.columnNames = ImmutableList.copyOf(columnNames);
        }
        
        public Builder addRow(Object... values) {
            checkArgument(values.length == columnNames.size(), "Expected %d values, got %d", columnNames.size(), values.length);
            rows.add(values);
            return this;
        }
        
        public Builder withVisibleRowCount(int visibleRows) {
            checkArgument(visibleRows > 0, "visibleRows must be > 0");
            this.visibleRowCount = visibleRows;
            return this;
        }
        
        public TableWidget build() {
            DefaultTableModel model = createModel();
            TableWidget table = new TableWidget(model);
            if (visibleRowCount != null) {
                table.setVisibleRowCount(visibleRowCount);
            }
            return table;
        }

        private DefaultTableModel createModel() {
            checkState(columnNames != null, "Must provide column names");
            Object[][] data = new Object[rows.size()][];
            for (int n = 0; n < data.length; ++n) {
                data[n] = rows.get(n);
            }
            DefaultTableModel model = new DefaultTableModel(data, columnNames.toArray());
            return model;
        }
    }
    
}
