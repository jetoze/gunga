package jetoze.gunga.widget;

import static com.google.common.base.Preconditions.checkArgument;

import java.awt.Dimension;

import javax.swing.JComponent;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.TableModel;

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

}
