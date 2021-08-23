package taller_2.gui;

import taller_2.Analyzer;
import taller_2.WindowEvents;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class TableGUI {
    private final Analyzer analyzer;
    private final WindowEvents windowEvents;
    private JPanel tablePanel;
    private JTable symbolsTable;
    private DefaultTableModel tableModel;

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
        tableModel.setColumnIdentifiers(analyzer.getElementsTypes());
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
