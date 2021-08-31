package taller_2.gui;

import taller_2.Analyzer;
import taller_2.ColumnType;
import taller_2.WindowEvents;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;


/**
 * <p><b>Graphic Interface class.</b></p>
 * <p>Manages the Table window interface.</p>
 * <p><i>This class makes use of an intellij form to connect the GUI objects to the code</i></p>
 */
public class TableGUI {

    private final Analyzer analyzer;
    private final WindowEvents windowEvents;
    private JPanel tablePanel;
    private JTable symbolsTable;
    private DefaultTableModel tableModel;

    /**
     * <p>Creates an instance of the TableGUI.</p>
     *
     * @param analyzer     the analyzer to use
     * @param windowEvents interface to interact with the window
     */
    public TableGUI(Analyzer analyzer, WindowEvents windowEvents) {
        if (analyzer == null) {
            throw new NullPointerException("Analyzer cannot be null.");
        }
        this.analyzer = analyzer;
        this.windowEvents = windowEvents;

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

    public JPanel getTablePanel() {
        return tablePanel;
    }

    public JTable getSymbolsTable() {
        return symbolsTable;
    }

    public DefaultTableModel getTableModel() {
        return tableModel;
    }
}
