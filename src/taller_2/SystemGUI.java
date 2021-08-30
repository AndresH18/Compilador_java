package taller_2;

import taller_2.gui.ArithmeticGUI;
import taller_2.gui.MainGUI;
import taller_2.gui.TableGUI;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

/**
 * <p>Main Class for the GUI.</p>
 */
public class SystemGUI implements WindowEvents {

    /**
     * <p>Window {@code JFrame} containing the main menu.</p>
     */
    private final JFrame mainWindow;

    /**
     * <p>Window {@code JFrame} containing the symbols table.</p>
     */
    private final JFrame tableWindow;

    /**
     * <p>Window {@code JFrame} containing the Arithmetic expressions.</p>
     */
    private final JFrame mathWindow;

    /**
     * <p>{@link Analyzer} instance.</p>
     */
    private final Analyzer analyzer;

    /**
     * <p>{@link MainGUI} instance.</p>
     */
    private final MainGUI mainPage;

    /**
     * <p>{@link TableGUI} instance.</p>
     */
    private final TableGUI tableGUI;

    /**
     * <p>{@link ArithmeticGUI] instance.}</p>
     */
    private final ArithmeticGUI mathGUI;


    // creating Objects
    {
        analyzer = new Analyzer();
        mainPage = new MainGUI(analyzer, this);
        tableGUI = new TableGUI(analyzer, this);
        mathGUI = new ArithmeticGUI(analyzer, this);

    }

    /**
     * <p>Class constructor</p>
     */
    public SystemGUI() {
        // initializing mainWindow
        mainWindow = new JFrame(Analyzer.LANGUAGE_NAME + " Compiler");
        // setting close operation to terminate program
        mainWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // adding mainPage panel
        mainWindow.add(mainPage.getTabbedPane());
        // resizing to fit content
        resizeWindow(mainWindow);
        // setting location to the middle of the screen
        mainWindow.setLocationRelativeTo(null);
        // disabling resize
        mainWindow.setResizable(false);
        // set visibly
        mainWindow.setVisible(true);

        // initializing tableWindow
        tableWindow = new JFrame("Symbols Table");
        // setting close operation to hide
        tableWindow.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        // adding tableGUI panel
        tableWindow.add(tableGUI.getTablePanel());
        // setting visibility to false
        tableWindow.setVisible(false);
        // resizing to fit content
        resizeWindow(tableWindow);

        // initializing
        mathWindow = new JFrame("Arithmetic Expressions");
        // setting close operation to hide
        mathWindow.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        // adding mathGUI panel
        mathWindow.add(mathGUI.getMathPanel());
        // set location relative to tableWindow
        mathWindow.setLocationRelativeTo(tableWindow);
        // set visible to false
        mathWindow.setVisible(false);
        // disable resize
        mathWindow.setResizable(false);
        // resize to fit content
        resizeWindow(mathWindow);

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
        // check that it's not null
        if (tableWindow != null) {
            // set visible
            tableWindow.setVisible(true);
        }
    }


    @Override
    public void modifySymbolsData() {
        // verify that it's not null
        if (tableWindow != null) {
            // get tableModel
            var m = tableGUI.getTableModel();
            // remove items
            m.setRowCount(0);
            // add all items of the analyzed file
            for (String[] strings : Objects.requireNonNull(analyzer.analyzeFile())) {
                m.addRow(strings);
            }
            // notify tableModel data has changed
            m.fireTableDataChanged();
        }
    }

    @Override
    public void showMath() {
        // check that it's not null
        if (mathWindow != null) {
            // set visible
            mathWindow.setVisible(true);
        }
    }

    @Override
    public void updateMath() {

        if (mathGUI != null) {
            var m = mathGUI.getListModel();
            m.removeAllElements();
            for (String s : analyzer.findMathExpressions()) {
                m.addElement(s);
            }
//            m.addAll(analyzer.findMathExpressions());
        }

    }
}

