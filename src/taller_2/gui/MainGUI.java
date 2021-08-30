package taller_2.gui;


import taller_2.Analyzer;
import taller_2.WindowEvents;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * <p>Graphic Interface class</p>
 * <p>This class makes use of an intellij form to connect the GUI objects to the code</p>
 */
public class MainGUI {

    /**
     * <p>Analyzer.</p>
     */
    private final Analyzer analyzer;

    /**
     * <p>Window Events Interface.</p>
     */
    private final WindowEvents windowEvents;

    /**
     * <p>File Browser to select the File.</p>
     */
    private final JFileChooser fileChooser;

    /**
     * <p>Tabbed Pane to hold the different Tabs.</p>
     */
    private JTabbedPane tabbedPane;

    /**
     * <p>Panel for the file Selection.</p>
     */
    private JPanel filePanel;

    /**
     * <p>Panel for examining the code.</p>
     */
    private JPanel codePanel;

    /**
     * <p>Panel for the Table.</p>
     */
    private JPanel tableOptionsPanel;

    /**
     * <p>"File" label.</p>
     */
    private JLabel file_lbl;

    /**
     * <p>TextField to write the file name.</p>
     */
    private JTextField fileName_txf;

    /**
     * <p>TextArea to present the code.</p>
     */
    private JTextArea codeArea_txtArea;

    /**
     * <p>Button to open file browser.</p>
     */
    private JButton search_btn;

    /**
     * <p>Button to analyze File.</p>
     */
    private JButton analyze_btn;

    /**
     * <p>CheckBox to toggle between the File name or the File Absolute Path.</p>
     */
    private JCheckBox fileFullPath_ckbx;
    private JButton tableButton;
    private JButton arithmeticButton;

    /**
     * <p>Holds the selected File.</p>
     */
    private File file;


    // Configures the file chooser
    {
        // create the Browser
        this.fileChooser = new JFileChooser(Analyzer.PROJECT_DIRECTORY);
        // set accepting type
        this.fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        // set accepting files
        this.fileChooser.setFileFilter(new FileNameExtensionFilter(Analyzer.LANGUAGE_NAME + " file", Analyzer.LANGUAGE_EXTENSION));
    }

    // Configures the buttons characteristics.
    {
        //
        search_btn.setIcon(UIManager.getIcon("Tree.openIcon"));
    }


    /**
     * <p>Constructor to create UI Panels</p>
     *
     * @param analyzer     the analyzer to use
     * @param windowEvents interface to interact with the window
     */
    public MainGUI(Analyzer analyzer, WindowEvents windowEvents) {
        this.analyzer = analyzer;
        // passing the Window Event for use
        this.windowEvents = windowEvents;

        /*
         * adding action listener for search button
         */
        search_btn.addActionListener(e -> {
            // open File browser and checks if approve option was selected
            if (fileChooser.showOpenDialog(this.filePanel) == JFileChooser.APPROVE_OPTION) {
                // selects file
                file = fileChooser.getSelectedFile();
                // sets the file name on the text field
                fileName_txf.setText(file.getName());
            }
            // resizes the window
//            resizeWindow();

        });

        /*
         * adding action listener
         */
        tableButton.addActionListener(e -> windowEvents.showTable());

        /*
         * adding action listener
         */
        arithmeticButton.addActionListener(e -> windowEvents.showMath());

        /*
         * adding action listener for analyse button
         */
        analyze_btn.addActionListener(this::analyzeSelectedFile);
        /*
         * adding action listener for txt field,
         * triggered when "enter" key is pressed while the txt field is focused
         */
        fileName_txf.addActionListener(this::analyzeSelectedFile);

        /*
         * adding state change listener for the tabbed pane,
         * triggered when a different tab is selected
         * TODO: delete listener?
         */
        tabbedPane.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                // TODO: Implement
                System.out.println("tab's state changed");
//                resizeWindow(); // dont use here, because if the text area changes size, the window will increase size and will not become smaller again
            }
        });

        /*
         * adding action listener for the full path combo box
         */
        fileFullPath_ckbx.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // check if combobox is selected
                if (fileFullPath_ckbx.isSelected() && file != null) {
                    fileName_txf.setText(file.getAbsolutePath());
                } else if (!fileFullPath_ckbx.isSelected() && file != null) {
                    fileName_txf.setText(file.getName());
                }
            }
        });

    }

    /**
     * <p>Checks if the file is valid and if it is, analyzes it.</p>
     *
     * @param e action event sent by the caller
     */
    private void analyzeSelectedFile(ActionEvent e) {
        // file is not selected or text field is empty
        if (file == null && fileName_txf.getText().isBlank()) {
            JOptionPane.showMessageDialog(tabbedPane, "Select a file", "", JOptionPane.WARNING_MESSAGE);

        } else if ((file = isValidFile()) != null) { // checks if the file is valid
            // analyze file
            analyse();
//            tabbedPane.setSelectedIndex(1);
        } else {
            System.out.println("no existe");
            // show error message to user
            JOptionPane.showMessageDialog(tabbedPane, "Invalid File", "", JOptionPane.ERROR_MESSAGE);
        }
    }


//    /**
//     * <p>Adjust the window to fit contents</p>>
//     */
//    private void resizeWindow() {
//        if (windowEvents != null) {
//            windowEvents.resizeWindow();
//        }
//    }

    /**
     * <p>Checks if the file is valid.</p>
     * <p>This is necessary because the file can be selected using the file browser as well as by
     * typing it's name(path).</p>
     *
     * @return The Abstract representation(java.io.File) of the file, or null if not valid.
     */
    private File isValidFile() {
        if (file != null) {
            if (file.getName().equals(fileName_txf.getText())) {
//                return true;
                return file;
            } else {
                File f = new File(fileName_txf.getText());
                return f.exists() && !f.isDirectory() && f.getName().endsWith('.' + Analyzer.LANGUAGE_EXTENSION) ? f : null;
            }
        } else {
            File f = new File(fileName_txf.getText());
            return f.exists() && !f.isDirectory() && f.getName().endsWith('.' + Analyzer.LANGUAGE_EXTENSION) ? f : null;
        }
    }

    /**
     * TODO: DOCUMENT
     */
    private void analyse() {

        System.out.println("Analyzing: " + file.getName());
        displayCode();
        analyzer.setCodeFile(file);
        windowEvents.modifySymbolsData();
        windowEvents.showTable();
        windowEvents.updateMath();
        windowEvents.showMath();

    }

    /**
     * TODO: DOCUMENT
     */
    private void displayCode() {
        try (BufferedReader in = new BufferedReader(new FileReader(file))) {
            codeArea_txtArea.read(in, null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public JTabbedPane getTabbedPane() {
        return tabbedPane;
    }

}
