package text_editor.gui;

import text_editor.ColumnType;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

/**
 * <p><b>Graphic Interface class.</b></p>
 * <p>Manages the Table window interface.</p>
 * <p><i>This class makes use of an intellij form to connect the GUI objects to the code</i></p>
 */
public class TableGUI {
    private JPanel panel;
    private JTable symbolsTable;
    private final DefaultTableModel tableModel;

    /**
     * <p><b>Graphic Interface class.</b></p>
     * <p>Manages the Table window interface.</p>
     * <p><i>This class makes use of an intellij form to connect the GUI objects to the code</i></p>
     */
    public TableGUI() {
//        panel.setBorder(BorderFactory.createTitledBorder("Table"));
        symbolsTable.setModel((tableModel = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        }));
        tableModel.setColumnIdentifiers(ColumnType.values());
        // move "Token id" column to position 0
        symbolsTable.moveColumn(4, 0);
        // move "Token" column to position 1
        symbolsTable.moveColumn(4, 1);


    }

    public JPanel getPanel() {
        return panel;
    }

    public JTable getSymbolsTable() {
        return symbolsTable;
    }

    public DefaultTableModel getTableModel() {
        return tableModel;
    }
}
