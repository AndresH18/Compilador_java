package taller_2.gui;

import taller_1.Sistema;
import taller_2.WindowEvents;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;

/**
 * <p>Graphic Interface class</p>
 * <p>This class makes use of an intellij form to connect the GUI objects to the code</p>
 */
public class MainPageTab {

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
    private JPanel tablePanel;

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
    private JCheckBox fileFullPath_cbx;

    /**
     * <p>Holds the selected File.</p>
     */
    private File file;


    // Configures the file chooser
    {
        // create the Browser
        this.fileChooser = new JFileChooser(Sistema.PROJECT_DIRECTORY);
        // set accepting type
        this.fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        // set accepting files
        this.fileChooser.setFileFilter(new FileNameExtensionFilter(Sistema.LANGUAGE_NAME + " file", Sistema.LANGUAGE_EXTENSION));
    }


    // Configures the buttons characteristics.
    {
        //
        search_btn.setIcon(UIManager.getIcon("Tree.openIcon"));
    }

    /**
     * <p>Constructor to create UI Panels</p>
     *
     * @param windowEvents - interface to interact with the window
     */
    public MainPageTab(WindowEvents windowEvents) {
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
            resizeWindow();

        });
        /*
         * adding action listener for analyse button
         */
        analyze_btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // checks if the file is valid
                if ((file = isValidFile()) != null) {
                    // analyze file
                    analyse();
                } else {
                    System.out.println("no existe");
                    // show error message to user
                    JOptionPane.showMessageDialog(tabbedPane, "Invalid File", "", JOptionPane.WARNING_MESSAGE);
                }
            }
        });
        /*
         * adding state change listener for the tabbed pane,
         * triggered when a different tab is selected
         */
        tabbedPane.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                // TODO: Implement
                System.out.println("tab's state changed");
                resizeWindow();
            }
        });

        /*
         * adding action listener for txt field,
         * triggered when "enter" key is pressed while the txt field is focused
         */
        fileName_txf.addActionListener(e -> {
            System.out.println("TextField Action");
            analyse();
        });

        /*
         * adding action listener for the full path combo box
         */
        fileFullPath_cbx.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (fileFullPath_cbx.isSelected() && file != null) {
                    fileName_txf.setText(file.getAbsolutePath());
                } else if (!fileFullPath_cbx.isSelected() && file != null) {
                    fileName_txf.setText(file.getName());
                }
            }
        });
    }

    /**
     * <p>Adjust the window to fit contents</p>>
     */
    private void resizeWindow() {
        if (windowEvents != null) {
            windowEvents.resize();
        }
    }

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
                return f.exists() && !f.isDirectory() && f.getName().endsWith('.' + Sistema.LANGUAGE_EXTENSION) ? f : null;
            }
        } else {
            File f = new File(fileName_txf.getText());
            return f.exists() && !f.isDirectory() && f.getName().endsWith('.' + Sistema.LANGUAGE_EXTENSION) ? f : null;
        }
    }

    private void analyse() {

        System.out.println(file.getName());

//            read();

    }

    private void read() {
        if (file != null) {
            try (BufferedReader in = new BufferedReader(new FileReader(file))) {
                StringBuilder sb = new StringBuilder();
                String s;
                while ((s = in.readLine()) != null)
                    sb.append(s);
                codeArea_txtArea.setText(sb.toString());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    public JPanel getFirstPanel() {
        return filePanel;
    }

    public JTabbedPane getTabbedPane() {
        return tabbedPane;
    }

    public JPanel getSecondPanel() {
        return codePanel;
    }
}
