package taller_2.gui;

import taller_1.Sistema;
import taller_2.WindowEvents;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

public class MainPageG {

    private final WindowEvents windowEvents;
    private final JFileChooser fileChooser;

    private JPanel MAIN_PANEL;
    private JButton search_btn;
    private JLabel label1;
    private JLabel fileName_lbl;
    private JButton btn;


    private java.io.File file;

    {
        this.fileChooser = new JFileChooser(Sistema.PROJECT_DIRECTORY);
        this.fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        this.fileChooser.setFileFilter(new FileNameExtensionFilter(Sistema.LANGUAGE_NAME + " file", Sistema.LANGUAGE_EXTENSION));
    }

    public MainPageG() {
        this(null);
    }

    public MainPageG(WindowEvents windowEvents) {
        this.windowEvents = windowEvents;


        search_btn.addActionListener(e -> {

            if (fileChooser.showOpenDialog(this.MAIN_PANEL) == JFileChooser.APPROVE_OPTION) {
                file = fileChooser.getSelectedFile();
                fileName_lbl.setText(file.getName());
            }
            resizeWindow();

        });

        btn.addActionListener(e -> {

        });
    }

    private void resizeWindow() {
        if (windowEvents != null) {
            windowEvents.resize();
        }
    }

    public JPanel getMAIN_PANEL() {
        return MAIN_PANEL;
    }
}
