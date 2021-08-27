package taller_2;

import taller_2.gui.MainGUI;
import taller_2.gui.TableGUI;

import javax.swing.*;
import java.awt.*;

/**
 * TODO: DOCUMENT ALL
 */
public class SystemGUI implements WindowEvents {


    private final JFrame mainWindow;
    private final JFrame tableWindow;
    private final Analyzer analyzer;
    private final MainGUI mainPage;
    private final TableGUI tableGUI;


    // creating Objects
    {
        analyzer = new Analyzer();
        mainPage = new MainGUI(analyzer, this);
        tableGUI = new TableGUI(analyzer, this);

    }

    public SystemGUI() {

        mainWindow = new JFrame("APP");
        mainWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainWindow.add(mainPage.getTabbedPane());
        resizeWindow(mainWindow);

        mainWindow.setLocationRelativeTo(null);
        mainWindow.setResizable(false);
        mainWindow.setVisible(true);

        tableWindow = new JFrame("Table");
        tableWindow.add(tableGUI.getTablePanel());
        tableWindow.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        tableWindow.setVisible(false);
        resizeWindow(tableWindow);


    }

    public static void main(String[] args) {
//        EventQueue.invokeLater(new Runnable() {
//            @Override
//            public void run() {
//                new SystemGUI();
//            }
//        });

//        EventQueue.invokeLater(() -> new SystemGUI());
        EventQueue.invokeLater(SystemGUI::new);
    }

    @Override
    public void resizeWindow(JFrame window) {
        window.pack();
    }

    @Override
    public void showTable() {
        if (tableWindow != null) {
            tableWindow.setVisible(true);
        }
    }

    @Override
    public void displaySymbolsData() {
        if (tableWindow != null) {
            var m = tableGUI.getTableModel();
            m.setRowCount(0);
            var s = analyzer.analyzeFile();
            for (String[] strings : analyzer.analyzeFile()) {
                m.addRow(strings);
            }
            m.fireTableDataChanged();
        }
    }
}

