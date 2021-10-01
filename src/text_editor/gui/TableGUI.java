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
    /**
     * <p>{@link DefaultTableModel} for the table</p>
     */
    private final DefaultTableModel tableModel;
    /**
     * <p>{@link JPanel} containing the elements</p>
     */
    private JPanel panel;
    /**
     * <p>{@link JTable} to display the symbols</p>
     */
    private JTable symbolsTable;

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
        // set the columns headers
        tableModel.setColumnIdentifiers(ColumnType.values());
        // move "Token id" column to position 0
        symbolsTable.moveColumn(4, 0);
        // move "Token" column to position 1
        symbolsTable.moveColumn(4, 1);


    }

    /**
     * @return the {@link JPanel}
     */
    public JPanel getPanel() {
        return panel;
    }

    /**
     * @return the {@link DefaultTableModel}
     */
    public DefaultTableModel getTableModel() {
        return tableModel;
    }
}
